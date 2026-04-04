package com.example.musicplayer

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.musicplayer.viewmodel.PlayerViewModel
import kotlinx.coroutines.launch
import java.io.File

class PlayerActivity : AppCompatActivity() {

    private val viewModel: PlayerViewModel by viewModels()

    private lateinit var btnPlay: Button
    private lateinit var btnNext: Button
    private lateinit var btnPrevious: Button
    private lateinit var btnFastForward: Button
    private lateinit var btnFastBackward: Button
    private lateinit var songNameText: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        btnPlay = findViewById(R.id.btnPlay)
        btnNext = findViewById(R.id.btnNext)
        btnPrevious = findViewById(R.id.btnPrevious)
        btnFastForward = findViewById(R.id.btnFastForward)
        btnFastBackward = findViewById(R.id.btnFastBackward)
        songNameText = findViewById(R.id.txtSong)
        seekBar = findViewById(R.id.seekBar)
        imageView = findViewById(R.id.imgView)

        val songs = (intent.getSerializableExtra("song") as? ArrayList<File>) ?: return
        val position = intent.getIntExtra("position", 0)

        songNameText.isSelected = true
        viewModel.playSong(applicationContext, songs, position)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.currentSongName.collect { name ->
                        songNameText.text = name
                    }
                }
                launch {
                    viewModel.isPlaying.collect { playing ->
                        btnPlay.setBackgroundResource(
                            if (playing) R.drawable.ic_pause else R.drawable.ic_play
                        )
                    }
                }
            }
        }

        btnPlay.setOnClickListener { viewModel.togglePlayPause() }
        btnNext.setOnClickListener { viewModel.next(applicationContext) }
        btnPrevious.setOnClickListener { viewModel.previous(applicationContext) }
    }
}
