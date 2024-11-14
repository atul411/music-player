package com.example.musicplayer;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

   ListView listView;
   String[] items;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      listView = (ListView) findViewById(R.id.listView);
      runTimePermission();
   }

   public void runTimePermission() {
      Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
         @Override
         public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
            displaySong();
         }

         @Override
         public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

         }

         @Override
         public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
            permissionToken.continuePermissionRequest();
         }
      }).check();
   }

   public ArrayList<File> findSong(File file) {
      ArrayList<File> arrayList = new ArrayList<>();
      File[] files = file.listFiles();
      if (files == null) {
         return arrayList;
      }
      for (File singleFile : files) {
         if (singleFile.isDirectory() && !singleFile.isHidden()) {
            arrayList.addAll(findSong(singleFile));
         } else if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")) {
            arrayList.add(singleFile);
         }
      }
      return arrayList;
   }

   public void displaySong() {
      final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());
      items = new String[mySongs.size()];
      for (int i = 0; i < mySongs.size(); i++) {
         items[i] = mySongs.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
      }
      customAdapter customAdapter = new customAdapter();
      listView.setAdapter(customAdapter);
      listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String songName = (String) listView.getItemAtPosition(position);
            startActivity(new Intent(getApplicationContext(), PlayerActivity.class)
                    .putExtra("song", mySongs)
                    .putExtra("songName", songName)
                    .putExtra("position", position));
         }
      });
   }

   class customAdapter extends BaseAdapter {

      @Override
      public int getCount() {
         return items.length;
      }

      @Override
      public Object getItem(int position) {
         return null;
      }

      @Override
      public long getItemId(int position) {
         return 0;
      }

      @Override
      public View getView(int position, View convertView, ViewGroup parent) {
         View view = getLayoutInflater().inflate(R.layout.list_items, null);
         TextView textSong = view.findViewById(R.id.textSong);
         textSong.setSelected(true);
         textSong.setText(items[position]);
         return view;
      }
   }
}