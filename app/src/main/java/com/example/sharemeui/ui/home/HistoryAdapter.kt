package com.example.sharemeui.ui.home
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sharemeui.R
import kotlinx.android.synthetic.main.list_history.view.*

class historyAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var historyList = mutableListOf<HistoryEntity>()
    val TAG = "historyAdapter"

    fun sethistory(history: MutableList<HistoryEntity>) {
        this.historyList = history
        notifyDataSetChanged()
    }

    fun updateHistoryList(history: List<HistoryEntity> ) {
        Log.d("debug:- new Data ", history.toString())
        val oldSize = this.historyList.size
        Log.d("debug:- old Size ", oldSize.toString())
        this.historyList.addAll(history)
        Log.d("debug:- total data", historyList.toString())
        val newSize = this.historyList.size
        Log.d("debug:- new Size ", newSize.toString())
        notifyItemRangeInserted(oldSize, newSize)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return historyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_history, parent, false
            )
        )
    }
    override fun getItemCount(): Int {
        return historyList.size
    }
    override fun getItemViewType(position: Int): Int {
        Log.d("debug:-  view Type", historyList[position].title)
        return 1
    }
    override fun onBindViewHolder(holder:  RecyclerView.ViewHolder, position: Int) {
        if (holder is historyViewHolder){
            holder.bind(historyList[position])
        }
    }
    inner class historyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(history: HistoryEntity) {
            Log.d("debug:- in here", "ok")
            itemView.title?.text = history.title
            itemView.size?.text = (Math.round((history.size!!.toDouble() / ( 1024 * 1024 )) * 100.0)/100.0).toString() + " MB"
            Log.d(TAG, "sasas" + history.type)
            if (history.type.equals("photo"))
            {Glide.with(this.itemView).asBitmap().load(history.coverImage).centerCrop().into(itemView.coverImage)
            Log.d(TAG, "photo detected")}
            else if (history.type.equals("video"))
                Glide.with(this.itemView).load(history.coverImage).thumbnail(0.1f).into(itemView.coverImage);
            else if (history.type.equals("music"))
                itemView.coverImage.setImageResource(R.mipmap.music_image)
        }
    }
}