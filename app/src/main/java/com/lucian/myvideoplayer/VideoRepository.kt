package com.lucian.myvideoplayer

/**
 * Repository of video information.
 */
class VideoRepository {
    // Define data item for video list.
    data class DataItem (
        val videoId: String
    )

    // Fetch online video data.
    suspend fun fetchOnlineData() = WebService.api.requestOnlineData()
}