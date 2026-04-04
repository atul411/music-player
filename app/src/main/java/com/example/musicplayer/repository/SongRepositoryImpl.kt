package com.example.musicplayer.repository

import android.content.ContentResolver
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class SongRepositoryImpl(private val contentResolver: ContentResolver) : SongRepository {

    override suspend fun getSongs(): List<File> = withContext(Dispatchers.IO) {
        val result = mutableListOf<File>()
        val projection = arrayOf(MediaStore.Audio.Media.DATA)
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection, selection, null, null
        )
        cursor?.use {
            val dataIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            while (it.moveToNext()) {
                result.add(File(it.getString(dataIndex)))
            }
        }
        result
    }
}
