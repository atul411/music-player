package com.example.musicplayer

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class PlayerActivity : AppCompatActivity() {

    private lateinit var btnPlay: Button
    private lateinit var btnNext: Button
    private lateinit var btnPrevious: Button
    private lateinit var btnFastForward: Button
    private lateinit var btnFastBackward: Button
    private lateinit var songNameText: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var imageView: ImageView
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var mySongs: MutableList<File>
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        btnPlay = findViewById(R.id.btnPlay)
        btnNext = findViewById(R.id.btnNext)
        btnPrevious = findViewById(R.id.btnPrevious)
        btnFastBackward = findViewById(R.id.btnFastBackward)
        btnFastForward = findViewById(R.id.btnFastForward)
        songNameText = findViewById(R.id.txtSong)
        seekBar = findViewById(R.id.seekBar)
        imageView = findViewById(R.id.imgView)

        mySongs = (intent.getSerializableExtra("song") as? ArrayList<File>)?.toMutableList() ?: mutableListOf()
        position = intent.getIntExtra("position", 0)

        songNameText.isSelected = true
        playSong(position)

        btnPlay.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                btnPlay.setBackgroundResource(R.drawable.ic_play)
                mediaPlayer.pause()
            } else {
                btnPlay.setBackgroundResource(R.drawable.ic_pause)
                mediaPlayer.start()
            }
        }

        btnNext.setOnClickListener {
            position = (position + 1) % mySongs.size
            playSong(position)
        }

        btnPrevious.setOnClickListener {
            position = if (position - 1 < 0) mySongs.size - 1 else (position - 1) % mySongs.size
            playSong(position)
        }
    }

    private fun playSong(pos: Int) {
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
        val uri = Uri.parse(mySongs[pos].toString())
        songNameText.text = mySongs[pos].name
        mediaPlayer = MediaPlayer.create(applicationContext, uri)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener { mp ->
            mp.stop()
            mp.release()
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.release()
    }
}
