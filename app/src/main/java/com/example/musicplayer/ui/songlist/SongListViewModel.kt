package com.example.musicplayer.ui.songlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.repository.SongRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SongListViewModel(private val repository: SongRepository) : ViewModel() {

    private val _state = MutableStateFlow(SongListState())
    val state: StateFlow<SongListState> = _state

    fun processIntent(intent: SongListIntent) {
        when (intent) {
            is SongListIntent.LoadSongs -> loadSongs()
            is SongListIntent.SongClicked -> Unit
            is SongListIntent.PermissionResult -> {
                _state.value = _state.value.copy(permissionGranted = intent.granted)
                if (intent.granted) loadSongs()
            }
        }
    }

    private fun loadSongs() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val songs = repository.getSongs()
                _state.value = _state.value.copy(songs = songs, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = e.message)
            }
        }
    }
}
