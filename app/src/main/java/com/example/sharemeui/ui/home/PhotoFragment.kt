package com.example.sharemeui.ui.home
import android.database.Cursor
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


class PhotoFragment : Fragment() {
    val TAG = "photoFrag"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        val photoFragment = inflater.inflate(R.layout.fragment_photo, container, false)
        val SUBTAG = "OnCreate"
        val cr = requireActivity().contentResolver
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val sortOrder = MediaStore.Images.Media.DATE_TAKEN + " DESC"
        val cur: Cursor? = cr.query(uri, null, null, null, sortOrder)
        var newPhoto = mutableListOf<photo>()
        var count = 0
//        val photoAdapter = photoAdapter()
//        val recyclerView = photoFragment.findViewById<RecyclerView>(R.id.photoRCV)
//        recyclerView.layoutManager = LinearLayoutManager(context)
//        recyclerView.adapter = photoAdapter

        Log.d("$TAG-$SUBTAG", "Image found :- " + cur?.getCount().toString())
        if (cur != null) {
            count = cur.getCount()
            if (count > 0) {
                while (cur.moveToNext()) {
                    val data: String = cur.getString(cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                    val title: String = cur.getString(cur.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))

                    Log.d("$TAG-$SUBTAG",
                        "Image found :- $data  $title"
                    )
                    // Save to your list here
                    newPhoto.add(photo(title, data))

//                  TODO() :- SIDDHARTH
//                  prepare list in group of 4 to pass RCV for parallel and fast loading of preview image

                }
            }
            val close: Any = cur.close()
        }
        Log.d("$TAG-$SUBTAG", newPhoto.toString())
//        photoAdapter.setPhoto(newPhoto)
        return photoFragment
    }

}