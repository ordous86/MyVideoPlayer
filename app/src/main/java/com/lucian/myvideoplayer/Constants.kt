package com.lucian.myvideoplayer

/**
 * Define constants for global access.
 */
interface Constants

const val SOURCE_URL = "https://srv0api-dev-v2-dot-framy-stage.uc.r.appspot.com/"
const val TAG = "VideoPlayerDemo"
const val VIDEO_SUFFIX = ".mp4"
const val VIDEO_URL = "https://storage.googleapis.com/pst-framy/vdo/"

enum class QueryState {
    IDLE,
    ERROR,
    RUNNING,
    SUCCESS
}