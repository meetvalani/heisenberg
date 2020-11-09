package com.example.sharemeui.ui.home

import android.content.ContentUris
import android.database.Cursor
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sharemeui.R
import kotlinx.android.synthetic.main.fragment_temp_music.*


class MusicFragment : Fragment() {
    val TAG = "MusicFrag"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val musicFragment = inflater.inflate(R.layout.fragment_temp_music, container, false)
        val SUBTAG = "OnCreate"
        val cr = requireActivity().contentResolver
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
        val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"
        val cur: Cursor? = cr.query(uri, null, selection, null, sortOrder)
        var newMusic = mutableListOf<music>()
        var count = 0
        val musicAdapter = musicAdapter()
        val recyclerView = musicFragment.findViewById<RecyclerView>(R.id.musicRCV)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = musicAdapter

        Log.d("$TAG-$SUBTAG", "song found :- " + cur?.getCount().toString())
        if (cur != null) {
            count = cur.getCount()
            if (count > 0) {
                while (cur.moveToNext()) {
                    val data: String = cur.getString(cur.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                    val size: String = cur.getString(cur.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))
                    val title: String = cur.getString(cur.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
                    val artist: String = cur.getString(cur.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                    val album: Uri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        cur.getLong(cur.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                    )
                    val displayImage = BitmapFactory.decodeFile(album.toString())
                    Log.d("$TAG-$SUBTAG",
                        "song found :- $data $size $title $artist $displayImage"
                    )
                    // Save to your list here
                    newMusic.add(music(title, size , data))
                }
            }
            val close: Any = cur.close()
        }
        Log.d("$TAG-$SUBTAG", newMusic.toString())
        musicAdapter.setMusic(newMusic)
        return musicFragment
    }

}