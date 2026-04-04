package com.example.musicplayer.ui.songlist

import java.io.File

data class SongListState(
    val songs: List<File> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val permissionGranted: Boolean? = null
)
