package com.example.homeworkweek9

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.net.HttpURLConnection
import java.net.URL
import java.util.Collections.synchronizedMap

// allows for caching of images
class ImageStorage {
    private val images: MutableMap<String, Bitmap> = synchronizedMap(mutableMapOf())

    private fun loadBitmap(urlStr: String): Bitmap {
        val url = URL(urlStr)
        val httpConnection = url.openConnection() as HttpURLConnection
        httpConnection.doInput = true
        httpConnection.connect()
        val inputStream = httpConnection.inputStream
        return BitmapFactory.decodeStream(inputStream)
    }

    fun requestBitmap(source: String): Bitmap {
        return if (images.contains(source)) {
            images[source]!!
        } else {
            loadBitmap(source)
        }
    }


}