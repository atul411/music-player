package com.example.musicplayer.ui.songlist

sealed class SongListIntent {
    object LoadSongs : SongListIntent()
    data class SongClicked(val index: Int) : SongListIntent()
}
