package com.example.musicplayer.repository

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class PlaybackRepositoryImpl(private val context: Context) : PlaybackRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _isPlaying = MutableStateFlow(false)
    override val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _currentSongName = MutableStateFlow("")
    override val currentSongName: StateFlow<String> = _currentSongName

    private val _progress = MutableStateFlow(0)
    override val progress: StateFlow<Int> = _progress

    private val _duration = MutableStateFlow(0)
    override val duration: StateFlow<Int> = _duration

    private var mediaPlayer: MediaPlayer? = null
    private var songs: List<File> = emptyList()
    private var currentPosition: Int = 0
    private var userPaused = false
    private var progressJob: Job? = null

    override fun play(songs: List<File>, position: Int) {
        if (songs.isEmpty()) return
        this.songs = songs
        this.currentPosition = position
        userPaused = false
        loadAndPlay()
    }

    override fun togglePlayPause() {
        val player = mediaPlayer ?: return
        if (player.isPlaying) {
            player.pause()
            userPaused = true
            _isPlaying.value = false
            progressJob?.cancel()
        } else {
            player.start()
            userPaused = false
            _isPlaying.value = true
            startProgressTracking()
        }
    }

    override fun next() {
        if (songs.isEmpty()) return
        currentPosition = (currentPosition + 1) % songs.size
        userPaused = false
        loadAndPlay()
    }

    override fun previous() {
        if (songs.isEmpty()) return
        currentPosition = if (currentPosition - 1 < 0) songs.size - 1 else currentPosition - 1
        userPaused = false
        loadAndPlay()
    }

    override fun fastForward() {
        val player = mediaPlayer ?: return
        player.seekTo((player.currentPosition + 10_000).coerceAtMost(player.duration))
    }

    override fun fastBackward() {
        val player = mediaPlayer ?: return
        player.seekTo((player.currentPosition - 10_000).coerceAtLeast(0))
    }

    override fun seekTo(ms: Int) {
        mediaPlayer?.seekTo(ms)
    }

    override fun pauseForLifecycle() {
        if (!userPaused) {
            mediaPlayer?.pause()
            _isPlaying.value = false
            progressJob?.cancel()
        }
    }

    override fun resumeForLifecycle() {
        if (!userPaused && mediaPlayer != null) {
            mediaPlayer?.start()
            _isPlaying.value = true
            startProgressTracking()
        }
    }

    override fun release() {
        progressJob?.cancel()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun loadAndPlay() {
        progressJob?.cancel()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        val file = songs[currentPosition]
        _currentSongName.value = file.name.replace(".mp3", "").replace(".wav", "")
        mediaPlayer = MediaPlayer.create(context, Uri.parse(file.toString()))
        mediaPlayer?.start()
        _isPlaying.value = true
        _duration.value = mediaPlayer?.duration ?: 0
        startProgressTracking()
        mediaPlayer?.setOnCompletionListener {
            _isPlaying.value = false
            progressJob?.cancel()
        }
    }

    private fun startProgressTracking() {
        progressJob?.cancel()
        progressJob = scope.launch {
            while (true) {
                _progress.value = mediaPlayer?.currentPosition ?: 0
                delay(500)
            }
        }
    }
}
