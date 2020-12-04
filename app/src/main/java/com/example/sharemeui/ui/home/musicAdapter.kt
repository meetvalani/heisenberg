package com.example.sharemeui.ui.home
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sharemeui.R
import kotlinx.android.synthetic.main.list_music.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class musicAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var musicList = mutableListOf<music>()
    val TAG = "musicAdapter"

    fun setMusic(musics: MutableList<music>) {
        this.musicList = musics
        notifyDataSetChanged()
    }

    fun updateMusicList(music: List<music> ) {
        val oldSize = this.musicList.size
        this.musicList.addAll(music)
        val newSize = this.musicList.size
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
        return 1
    }
    override fun onBindViewHolder(holder:  RecyclerView.ViewHolder, position: Int) {
        if (holder is musicViewHolder){
            holder.bind(musicList[position])
        }
    }
    inner class musicViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(music: music) {
            val util = Util(itemView.context)
            itemView.title?.text = music.title
            itemView.size?.text = (Math.round((music.size.toDouble() / ( 1024 * 1024 )) * 100.0)/100.0).toString() + " MB"
//            Glide.with(this.itemView).asBitmap().load("/storage/emulated/0/Download/Tenu Na Bol Pawaan (Behen Hogi Teri).mp3").into(itemView.coverImage)
            itemView.setOnClickListener {
                if (itemView.background !== null) {
                    itemView.background = null
                    CoroutineScope(Dispatchers.IO).launch { util.removeFromTransferQueue(music.data) }
                } else {
                    itemView.setBackgroundColor(Color.rgb(47, 127, 45))
                    CoroutineScope(Dispatchers.IO).launch {
                        util.insertIntoTransferQueue(music.title, "ASK SID MB", music.data, "INQUEUE", "AUDIO")
                    }
                }
            }
        }
    }
}