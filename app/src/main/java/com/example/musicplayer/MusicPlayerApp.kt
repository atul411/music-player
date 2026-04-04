package com.example.musicplayer

import android.app.Application
import com.example.musicplayer.repository.PlaybackRepository
import com.example.musicplayer.repository.PlaybackRepositoryImpl
import com.example.musicplayer.repository.SongRepository
import com.example.musicplayer.repository.SongRepositoryImpl

class MusicPlayerApp : Application() {

    val songRepository: SongRepository by lazy {
        SongRepositoryImpl(contentResolver)
    }

    val playbackRepository: PlaybackRepository by lazy {
        PlaybackRepositoryImpl(this)
    }
}
