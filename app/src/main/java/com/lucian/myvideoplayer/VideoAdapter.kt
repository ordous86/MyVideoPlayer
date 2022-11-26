package com.lucian.myvideoplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.util.MimeTypes
import com.lucian.myvideoplayer.VideoAdapter.VideoViewHolder
import com.lucian.myvideoplayer.VideoRepository.DataItem

/**
 * Adapter for video player.
 */
class VideoAdapter(private val playerStateListener: Player.Listener): ListAdapter<DataItem, VideoViewHolder>(DiffCallback) {
    // Callback for calculating the diff between 2 list items.
    companion object DiffCallback: DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem) = (oldItem === newItem)
        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem) = (oldItem.videoId == newItem.videoId)
    }

    // Define view holder for adapter.
    inner class VideoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        // Fields.
        lateinit var videoId: String

        // Set data to list item.
        fun setData(id: String) {
            videoId = id
        }
    }

    // Bind.
    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.setData(getItem(position).videoId)
    }

    // Create.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VideoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.video_layout, parent, false))

    // Attach.
    override fun onViewAttachedToWindow(holder: VideoViewHolder) {
        // call super
        super.onViewAttachedToWindow(holder)

        // initialize video player
        val videoView = holder.itemView.findViewById<StyledPlayerView>(R.id.video_view)
        videoView.player = ExoPlayer.Builder(videoView.context).build().also { player ->
            player.addMediaItem(MediaItem.Builder().let { builder ->
                builder.setUri("$VIDEO_URL${holder.videoId}$VIDEO_SUFFIX")
                builder.setMimeType(MimeTypes.APPLICATION_MP4)
                builder.build()
            })
            player.playWhenReady = true
            player.addListener(playerStateListener)
            player.seekTo(0, 0L)
            player.prepare()
        }
    }

    // Detach.
    override fun onViewDetachedFromWindow(holder: VideoViewHolder) {
        // call super
        super.onViewDetachedFromWindow(holder)

        // release video player
        holder.itemView.findViewById<StyledPlayerView>(R.id.video_view).player?.also { player ->
            player.removeListener(playerStateListener)
            player.release()
        }
    }
}