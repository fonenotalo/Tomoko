package org.coffe.tomoko.localmusic

import android.net.Uri

data class LocalTrack(
    val id: Long,
    val uri: Uri,
    val title: String,
    val artist: String,
    val album: String,
    val durationMs: Long,
    val albumArtUri: Uri? = null
)