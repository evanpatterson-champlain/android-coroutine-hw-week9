package com.example.homeworkweek9

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.appcompat.app.AppCompatActivity
import com.example.homeworkweek9.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.coroutines.coroutineContext
import kotlin.math.min


private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {

    private lateinit var blankImage: Drawable

    private lateinit var imageURLs: Array<out String>
    private lateinit var imageDescriptions: Array<out String>
    private var curImageIndex = 0
    private var imgJob: Job? = null

    private fun initVariables() {
        imageURLs = resources.getStringArray(R.array.image_url_list)
        imageDescriptions = resources.getStringArray(R.array.image_descriptions)
        blankImage = getDrawable(R.drawable.blank)!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initVariables()
        val view: View
        binding = ActivityMainBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        setOnTouchDescription()
        launchImageGet()

    }

    private fun launchImageGet() {
        imgJob = GlobalScope.launch(Dispatchers.IO) {
            try {
                val url = URL(imageURLs[curImageIndex])
                val httpConnection = url.openConnection() as HttpURLConnection
                httpConnection.doInput = true
                httpConnection.connect()
                val inputStream = httpConnection.inputStream
                val bitmapImage = BitmapFactory.decodeStream(inputStream)
                launch(Dispatchers.Main) {
                    binding.imageView.setImageBitmap(bitmapImage)
                    binding.imageDescription.text = imageDescriptions[curImageIndex]
                    imgJob = null
                }
            }
            catch (e: Exception) {
                launch(Dispatchers.Main) {
                    binding.imageView.setImageDrawable(blankImage)
                    binding.imageDescription.text = imageDescriptions[curImageIndex]
                    imgJob = null
                }
                Log.d("ERROR", e.toString())
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setOnTouchDescription() {
        binding.imageDescription.setOnTouchListener(OnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (imgJob != null && imgJob!!.isActive) {
                    Log.d("Log", "Cancelled image request")
                    imgJob!!.cancel("Called a new request")
                    imgJob = null
                }
                val ceiling = min(imageURLs.size, imageDescriptions.size)
                curImageIndex++
                curImageIndex %= ceiling
                launchImageGet()
            }
            false
        })
    }

}