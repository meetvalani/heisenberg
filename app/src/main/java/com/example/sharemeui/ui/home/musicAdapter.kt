package com.example.sharemeui.ui.home
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sharemeui.R
import kotlinx.android.synthetic.main.list_music.view.*


class musicAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var musicList = mutableListOf<music>()
    val TAG = "musicAdapter"

    fun setMusic(musics: MutableList<music>) {
        this.musicList = musics
        notifyDataSetChanged()
    }

    fun updateMusicList(music: List<music> ) {
        Log.d("debug:- new Data ", music.toString())
        val oldSize = this.musicList.size
        Log.d("debug:- old Size ", oldSize.toString())
        this.musicList.addAll(music)
        Log.d("debug:- total data", musicList.toString())
        val newSize = this.musicList.size
        Log.d("debug:- new Size ", newSize.toString())
        notifyItemRangeInserted(oldSize, newSize)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return musicViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_music, parent, false
            )
        )
    }
    override fun getItemCount(): Int {
        return musicList.size
    }
    override fun getItemViewType(position: Int): Int {
        Log.d("debug:-  view Type", musicList[position].title)
        return 1
    }
    override fun onBindViewHolder(holder:  RecyclerView.ViewHolder, position: Int) {
        if (holder is musicViewHolder){
            holder.bind(musicList[position])
        }
    }
    inner class musicViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(music: music) {
            Log.d("debug:- in here", "ok")
            itemView.title?.text = music.title
            itemView.size?.text = (Math.round((music.size.toDouble() / ( 1024 * 1024 )) * 100.0)/100.0).toString() + " MB"
            Glide.with(this.itemView).asBitmap().load("/storage/emulated/0/Download/Tenu Na Bol Pawaan (Behen Hogi Teri).mp3").into(itemView.coverImage)
        }
    }
}