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
import com.example.sharemeui.MainActivity
import com.example.sharemeui.R
import kotlinx.android.synthetic.main.fragment_photo.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {
    val TAG = "HistoryFrag"
    val historyAdapter = historyAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val historyFragment =  inflater.inflate(R.layout.fragment_history, container, false)
        val recyclerView = historyFragment.findViewById<RecyclerView>(R.id.historyRCV)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = historyAdapter

        val util = this.context?.let { Util(it) }
        if (util != null) {
            CoroutineScope(IO).launch {
                updateHistory(util.getAllHistory())
            }
        }
        return historyFragment
    }
    fun updateHistory(history: List<HistoryEntity>?) {
        if (history !== null && history.isNotEmpty()) {
            CoroutineScope(Main).launch {
                Log.d("$TAG", "updating history in main thread")
                historyAdapter.sethistory(history as MutableList<HistoryEntity>)
                Log.d("$TAG", history.toString())
            }
        }
    }
}