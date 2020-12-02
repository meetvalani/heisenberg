package com.example.sharemeui.ui.home
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sharemeui.R
import kotlinx.android.synthetic.main.list_photo.view.*
import kotlin.math.ceil


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
        var listSize: Int = ceil((photoList.size.toDouble() / imagePerRow)).toInt()
        return listSize
    }
    override fun getItemViewType(position: Int): Int {
        return 1
    }
    override fun onBindViewHolder(holder:  RecyclerView.ViewHolder, position: Int) {
        if (holder is photoViewHolder){
            holder.bind(photoList, position)
        }
    }
    inner class photoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(photoList: MutableList<photo>, position: Int) {
            Log.d("debug:- in here", "ok")
//            itemView.title?.text = photo.title
//            itemView.size?.text = (Math.round((photo.size.toDouble() / ( 1024 * 1024 )) * 100.0)/100.0).toString() + " MB"
            var columnWidth = (width / imagePerRow)
            var columnHeight = (columnWidth * 4 / 3)
            var basePosition = position * imagePerRow
            var actualPosition = basePosition

            var imageLayoutsList = itemView.findViewById<LinearLayout>(R.id.main_box).children
            for (imageLayouts in imageLayoutsList) {
                imageLayouts.layoutParams.width = columnWidth
                imageLayouts.layoutParams.height = columnHeight
            }

            // for first photo of list, no need to check (actualPosition < appList.size)
            Glide.with(this.itemView).asBitmap().load(photoList[actualPosition].coverImage).centerCrop().into(itemView.photo)
            actualPosition += 1

            if (actualPosition < photoList.size) {
                Glide.with(this.itemView).asBitmap().load(photoList[actualPosition].coverImage)
                    .centerCrop().into(itemView.photo1)
            } else {
                Glide.with(this.itemView).asBitmap().load("None")
                    .centerCrop().into(itemView.photo1)
            }
            actualPosition += 1

            if (actualPosition < photoList.size) {
                Glide.with(this.itemView).asBitmap().load(photoList[actualPosition].coverImage)
                    .centerCrop().into(itemView.photo2)
            } else {
                Glide.with(this.itemView).asBitmap().load("None")
                    .centerCrop().into(itemView.photo2)
            }
            actualPosition += 1

            if (actualPosition < photoList.size) {
                Glide.with(this.itemView).asBitmap().load(photoList[actualPosition].coverImage)
                    .centerCrop().into(itemView.photo3)
            } else {
                Glide.with(this.itemView).asBitmap().load("None")
                    .centerCrop().into(itemView.photo3)
            }

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