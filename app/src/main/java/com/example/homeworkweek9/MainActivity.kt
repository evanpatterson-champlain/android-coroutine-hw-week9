package com.example.homeworkweek9

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.homeworkweek9.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL




private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {

    private val imageStorage = ImageStorage()
    private lateinit var imageURLs: Array<out String>
    private lateinit var imageDescriptions: Array<out String>
    private var curImageIndex = 0

    private fun initVariables() {
        imageURLs = resources.getStringArray(R.array.image_url_list)
        imageDescriptions = resources.getStringArray(R.array.image_descriptions)
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
        GlobalScope.launch(Dispatchers.IO) {
            val imageUrl = imageURLs[curImageIndex]
            val bitmapImage = imageStorage.requestBitmap(imageUrl)

            launch(Dispatchers.Main) {
                Log.d("CoroutineExercise", "Name Of thread is  ${Thread.currentThread().name}")
                binding.imageView.setImageBitmap(bitmapImage)
                binding.imageDescription.text = imageDescriptions[curImageIndex]
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setOnTouchDescription() {
        binding.imageDescription.setOnTouchListener(OnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                curImageIndex++
                curImageIndex %= imageURLs.size
                launchImageGet()
            }
            false
        })
    }

}