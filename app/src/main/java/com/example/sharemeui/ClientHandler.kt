package com.example.sharemeui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.lang.Exception
import java.net.Socket
import java.nio.charset.Charset
import java.util.*

class ClientHandler (client: Socket) {
    val TAG = "ClientHandler"
    val client = client
     fun run(context: Context) {
         try {
             Log.d(TAG, "Started Client $client")
             val inputStream: InputStream = client.getInputStream()
             val reader: Scanner = Scanner(inputStream)
             val writer: OutputStream = client.getOutputStream()
             while (true) {
                 val message = reader.nextLine() ?: "null"
                 Log.d(TAG, "client $client:- $message")
                 if (message.equals("PING")) {
                     send(writer, "I'm server")
                 } else if (message.equals("EXIT")) {
                     client.close()
                     break
                 } else if (message.equals("FILE")) {
                     val permission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                     if (permission != PackageManager.PERMISSION_GRANTED) {
                         Log.d(TAG, "permission is not granted dude")
                         ActivityCompat.requestPermissions(
                             context as Activity,
                             arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                             1
                         )
                     } else {
                         Log.d(TAG, "permission is granted dude")
                     }
                     val yourFile = File(Environment.getExternalStorageDirectory().toString()+"/test.jpg")
                     yourFile.createNewFile()
                     val fos = FileOutputStream(Environment.getExternalStorageDirectory().toString()+"/test.jpg")
                     var old = 0
                     val bitSize = 1024
                     while (true) {
                         val buffer = ByteArray(bitSize)
                         val size = inputStream.read(buffer)

                         Log.d(TAG, "received bytes :- " + size.toString())
                         fos.write(buffer, 0, size)
                         if (size < bitSize)
                             break
                         Log.d(TAG, buffer.size.toString() + " - " + size.toString() )

                         old = size - 1
                     }
                     fos.flush()
                     fos.close()
                 }
             }
             client.close()
         } catch (e: Exception) {
             Log.d(TAG, e.printStackTrace().toString())
         }
    }
    fun send(writer: OutputStream, message: String?) {
        writer.write((message + '\n').toByteArray(Charset.defaultCharset()))
    }
}