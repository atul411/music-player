package com.example.musicplayer.ui.player

data class PlayerState(
    val currentSongName: String = "",
    val isPlaying: Boolean = false,
    val progress: Int = 0,
    val duration: Int = 0
)
