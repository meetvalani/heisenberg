package com.example.sharemeui.ui.home
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sharemeui.R
import kotlinx.android.synthetic.main.list_app.view.*


class appAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var appList = mutableListOf<app>()
    val TAG = "appAdapter"

    fun setApp(apps: MutableList<app>) {
        this.appList = apps
        notifyDataSetChanged()
    }

    fun updateAppList(app: MutableList<app> ) {
        val oldSize = this.appList.size
        this.appList = app
        val newSize = this.appList.size
        notifyItemRangeInserted(oldSize, newSize)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return appViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_app, parent, false
            )
        )
    }
    override fun getItemCount(): Int {
        return appList.size
    }
    override fun getItemViewType(position: Int): Int {
        return 1
    }
    override fun onBindViewHolder(holder:  RecyclerView.ViewHolder, position: Int) {
        if (holder is appViewHolder){
            holder.bind(appList[position])
        }
    }
    inner class appViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(app: app) {
            itemView.title.gravity = Gravity.CENTER
            itemView.size.gravity = Gravity.CENTER
            itemView.title?.text = app.title
            itemView.size?.text = (Math.round((app.size.toDouble() / ( 1024 * 1024 )) * 100.0)/100.0).toString() + " MB"
            itemView.photo.setImageDrawable(app.coverImage)

            itemView.title1.gravity = Gravity.CENTER
            itemView.size1.gravity = Gravity.CENTER
            itemView.title1?.text = app.title1
            itemView.size1?.text = (Math.round((app.size1.toDouble() / ( 1024 * 1024 )) * 100.0)/100.0).toString() + " MB"
            itemView.photo1.setImageDrawable(app.coverImage1)

            itemView.title1.gravity = Gravity.CENTER
            itemView.size1.gravity = Gravity.CENTER
            itemView.title2?.text = app.title2
            itemView.size2?.text = (Math.round((app.size2.toDouble() / ( 1024 * 1024 )) * 100.0)/100.0).toString() + " MB"
            itemView.photo2.setImageDrawable(app.coverImage2)

            itemView.title1.gravity = Gravity.CENTER
            itemView.size1.gravity = Gravity.CENTER
            itemView.title3?.text = app.title3
            itemView.size3?.text = (Math.round((app.size3.toDouble() / ( 1024 * 1024 )) * 100.0)/100.0).toString() + " MB"
            itemView.photo3.setImageDrawable(app.coverImage3)

//            Glide.with(this.itemView).asBitmap().load("/storage/emulated/0/Download/Tenu Na Bol Pawaan (Behen Hogi Teri).mp3").into(itemView.coverImage)
        }
    }
}