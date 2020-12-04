package com.example.sharemeui.ui.home
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sharemeui.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_photo.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch


class photoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var photoList = mutableListOf<photo>()
    val TAG = "photoAdapter"
    var height = 0
    var width = 0
    var imagePerRow = 4
    fun setPhoto(photos: MutableList<photo>) {
        this.photoList = photos
        notifyDataSetChanged()
    }

    fun updatePhotoList(photo: List<photo> ) {
        val oldSize = this.photoList.size
        this.photoList.addAll(photo)
        val newSize = this.photoList.size
        notifyItemRangeInserted(oldSize, newSize)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return photoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_photo, parent, false
            )
        )
    }
    override fun getItemCount(): Int {
        return photoList.size
    }
    override fun getItemViewType(position: Int): Int {
        return 1
    }
    override fun onBindViewHolder(holder:  RecyclerView.ViewHolder, position: Int) {
        if (holder is photoViewHolder){
            holder.bind(photoList[position])
        }
    }
    inner class photoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(photo: photo) {
            var columnWidth = (width / imagePerRow).toInt()
            var columnHeight = (columnWidth * 4 / 3).toInt()
            val util = Util(itemView.context)


            CoroutineScope(Main).launch {
                val imageView = itemView.findViewById<ImageView>(R.id.photo)
                var imageLayouts = imageView.layoutParams
                imageLayouts.width = columnWidth
                imageLayouts.height = columnHeight
                Glide.with(itemView).load(photo.coverImage).into(itemView.photo)
                imageView.setOnClickListener {
                    val data = photo.coverImage
                    if (imageView.background !== null) {
                        imageView.background = null
                        CoroutineScope(IO).launch { util.removeFromTransferQueue(data) }
                    } else {
                        imageView.setBackgroundColor(Color.rgb(47, 127, 45))
                        CoroutineScope(IO).launch {
                            util.insertIntoTransferQueue(
                                data.split("/")[data.split(
                                    "/"
                                ).size - 1], "ASK SID MB", data, "INQUEUE", "IMAGE"
                            )
                        }
                    }
                }

            }

            CoroutineScope(Main).launch {
                val imageView1 = itemView.findViewById<ImageView>(R.id.photo1)
                var imageLayouts1 = imageView1.layoutParams
                imageLayouts1.width = columnWidth
                imageLayouts1.height = columnHeight
                Glide.with(itemView).load(photo.coverImage1).into(itemView.photo1)
                imageView1.setOnClickListener {
                    val data = photo.coverImage1
                    if (imageView1.background !== null) {
                        imageView1.background = null
                        CoroutineScope(IO).launch { util.removeFromTransferQueue(data) }
                    } else {
                        imageView1.setBackgroundColor(Color.rgb(47, 127, 45))
                        CoroutineScope(IO).launch {
                            util.insertIntoTransferQueue(
                                data.split("/")[data.split(
                                    "/"
                                ).size - 1], "ASK SID MB", data, "INQUEUE", "IMAGE"
                            )
                        }
                    }
                }
            }


            CoroutineScope(Main).launch {
                val imageView2 = itemView.findViewById<ImageView>(R.id.photo2)
                val imageLayouts2 = imageView2.layoutParams
                imageLayouts2.width = columnWidth
                imageLayouts2.height = columnHeight
                Glide.with(itemView).load(photo.coverImage2).into(itemView.photo2)
                imageView2.setOnClickListener {
                    val data = photo.coverImage2
                    if (imageView2.background !== null) {
                        imageView2.background = null
                        CoroutineScope(IO).launch { util.removeFromTransferQueue(data) }
                    } else {
                        imageView2.setBackgroundColor(Color.rgb(47, 127, 45))
                        CoroutineScope(IO).launch {
                            util.insertIntoTransferQueue(
                                data.split("/")[data.split(
                                    "/"
                                ).size - 1], "ASK SID MB", data, "INQUEUE", "IMAGE"
                            )
                        }
                    }
                }
            }




            CoroutineScope(Main).launch {
                val imageView3 = itemView.findViewById<ImageView>(R.id.photo3)
                val imageLayouts3 = imageView3.layoutParams
                imageLayouts3.width = columnWidth
                imageLayouts3.height = columnHeight
                Glide.with(itemView).load(photo.coverImage3).into(itemView.photo3)
                imageView3.setOnClickListener {
                    val data = photo.coverImage3
                    if (imageView3.background !== null) {
                        imageView3.background = null
                        CoroutineScope(IO).launch { util.removeFromTransferQueue(data) }
                    } else {
                        imageView3.setBackgroundColor(Color.rgb(47, 127, 45))
                        CoroutineScope(IO).launch {
                            util.insertIntoTransferQueue(
                                data.split("/")[data.split(
                                    "/"
                                ).size - 1], "ASK SID MB", data, "INQUEUE", "IMAGE"
                            )
                        }
                    }
                }
            }
//            TODO() :- SIDDHARTH
//             make proper loading for loading photos and its size

        }
    }
    fun setScreen(heightTemp: Int, widthTemp: Int) {
        height = heightTemp
        width = widthTemp
    }
}