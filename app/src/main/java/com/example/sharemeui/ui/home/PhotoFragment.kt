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
        val photoAdapter = photoAdapter()
        val recyclerView = photoFragment.findViewById<RecyclerView>(R.id.photoRCV)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = photoAdapter

        Log.d("$TAG-$SUBTAG", "Image found :- " + cur?.getCount().toString())
        if (cur != null) {
            count = cur.getCount()
            if (count > 0) {
                var photo4List = mutableListOf<String>()
                while (cur.moveToNext()) {
                    val data: String = cur.getString(cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                    photo4List.add(data)
                    Log.d("$TAG-$SUBTAG","Image found :- $data")
                    if (photo4List.size >= 4) {
                        // Save to your list here
                        newPhoto.add(photo(photo4List[0], photo4List[1], photo4List[2], photo4List[3]))
                        photo4List.removeAt(0)
                        photo4List.removeAt(0)
                        photo4List.removeAt(0)
                        photo4List.removeAt(0)
                    }
                }
                if (photo4List.size > 0) {
                    if (photo4List.size == 1)
                        newPhoto.add(photo(photo4List[0], "None", "None", "None"))
                    if (photo4List.size == 2)
                        newPhoto.add(photo(photo4List[0],photo4List[1], "None", "None"))
                    if (photo4List.size == 3)
                        newPhoto.add(photo(photo4List[0],photo4List[1], photo4List[2], "None"))
                    if (photo4List.size == 4)
                        newPhoto.add(photo(photo4List[0],photo4List[1], photo4List[2], photo4List[3]))
                }
//                TODO() -> SIDDHARTH
//                make proper login to add in grid
            }
            val close: Any = cur.close()
        }
        Log.d("$TAG-$SUBTAG", newPhoto.toString())
        photoAdapter.setPhoto(newPhoto)
        return photoFragment
    }

}