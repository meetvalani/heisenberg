package com.example.sharemeui.ui.home
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sharemeui.R
import kotlinx.android.synthetic.main.list_photo.view.*


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
        Log.d("debug:- new Data ", photo.toString())
        val oldSize = this.photoList.size
        Log.d("debug:- old Size ", oldSize.toString())
        this.photoList.addAll(photo)
        Log.d("debug:- total data", photoList.toString())
        val newSize = this.photoList.size
        Log.d("debug:- new Size ", newSize.toString())
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
            Log.d("debug:- in here", "ok")
//            itemView.title?.text = photo.title
//            itemView.size?.text = (Math.round((photo.size.toDouble() / ( 1024 * 1024 )) * 100.0)/100.0).toString() + " MB"
            var columnWidth = (width / imagePerRow).toInt()
            var columnHeight = (columnWidth * 4 / 3).toInt()

            var imageLayouts = itemView.findViewById<ImageView>(R.id.photo).layoutParams
            imageLayouts.width = columnWidth
            imageLayouts.height = columnHeight
            imageLayouts = itemView.findViewById<ImageView>(R.id.photo1).layoutParams
            imageLayouts.width = columnWidth
            imageLayouts.height = columnHeight
            imageLayouts = itemView.findViewById<ImageView>(R.id.photo2).layoutParams
            imageLayouts.width = columnWidth
            imageLayouts.height = columnHeight
            imageLayouts = itemView.findViewById<ImageView>(R.id.photo3).layoutParams
            imageLayouts.width = columnWidth
            imageLayouts.height = columnHeight

            Glide.with(this.itemView).asBitmap().load(photo.coverImage).centerCrop().into(itemView.photo)
            Glide.with(this.itemView).asBitmap().load(photo.coverImage1).centerCrop().into(itemView.photo1)
            Glide.with(this.itemView).asBitmap().load(photo.coverImage2).centerCrop().into(itemView.photo2)
            Glide.with(this.itemView).asBitmap().load(photo.coverImage3).centerCrop().into(itemView.photo3)
            Log.d(TAG, "Height and Width are :- ($height, $width)")
//            TODO() :- SIDDHARTH
//             make proper login for loading photos and its size

        }
    }
    fun setScreen(heightTemp: Int, widthTemp: Int) {
        height = heightTemp
        width = widthTemp
    }
}