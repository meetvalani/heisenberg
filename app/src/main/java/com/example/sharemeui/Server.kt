package com.example.sharemeui

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.net.ServerSocket

class Server () {
    var TAG = "Server"
    var port = 7567
    fun createServer(context: Context){
        try {
            CoroutineScope(IO).launch {
                val server = ServerSocket(port)
                Log.d(TAG, "server is started on ${port}")
                while (true) {
                    Log.d(TAG, "waiting for client to connect")
                    val client = server.accept()
                    println("Client connected: ${client.inetAddress.hostAddress}")
                    CoroutineScope(IO).launch {
                        ClientHandler(client).run(context)
                }
                }
            }
        } catch (e: Exception) {
            Log.d(TAG,"exception ${e.printStackTrace()}")
        }

    }
}