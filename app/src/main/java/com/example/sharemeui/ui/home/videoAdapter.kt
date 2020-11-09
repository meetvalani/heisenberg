package com.example.sharemeui.ui.home
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sharemeui.R
import kotlinx.android.synthetic.main.list_video.view.*


class videoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var videoList = mutableListOf<video>()
    val TAG = "videoAdapter"

    fun setVideo(videos: MutableList<video>) {
        this.videoList = videos
        notifyDataSetChanged()
    }

    fun updateVideoList(video: List<video> ) {
        Log.d("debug:- new Data ", video.toString())
        val oldSize = this.videoList.size
        Log.d("debug:- old Size ", oldSize.toString())
        this.videoList.addAll(video)
        Log.d("debug:- total data", videoList.toString())
        val newSize = this.videoList.size
        Log.d("debug:- new Size ", newSize.toString())
        notifyItemRangeInserted(oldSize, newSize)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return videoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_video, parent, false
            )
        )
    }
    override fun getItemCount(): Int {
        return videoList.size
    }
    override fun getItemViewType(position: Int): Int {
        Log.d("debug:-  view Type", videoList[position].title)
        return 1
    }
    override fun onBindViewHolder(holder:  RecyclerView.ViewHolder, position: Int) {
        if (holder is videoViewHolder){
            holder.bind(videoList[position])
        }
    }
    inner class videoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(video: video) {
            Log.d("debug:- in here", "ok")
            itemView.title?.text = video.title
            itemView.size?.text = (Math.round((video.size.toDouble() / ( 1024 * 1024 )) * 100.0)/100.0).toString() + " MB"
            itemView.coverImage.setImageBitmap(video.coverImage)
        }
    }
}