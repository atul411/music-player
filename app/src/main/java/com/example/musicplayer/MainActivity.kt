package com.example.musicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.musicplayer.di.PlayerViewModelFactory
import com.example.musicplayer.di.SongListViewModelFactory
import com.example.musicplayer.navigation.Player
import com.example.musicplayer.navigation.SongList
import com.example.musicplayer.ui.player.PlayerIntent
import com.example.musicplayer.ui.player.PlayerScreen
import com.example.musicplayer.ui.player.PlayerViewModel
import com.example.musicplayer.ui.songlist.SongListScreen
import com.example.musicplayer.ui.songlist.SongListViewModel
import com.example.musicplayer.ui.theme.MusicPlayerTheme

class MainActivity : ComponentActivity() {

    private val app by lazy { application as MusicPlayerApp }

    private val songListViewModel: SongListViewModel by viewModels {
        SongListViewModelFactory(app.songRepository)
    }
    private val playerViewModel: PlayerViewModel by viewModels {
        PlayerViewModelFactory(app.playbackRepository)
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
            PlayerScreen(
                viewModel = playerViewModel,
                lifecycleOwner = LocalLifecycleOwner.current
            )
        }
    }
}
