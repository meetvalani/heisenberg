package com.example.sharemeui.ui.home

import android.content.ContentUris
import android.database.Cursor
import android.graphics.BitmapFactory
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
import com.example.sharemeui.R

class VideoFragment : Fragment() {

    val TAG = "VideoFrag"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val videoFragment = inflater.inflate(R.layout.fragment_video, container, false)
        val SUBTAG = "OnCreate"
        val cr = requireActivity().contentResolver
        val uri: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val sortOrder = MediaStore.Video.Media.TITLE + " ASC"
        val cur: Cursor? = cr.query(uri, null, null, null, sortOrder)
        var newVideo = mutableListOf<video>()
        var count = 0
        val videoAdapter = videoAdapter()
        val recyclerView = videoFragment.findViewById<RecyclerView>(R.id.videoRCV)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = videoAdapter

        Log.d("$TAG-$SUBTAG", "videos found :- " + cur?.getCount().toString())
        if (cur != null) {
            count = cur.getCount()
            if (count > 0) {
                while (cur.moveToNext()) {
                    val data: String = cur.getString(cur.getColumnIndexOrThrow(MediaStore.Video.Media._ID))
                    val size: String = cur.getString(cur.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE))
                    val title: String = cur.getString(cur.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME))
                    val artist: String = cur.getString(cur.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST))
                    val album: Uri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        cur.getLong(cur.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                    )
                    val displayImage = BitmapFactory.decodeFile(album.toString())
                    Log.d("$TAG-$SUBTAG",
                        "video found :- $data $size $title $artist $displayImage"
                    )
                    // Save to your list here
                    newVideo.add(video(title, size , album.toString()))
                }
            }
            val close: Any = cur.close()
        }
        Log.d("$TAG-$SUBTAG", newVideo.toString())
        videoAdapter.setVideo(newVideo)
        return videoFragment
    }

}