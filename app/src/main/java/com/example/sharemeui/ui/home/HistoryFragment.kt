package com.example.sharemeui.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharemeui.R
import java.sql.Date

class HistoryFragment : Fragment() {
    val TAG = "HistoryFrag"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val historyFragment =  inflater.inflate(R.layout.fragment_history, container, false)
        val historyAdapter = historyAdapter()
        val recyclerView = historyFragment.findViewById<RecyclerView>(R.id.historyRCV)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = historyAdapter

        val util = this.context?.let { Util(it) }
        if (util != null) {
            val history = util.getAllHistory()
            historyAdapter.sethistory(history as MutableList<HistoryEntity>)
            Log.d("$TAG", history.toString())
        }
        return historyFragment
    }
}