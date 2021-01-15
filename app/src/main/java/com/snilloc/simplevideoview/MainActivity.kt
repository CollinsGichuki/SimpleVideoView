package com.snilloc.simplevideoview

import android.media.MediaPlayer.OnCompletionListener
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.URLUtil
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private val VIDEO_SAMPLE = "https://developers.google.com/training/images/tacoma_narrows.mp4"
    private lateinit var videoView: VideoView
    private lateinit var mediaController: MediaController
    private lateinit var progressBar: ProgressBar

    //Variable to preserve the playback position of the video
    private var currentPosition: Int = 0
    //Key for currentPosition when stored in bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Initialize the MediaController
        mediaController = MediaController(this)
        //The Progressbar
        progressBar = findViewById(R.id.progress_bar)
        //Reference to the VideoView
        videoView = findViewById(R.id.video_view)
        //Connect the MediaController with the Videoview
        mediaController.setMediaPlayer(videoView)
        //Connect the VideoView with the MediaController
        videoView.setMediaController(mediaController)
    }

    //Converts the name of the video to a Uri
    private fun getMedia(mediaName: String): Uri {
        //Check if it is a valid Uri
        return if (URLUtil.isValidUrl(mediaName)) {
            Uri.parse(mediaName)
        } else {
            //Media source is a raw resource embedded in the app
            Uri.parse("android.resource://$packageName/raw/$mediaName")
        }
    }

    private fun initializePlayer() {
        //Show the Progress Bar
        progressBar.visibility = View.VISIBLE
        val videoUir: Uri = getMedia(VIDEO_SAMPLE)
        videoView.setVideoURI(videoUir)

        //Listen for when the video has finished buffering
        videoView.setOnPreparedListener() {
            progressBar.visibility = View.INVISIBLE
            //Start playing the video
            videoView.start()
        }

        //Show text and seek to the beginning of the video when the video ends
        videoView.setOnCompletionListener(OnCompletionListener {
            Toast.makeText(this@MainActivity, "Playback completed",
                    Toast.LENGTH_SHORT).show()
            videoView.seekTo(1)
        })
    }

    private fun releasePlayer() {
        videoView.stopPlayback()
    }

    override fun onStart() {
        super.onStart()
        //Start Playing the video
        initializePlayer()
    }

    override fun onStop() {
        super.onStop()
        //Stop playing the video
        releasePlayer()
    }
}