package com.lucian.myvideoplayer

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView
import kotlinx.coroutines.*

/**
 * Activity to play videos with horizontal sliding.
 */
class MainActivity: AppCompatActivity() {
    // Fields.
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(TAG, "CoroutineExceptionHandler() - An exception occurs: ", throwable.fillInStackTrace())
        viewModel.queryState.value = QueryState.ERROR
    }
    private val player: Player by lazy {
        ExoPlayer.Builder(this).build()
    }
    private val playerStateListener = object: Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            // call super
            super.onPlaybackStateChanged(playbackState)

            // switch to next page
            if (playbackState == Player.STATE_ENDED) {
                val nextPage = videoPager.currentItem + 1
                val totalPage = videoPager.adapter?.itemCount ?: 0
                if (nextPage < totalPage) {
                    videoPager.setCurrentItem(nextPage, true)
                }
            }
        }
    }
    private lateinit var videoPager: ViewPager2
    private val viewModel: VideoViewModel by lazy {
        ViewModelProvider(this, VideoViewModelFactory())[VideoViewModel::class.java]
    }

    // Create.
    override fun onCreate(savedInstanceState: Bundle?) {
        // call super
        super.onCreate(savedInstanceState)

        // initialize layout
        setContentView(R.layout.activity_main)
        VideoAdapter(playerStateListener).also { adapter ->
            // initialize view pager
            videoPager = findViewById<ViewPager2>(R.id.video_pager).also {
                it.adapter = adapter
                it.keepScreenOn = true
            }

            // query video data
            viewModel.viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
                adapter.submitList(viewModel.queryOnline())
            }
        }

        // observe query result
        viewModel.queryState.observe(this) { state ->
            if (state == QueryState.ERROR) {
                Toast.makeText(this, R.string.toast_network_error, Toast.LENGTH_LONG).show()
            }
        }
    }

    // Start.
    override fun onStart() {
        // call super
        super.onStart()

        // play video
        (videoPager[0] as RecyclerView).findViewById<StyledPlayerView>(R.id.video_view)?.player?.also {
            if (!it.isPlaying) {
                it.play()
            }
        }
    }

    // Stop.
    override fun onStop() {
        // call super
        super.onStop()

        // pause video
        (videoPager[0] as RecyclerView).findViewById<StyledPlayerView>(R.id.video_view)?.player?.also {
            if (it.isPlaying) {
                it.pause()
            }
        }
    }

    // Called when window focus changed.
    override fun onWindowFocusChanged(hasFocus : Boolean) {
        // call super
        super.onWindowFocusChanged(hasFocus)

        // enable immersive mode
        if (hasFocus) {
            val decorView = window.decorView
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
    }
}