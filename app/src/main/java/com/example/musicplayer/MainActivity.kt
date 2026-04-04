package com.example.musicplayer

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private var items: Array<String> = emptyArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listView = findViewById(R.id.listView)
        runTimePermission()
    }

    private fun runTimePermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_AUDIO
        else
            Manifest.permission.READ_EXTERNAL_STORAGE

        Dexter.withContext(this)
            .withPermission(permission)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    displaySong()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                }

                override fun onPermissionRationaleShouldBeShown(
                    request: PermissionRequest,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    private fun displaySong() {
        lifecycleScope.launch {
            val mySongs = withContext(Dispatchers.IO) {
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
            items = Array(mySongs.size) { i ->
                mySongs[i].name.replace(".mp3", "").replace(".wav", "")
            }
            listView.adapter = CustomAdapter()
            listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val songName = listView.getItemAtPosition(position) as String
                startActivity(
                    Intent(applicationContext, PlayerActivity::class.java)
                        .putExtra("song", ArrayList(mySongs))
                        .putExtra("songName", songName)
                        .putExtra("position", position)
                )
            }
        }
    }

    inner class CustomAdapter : BaseAdapter() {

        override fun getCount(): Int = items.size

        override fun getItem(position: Int): String = items[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = layoutInflater.inflate(R.layout.list_items, null)
            val textSong = view.findViewById<TextView>(R.id.textSong)
            textSong.isSelected = true
            textSong.text = items[position]
            return view
        }
    }
}
