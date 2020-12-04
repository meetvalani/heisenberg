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
import java.net.Socket
import java.nio.charset.Charset
import java.util.*

class ClientHandler (client: Socket) {
    val TAG = "ClientHandler"
    val client = client
    var clientFileName: String = "null"
     fun run(context: Context) {
         try {
             Log.d(TAG, "Started Client $client")
             val inputStream: InputStream = client.getInputStream()
             val reader: Scanner = Scanner(inputStream)
             val writer: OutputStream = client.getOutputStream()
             while (true) {
                 val message = reader.nextLine() ?: "null"
                 Log.d(TAG, "client $clientFileName:- $message")
                 if (message.equals("PING")) {
                     send(writer, "I'm server")
                 } else if (message.equals("EXIT")) {
                     client.close()
                     break
                 } else if (message.contains("FILE", ignoreCase = true)) {
                     val fileProperty = message.split("::/::")
                     if (fileProperty.isEmpty() || fileProperty.size < 4)
                         break
                     val title = fileProperty[1]
                     val size = fileProperty[2]
                     val type = fileProperty[3]
                     clientFileName = title
                     Log.d(TAG, fileProperty.toString())
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
                     val directoryName = Environment.getExternalStorageDirectory().toString() + "/ShareMe"
                     val directory = File(directoryName)
                     if (!directory.exists()) {
                         directory.mkdir()
                     }
                     var subDirectoryName = directoryName + "/${getSubDirectoryName(type)}"
                     val subDirectory = File(subDirectoryName)
                     if (!subDirectory.exists()) {
                         subDirectory.mkdir()
                     }
                     val localFilePath = subDirectoryName + "/$title"
                     val yourFile = File(localFilePath)
                     yourFile.createNewFile()
                     val fos = FileOutputStream(localFilePath)
                     val bitSize = 1024
                     val endFlag = "EXIT::/::"
                     while (true) {
                         val buffer = ByteArray(bitSize)
                         val size = inputStream.read(buffer)
                         if (String(buffer).contains(endFlag)) {
                             Log.d(TAG, "server $localFilePath:- End of the file i got $endFlag(${endFlag.toByteArray().size})(${size - endFlag.toByteArray().size})")
//                             fos.write(buffer, 0, size - endFlag.toByteArray().size)
                             fos.write(buffer, 0, size)
                             break
                         }
                         fos.write(buffer, 0, size)
                         Log.d(TAG, buffer.size.toString() + " - " + size.toString() )
                         send(writer, "COOL")
                     }
                     fos.flush()
                     fos.close()
                     client.close()
                     break
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

    fun  getSubDirectoryName(type: String?) : String{
        if(type.equals("IMAGE")) {
            return "Images"
        } else if (type.equals("VIDEO")) {
            return "Videos"
        } else if (type.equals("MUSIC")) {
            return "Audios"
        } else if (type.equals("APP")) {
            return "Apps"
        } else
            return "Others"
    }
}