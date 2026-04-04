package com.example.musicplayer.repository

import java.io.File

interface SongRepository {
    suspend fun getSongs(): List<File>
}
