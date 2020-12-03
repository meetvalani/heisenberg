package com.example.sharemeui.ui.home
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.example.sharemeui.R
import kotlinx.android.synthetic.main.list_app.view.*
import kotlin.math.ceil


class appAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var appList = mutableListOf<app>()
    val TAG = "appAdapter"
    var height = 0
    var width = 0
    var appPerRow = 4


    fun setApp(apps: MutableList<app>) {
        this.appList = apps
        notifyDataSetChanged()
    }

    fun updateAppList(app: MutableList<app> ) {
        Log.d("debug:- new Data ", app.toString())
        val oldSize = this.appList.size
        Log.d("debug:- old Size ", oldSize.toString())
        this.appList = app
        Log.d("debug:- total data", appList.toString())
        val newSize = this.appList.size
        Log.d("debug:- new Size ", newSize.toString())
        notifyItemRangeInserted(oldSize, newSize)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return appViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_app, parent, false
            )
        )
    }
    override fun getItemCount(): Int {
        var listSize: Int = ceil((appList.size.toDouble() / appPerRow)).toInt()
        return listSize
    }
    override fun getItemViewType(position: Int): Int {
        Log.d("debug:-  view Type", appList[position].title)
        return 1
    }
    override fun onBindViewHolder(holder:  RecyclerView.ViewHolder, position: Int) {
        if (holder is appViewHolder){
            holder.bind(appList, position)
        }
    }
    inner class appViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(appList: MutableList<app>, position: Int) {
            Log.d("debug:- in here", "ok")
            var columnWidth = (width / appPerRow)
            var basePosition = position * appPerRow
            var actualPosition = basePosition

            var appsLayoutsList = itemView.findViewById<LinearLayout>(R.id.main_box).children
            for (appsLayout in appsLayoutsList) {
                appsLayout.layoutParams.width = columnWidth
                appsLayout.setPadding(5)
            }
            
            // for first app of list, no need to check (actualPosition < appList.size)
            itemView.title?.text = appList[actualPosition].title
            itemView.size?.text =
                (Math.round((appList[actualPosition].size.toDouble() / (1024 * 1024)) * 100.0) / 100.0).toString() + " MB"
            itemView.photo.setImageDrawable(appList[actualPosition].coverImage)
            actualPosition += 1

            if (actualPosition < appList.size) {
                itemView.title1?.text = appList[actualPosition].title
                itemView.size1?.text =
                    (Math.round((appList[actualPosition].size.toDouble() / (1024 * 1024)) * 100.0) / 100.0).toString() + " MB"
                itemView.photo1.setImageDrawable(appList[actualPosition].coverImage)
            } else {
                itemView.title1?.text = ""
                itemView.size1?.text = ""
                itemView.photo1.setImageDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            actualPosition += 1

            if (actualPosition < appList.size) {
                itemView.title2?.text = appList[actualPosition].title
                itemView.size2?.text =
                    (Math.round((appList[actualPosition].size.toDouble() / (1024 * 1024)) * 100.0) / 100.0).toString() + " MB"
                itemView.photo2.setImageDrawable(appList[actualPosition].coverImage)
            } else {
                itemView.title2?.text = ""
                itemView.size2?.text = ""
                itemView.photo2.setImageDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            actualPosition += 1

            if (actualPosition < appList.size) {
                itemView.title3?.text = appList[actualPosition].title
                itemView.size3?.text =
                    (Math.round((appList[actualPosition].size.toDouble() / (1024 * 1024)) * 100.0) / 100.0).toString() + " MB"
                itemView.photo3.setImageDrawable(appList[actualPosition].coverImage)
            } else {
                itemView.title3?.text = ""
                itemView.size3?.text = ""
                itemView.photo3.setImageDrawable(ColorDrawable(Color.TRANSPARENT))
            }

        }
    }

    fun setScreen(heightTemp: Int, widthTemp: Int) {
        height = heightTemp
        width = widthTemp
    }
}