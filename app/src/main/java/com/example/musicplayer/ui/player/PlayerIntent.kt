package com.example.musicplayer.ui.player

import java.io.File

sealed class PlayerIntent {
    data class Play(val songs: List<File>, val position: Int) : PlayerIntent()
    object TogglePlayPause : PlayerIntent()
    object Next : PlayerIntent()
    object Previous : PlayerIntent()
    object FastForward : PlayerIntent()
    object FastBackward : PlayerIntent()
    data class SeekTo(val ms: Int) : PlayerIntent()
    object LifecyclePause : PlayerIntent()
    object LifecycleResume : PlayerIntent()
}
