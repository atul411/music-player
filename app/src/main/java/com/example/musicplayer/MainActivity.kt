package com.example.musicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.musicplayer.di.PlayerViewModelFactory
import com.example.musicplayer.di.SongListViewModelFactory
import com.example.musicplayer.repository.PlaybackRepositoryImpl
import com.example.musicplayer.repository.SongRepositoryImpl
import com.example.musicplayer.ui.player.PlayerIntent
import com.example.musicplayer.ui.player.PlayerScreen
import com.example.musicplayer.ui.player.PlayerViewModel
import com.example.musicplayer.ui.songlist.SongListScreen
import com.example.musicplayer.ui.songlist.SongListViewModel
import com.example.musicplayer.navigation.Player
import com.example.musicplayer.navigation.SongList
import com.example.musicplayer.ui.theme.MusicPlayerTheme

class MainActivity : ComponentActivity() {

    private val songRepository by lazy { SongRepositoryImpl(contentResolver) }
    private val playbackRepository by lazy { PlaybackRepositoryImpl(applicationContext) }

    private val songListViewModel: SongListViewModel by viewModels {
        SongListViewModelFactory(songRepository)
    }
    private val playerViewModel: PlayerViewModel by viewModels {
        PlayerViewModelFactory(playbackRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicPlayerTheme {
                MusicPlayerApp(songListViewModel, playerViewModel)
            }
        }
    }
}

@Composable
private fun MusicPlayerApp(
    songListViewModel: SongListViewModel,
    playerViewModel: PlayerViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = SongList) {
        composable<SongList> {
            SongListScreen(
                viewModel = songListViewModel,
                onSongClick = { songs, position ->
                    playerViewModel.processIntent(PlayerIntent.Play(songs, position))
                    navController.navigate(Player)
                }
            )
        }
        composable<Player> {
            PlayerScreen(viewModel = playerViewModel)
        }
    }
}
