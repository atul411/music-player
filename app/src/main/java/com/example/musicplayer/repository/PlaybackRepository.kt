package com.example.musicplayer.repository

import kotlinx.coroutines.flow.StateFlow
import java.io.File

interface PlaybackRepository {
    val isPlaying: StateFlow<Boolean>
    val currentSongName: StateFlow<String>
    val progress: StateFlow<Int>
    val duration: StateFlow<Int>

    fun play(songs: List<File>, position: Int)
    fun togglePlayPause()
    fun next()
    fun previous()
    fun fastForward()
    fun fastBackward()
    fun seekTo(ms: Int)
    fun pauseForLifecycle()
    fun resumeForLifecycle()
    fun release()
}
