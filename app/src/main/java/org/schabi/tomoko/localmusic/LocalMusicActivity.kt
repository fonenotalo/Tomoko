package org.coffe.tomoko.localmusic

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.text.format.DateUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.coffe.tomoko.R

class LocalMusicActivity : AppCompatActivity(), LocalMusicService.Listener {

    private lateinit var trackList: RecyclerView
    private lateinit var emptyView: TextView
    private lateinit var searchBox: EditText
    private lateinit var adapter: TrackAdapter

    private lateinit var trackTitle: TextView
    private lateinit var trackArtist: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var currentTime: TextView
    private lateinit var totalTime: TextView
    private lateinit var playPauseButton: Button
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button

    private var service: LocalMusicService? = null
    private var bound = false
    private var allTracks: List<LocalTrack> = emptyList()

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val s = (binder as LocalMusicService.LocalBinder).getService()
            service = s
            bound = true
            s.addListener(this@LocalMusicActivity)
            if (s.currentTrack == null && allTracks.isNotEmpty()) {
                s.setPlaylist(allTracks, 0)
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            service?.removeListener(this@LocalMusicActivity)
            service = null
            bound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_music)
        title = getString(R.string.local_music)

        trackList = findViewById(R.id.track_list)
        emptyView = findViewById(R.id.empty_view)
        searchBox = findViewById(R.id.search_box)

        trackTitle = findViewById(R.id.track_title)
        trackArtist = findViewById(R.id.track_artist)
        seekBar = findViewById(R.id.seek_bar)
        currentTime = findViewById(R.id.current_time)
        totalTime = findViewById(R.id.total_time)
        playPauseButton = findViewById(R.id.play_pause_button)
        prevButton = findViewById(R.id.prev_button)
        nextButton = findViewById(R.id.next_button)

        adapter = TrackAdapter { index ->
            service?.setPlaylist(filteredTracks(), index)
            service?.playTrackAt(index)
        }
        trackList.layoutManager = LinearLayoutManager(this)
        trackList.adapter = adapter

        playPauseButton.setOnClickListener { service?.toggle() }
        prevButton.setOnClickListener { service?.previous() }
        nextButton.setOnClickListener { service?.next() }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) service?.seekTo(progress.toLong())
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        searchBox.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                applyFilter(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })

        requestPermissionAndLoad()
    }

    private fun requestPermissionAndLoad() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            loadTracks()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 1001)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadTracks()
        } else {
            emptyView.text = getString(R.string.local_music_permission)
            emptyView.visibility = View.VISIBLE
        }
    }

    private fun loadTracks() {
        allTracks = LocalMusicRepository.loadTracks(this)
        applyFilter(searchBox.text.toString())
        updateEmpty()
        LocalMusicService.start(this)
        val intent = Intent(this, LocalMusicService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    private fun filteredTracks(): List<LocalTrack> =
        LocalMusicRepository.search(allTracks, searchBox.text.toString())

    private fun applyFilter(query: String) {
        val filtered = LocalMusicRepository.search(allTracks, query)
        adapter.submit(filtered)
        updateEmpty()
    }

    private fun updateEmpty() {
        if (allTracks.isEmpty()) {
            emptyView.visibility = View.VISIBLE
            trackList.visibility = View.GONE
        } else {
            emptyView.visibility = View.GONE
            trackList.visibility = View.VISIBLE
        }
    }

    override fun onTrackChanged(track: LocalTrack?) {
        runOnUiThread {
            trackTitle.text = track?.title ?: ""
            trackArtist.text = track?.artist ?: ""
        }
    }

    override fun onPlaybackStateChanged(isPlaying: Boolean) {
        runOnUiThread {
            playPauseButton.text = if (isPlaying) "Pause" else "Play"
        }
    }

    override fun onPositionChanged(positionMs: Long, durationMs: Long) {
        runOnUiThread {
            seekBar.max = durationMs.toInt().coerceAtLeast(0)
            seekBar.progress = positionMs.toInt().coerceAtLeast(0)
            currentTime.text = formatTime(positionMs)
            totalTime.text = formatTime(durationMs)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (bound) {
            service?.removeListener(this)
            unbindService(connection)
            bound = false
        }
    }

    private fun formatTime(ms: Long): String {
        val totalSeconds = (ms / 1000).toInt()
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%d:%02d", minutes, seconds)
    }

    inner class TrackAdapter(private val onClick: (Int) -> Unit) :
        RecyclerView.Adapter<TrackAdapter.VH>() {

        private val items = mutableListOf<LocalTrack>()

        fun submit(list: List<LocalTrack>) {
            items.clear()
            items.addAll(list)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): VH {
            val view = layoutInflater.inflate(R.layout.item_local_track, parent, false)
            return VH(view)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val track = items[position]
            holder.title.text = track.title
            holder.artist.text = track.artist
            holder.album.text = track.album
            holder.duration.text = formatTime(track.durationMs)
            if (track.albumArtUri != null) {
                try {
                    holder.albumArt.setImageURI(track.albumArtUri)
                } catch (e: Exception) {
                    holder.albumArt.setImageResource(android.R.drawable.ic_media_play)
                }
            } else {
                holder.albumArt.setImageResource(android.R.drawable.ic_media_play)
            }
            holder.itemView.setOnClickListener {
                val idx = items.indexOf(track)
                onClick(idx)
                // map to allTracks index? service uses filtered list? We pass filtered list setPlaylist
                service?.setPlaylist(items, idx)
                service?.playTrackAt(idx)
            }
        }

        override fun getItemCount(): Int = items.size

        inner class VH(view: View) : RecyclerView.ViewHolder(view) {
            val albumArt: ImageView = view.findViewById(R.id.album_art)
            val title: TextView = view.findViewById(R.id.title)
            val artist: TextView = view.findViewById(R.id.artist)
            val album: TextView = view.findViewById(R.id.album)
            val duration: TextView = view.findViewById(R.id.duration)
        }
    }
}