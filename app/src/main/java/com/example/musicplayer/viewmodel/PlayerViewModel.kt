package com.example.musicplayer.viewmodel

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

class PlayerViewModel : ViewModel() {

    private var mediaPlayer: MediaPlayer? = null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _currentSongName = MutableStateFlow("")
    val currentSongName: StateFlow<String> = _currentSongName

    private var songs: List<File> = emptyList()
    private var currentPosition: Int = 0

    fun playSong(context: Context, songs: List<File>, position: Int) {
        this.songs = songs
        this.currentPosition = position
        loadAndPlay(context)
    }

    fun togglePlayPause() {
        val player = mediaPlayer ?: return
        if (player.isPlaying) {
            player.pause()
            _isPlaying.value = false
        } else {
            player.start()
            _isPlaying.value = true
        }
    }

    fun next(context: Context) {
        currentPosition = (currentPosition + 1) % songs.size
        loadAndPlay(context)
    }

    fun previous(context: Context) {
        currentPosition = if (currentPosition - 1 < 0) songs.size - 1 else currentPosition - 1
        loadAndPlay(context)
    }

    private fun loadAndPlay(context: Context) {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        val file = songs[currentPosition]
        _currentSongName.value = file.name.replace(".mp3", "").replace(".wav", "")
        mediaPlayer = MediaPlayer.create(context, Uri.parse(file.toString()))
        mediaPlayer?.start()
        _isPlaying.value = true
        mediaPlayer?.setOnCompletionListener {
            _isPlaying.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
