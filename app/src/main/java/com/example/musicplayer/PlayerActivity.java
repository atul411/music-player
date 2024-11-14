package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

   Button btnPlay, btnNext, btnPrevious, btnFastForward, btnFastBackward;
   TextView songNameText, songStart, songEnd;
   SeekBar seekBar;
   ImageView imageView;
   String songName;
   public static final String EXTRA_SONG_NAME = "song_name";
   MediaPlayer mediaPlayer;
   int position;
   ArrayList<File> mySongs;
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_player);
      btnPlay = findViewById(R.id.btnPlay);
      btnNext = findViewById(R.id.btnNext);
      btnPrevious = findViewById(R.id.btnPrevious);
      btnFastBackward = findViewById(R.id.btnFastBackward);
      btnFastForward = findViewById(R.id.btnFastForward);
      songNameText = findViewById(R.id.txtSong);
      seekBar = findViewById(R.id.seekBar);
      imageView = findViewById(R.id.imgView);

      if (mediaPlayer != null){
         mediaPlayer.start();
         mediaPlayer.release();
      }
      Bundle bundle = getIntent().getExtras();
      mySongs = (ArrayList) bundle.getParcelableArrayList("song");
      songName = bundle.getString("songName");
      position = bundle.getInt("position");
      songNameText.setSelected(true);
      Uri uri = Uri.parse(mySongs.get(position).toString());
      songName = mySongs.get(position).getName();
      songNameText.setText(songName);
      mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
      mediaPlayer.start();
      btnPlay.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            if (mediaPlayer.isPlaying())
            {
               btnPlay.setBackgroundResource(R.drawable.ic_play);
               mediaPlayer.pause();
            }else{
               btnPlay.setBackgroundResource(R.drawable.ic_pause);
               mediaPlayer.start();
            }
         }
      });
      btnNext.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            position = (position+1)%mySongs.size();
            Uri uri = Uri.parse(mySongs.get(position).toString());
            songName = mySongs.get(position).getName();
            songNameText.setText(songName);
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
         }
      });
      btnPrevious.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = (position-1<0)?mySongs.size()-1:(position-1)%mySongs.size();
            Uri uri = Uri.parse(mySongs.get(position).toString());
            songName = mySongs.get(position).getName();
            songNameText.setText(songName);
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
         }
      });
      mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
         @Override
         public void onCompletion(MediaPlayer mp) {
            mediaPlayer.stop();
            mediaPlayer.release();
         }
      });
   }

   @Override
   protected void onPause() {
      super.onPause();
      mediaPlayer.pause();
   }

   @Override
   protected void onDestroy() {
      super.onDestroy();
      mediaPlayer.stop();
      mediaPlayer.release();
   }

   @Override
   protected void onResume() {
      super.onResume();
      mediaPlayer.start();
   }
}