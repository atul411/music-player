package com.example.musicplayer.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musicplayer.repository.PlaybackRepository
import com.example.musicplayer.repository.SongRepository
import com.example.musicplayer.ui.player.PlayerViewModel
import com.example.musicplayer.ui.songlist.SongListViewModel

class SongListViewModelFactory(
    private val repository: SongRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(SongListViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
        @Suppress("UNCHECKED_CAST")
        return SongListViewModel(repository) as T
    }
}

class PlayerViewModelFactory(
    private val repository: PlaybackRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
        @Suppress("UNCHECKED_CAST")
        return PlayerViewModel(repository) as T
    }
}
