package org.coffe.tomoko.localmusic

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.media.session.MediaSessionCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import org.coffe.tomoko.MainActivity
import org.coffe.tomoko.R

class LocalMusicService : Service() {

    interface Listener {
        fun onTrackChanged(track: LocalTrack?)
        fun onPlaybackStateChanged(isPlaying: Boolean)
        fun onPositionChanged(positionMs: Long, durationMs: Long)
    }

    inner class LocalBinder : Binder() {
        fun getService(): LocalMusicService = this@LocalMusicService
    }

    private val binder = LocalBinder()

    private var exoPlayer: ExoPlayer? = null
    private var mediaSession: MediaSessionCompat? = null
    private var sessionConnector: MediaSessionConnector? = null
    private var notificationManager: PlayerNotificationManager? = null

    private val tracks = mutableListOf<LocalTrack>()
    private var currentIndex: Int = -1

    private val listeners = mutableListOf<Listener>()

    var isPlaying: Boolean = false
        private set

    val currentTrack: LocalTrack?
        get() = tracks.getOrNull(currentIndex)

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        exoPlayer = ExoPlayer.Builder(this).build().apply {
            addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    this@LocalMusicService.isPlaying = isPlaying
                    notifyPlaybackStateChanged(isPlaying)
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_ENDED) {
                        next()
                    }
                }

                override fun onPositionDiscontinuity(
                    oldPosition: Player.PositionInfo,
                    newPosition: Player.PositionInfo,
                    reason: Int
                ) {
                    notifyPosition()
                }
            })
        }

        mediaSession = MediaSessionCompat(this, "TomokoLocalMusic").apply {
            isActive = true
        }
        sessionConnector = MediaSessionConnector(mediaSession!!).apply {
            setPlayer(exoPlayer)
        }

        notificationManager = PlayerNotificationManager.Builder(this, NOTIFICATION_ID, CHANNEL_ID)
            .setMediaDescriptionAdapter(object : PlayerNotificationManager.MediaDescriptionAdapter {
                override fun getCurrentContentTitle(player: Player): CharSequence =
                    currentTrack?.title ?: getString(R.string.app_name)

                override fun createCurrentContentIntent(player: Player): PendingIntent? {
                    val intent = Intent(this@LocalMusicService, LocalMusicActivity::class.java)
                    return PendingIntent.getActivity(
                        this@LocalMusicService, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                }

                override fun getCurrentContentText(player: Player): CharSequence? =
                    currentTrack?.artist

                override fun getCurrentLargeIcon(
                    player: Player,
                    callback: PlayerNotificationManager.BitmapCallback
                ): android.graphics.Bitmap? = null
            })
            .setNotificationListener(object : PlayerNotificationManager.NotificationListener {
                override fun onNotificationPosted(
                    notificationId: Int,
                    notification: Notification,
                    ongoing: Boolean
                ) {
                    if (ongoing) {
                        startForeground(notificationId, notification)
                    } else {
                        stopForeground(STOP_FOREGROUND_DETACH)
                    }
                }

                override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
                    stopForeground(STOP_FOREGROUND_REMOVE)
                    stopSelf()
                }
            })
            .build().apply {
                setPlayer(exoPlayer)
                setMediaSessionToken(mediaSession!!.sessionToken)
                setUseFastForwardActionInCompactView(true)
                setUseRewindActionInCompactView(true)
            }
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        androidx.media.session.MediaButtonReceiver.handleIntent(mediaSession, intent)
        return START_STICKY
    }

    fun addListener(listener: Listener) {
        listeners.add(listener)
        listener.onTrackChanged(currentTrack)
        listener.onPlaybackStateChanged(isPlaying)
        notifyPosition()
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    fun setPlaylist(newTracks: List<LocalTrack>, startIndex: Int = 0) {
        tracks.clear()
        tracks.addAll(newTracks)
        currentIndex = if (newTracks.isEmpty()) -1 else startIndex.coerceIn(0, newTracks.size - 1)
        loadCurrent(false)
    }

    fun playTrackAt(index: Int) {
        if (index in tracks.indices) {
            currentIndex = index
            loadCurrent(true)
        }
    }

    private fun loadCurrent(playWhenReady: Boolean) {
        val track = currentTrack ?: return
        val mediaItem = MediaItem.fromUri(track.uri)
        exoPlayer?.apply {
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = playWhenReady
        }
        notifyTrackChanged(track)
    }

    fun play() {
        exoPlayer?.playWhenReady = true
    }

    fun pause() {
        exoPlayer?.playWhenReady = false
    }

    fun toggle() {
        exoPlayer?.let { it.playWhenReady = !it.playWhenReady }
    }

    fun next() {
        if (tracks.isEmpty()) return
        currentIndex = (currentIndex + 1) % tracks.size
        loadCurrent(true)
    }

    fun previous() {
        if (tracks.isEmpty()) return
        currentIndex = if (currentIndex <= 0) tracks.size - 1 else currentIndex - 1
        loadCurrent(true)
    }

    fun seekTo(positionMs: Long) {
        exoPlayer?.seekTo(positionMs)
        notifyPosition()
    }

    fun positionMs(): Long = exoPlayer?.currentPosition ?: 0L
    fun durationMs(): Long = exoPlayer?.duration?.coerceAtLeast(0L) ?: 0L

    private fun notifyTrackChanged(track: LocalTrack?) {
        listeners.forEach { it.onTrackChanged(track) }
    }

    private fun notifyPlaybackStateChanged(isPlaying: Boolean) {
        listeners.forEach { it.onPlaybackStateChanged(isPlaying) }
    }

    private fun notifyPosition() {
        val pos = positionMs()
        val dur = durationMs()
        listeners.forEach { it.onPositionChanged(pos, dur) }
    }

    override fun onDestroy() {
        notificationManager?.setPlayer(null)
        sessionConnector?.setPlayer(null)
        mediaSession?.release()
        exoPlayer?.release()
        exoPlayer = null
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.local_music_notification_channel),
                NotificationManager.IMPORTANCE_LOW
            )
            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "tomoko_local_music"
        const val NOTIFICATION_ID = 912345
        const val ACTION_START = "org.coffe.tomoko.localmusic.START"

        fun start(context: Context) {
            val intent = Intent(context, LocalMusicService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }
}