package com.example.musicplayer.viewmodel

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.repository.SongRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel : ViewModel() {

    private val repository = SongRepository()

    private val _songs = MutableStateFlow<List<File>>(emptyList())
    val songs: StateFlow<List<File>> = _songs

    fun loadSongs(contentResolver: ContentResolver) {
        viewModelScope.launch {
            _songs.value = repository.getSongs(contentResolver)
        }
    }
}
