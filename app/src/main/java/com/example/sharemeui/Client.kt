package com.example.sharemeui
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.util.Log
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import java.io.File
import java.io.OutputStream
import java.net.Socket
import java.nio.charset.Charset
import java.util.*

class Client (ip: String, port: Int?) {
    val TAG = "Client"
    val ip = ip
    var port : Int = port ?: 7567
    suspend fun run(): Int? {
        try {
            val server: Socket = Socket(ip, port)
            val reader: Scanner = Scanner(server.getInputStream())
            val writer: OutputStream = server.getOutputStream()
            Log.d(TAG, "connected to server $server")
            Log.d(TAG, "sending ping to $ip:$port")
            send(writer, "PING")
            while (true) {
                val message = reader.nextLine()  ?: "null"
                Log.d(TAG, "Server($ip, $port): $message")
                if (message.equals("I'm server")) {
                    send(writer, "EXIT")
                    server.close()
                    return 1
                }
                if (message.equals("EXIT")) {
                    server.close()
                    return 1
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, e.printStackTrace().toString())
            return 0
        }
        return 0
    }

    suspend fun sendFile(filePath: String, context: Context): Int? {
        try {
            val server: Socket = Socket(ip, port)
            val reader: Scanner = Scanner(server.getInputStream())
            val writer: OutputStream = server.getOutputStream()
            Log.d(TAG, "connected to receiver $server")
            Log.d(TAG, "sending ping to confirm $ip:$port")
            send(writer, "PING")
            while (true) {
                val message = reader.nextLine()  ?: "null"
                Log.d(TAG, "Server($ip, $port): $message")
                if (message.equals("I'm server")) {
                    send(writer, "FILE")
                    val permission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(context as Activity,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            1)
                    } else {
                        Log.d(TAG, "permission is granted dude")
                    }
                    File(filePath).inputStream().buffered().use { input ->
                        val bitSize = 1024
                        while(true) {
                            val buff = ByteArray(bitSize)
                            val sz = input.read(buff)
                            writer.write(buff)
                            if (sz <= 0) break
                            Log.d(TAG,"sending file to ($ip, $port) :- ${sz}, ${buff}")
                        }
                        send(writer, "EXIT")
                        input.close()
                    }
                }
                if (message.equals("EXIT")) {
                    server.close()
                    return 1
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, e.printStackTrace().toString())
            return 0
        }
        return 0
    }


    fun send(writer: OutputStream, message: String?) {
        writer.write((message + '\n').toByteArray(Charset.defaultCharset()))
    }
}