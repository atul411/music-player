package com.example.musicplayer.ui.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.repository.PlaybackRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class PlayerViewModel(private val repository: PlaybackRepository) : ViewModel() {

    val state: StateFlow<PlayerState> = combine(
        repository.isPlaying,
        repository.currentSongName,
        repository.progress,
        repository.duration
    ) { isPlaying, name, progress, duration ->
        PlayerState(
            currentSongName = name,
            isPlaying = isPlaying,
            progress = progress,
            duration = duration
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, PlayerState())

    fun processIntent(intent: PlayerIntent) {
        when (intent) {
            is PlayerIntent.Play -> repository.play(intent.songs, intent.position)
            is PlayerIntent.TogglePlayPause -> repository.togglePlayPause()
            is PlayerIntent.Next -> repository.next()
            is PlayerIntent.Previous -> repository.previous()
            is PlayerIntent.FastForward -> repository.fastForward()
            is PlayerIntent.FastBackward -> repository.fastBackward()
            is PlayerIntent.SeekTo -> repository.seekTo(intent.ms)
            is PlayerIntent.LifecyclePause -> repository.pauseForLifecycle()
            is PlayerIntent.LifecycleResume -> repository.resumeForLifecycle()
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.release()
    }
}
