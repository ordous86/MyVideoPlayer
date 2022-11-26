package com.lucian.myvideoplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Factory to build [VideoViewModel].
 */
@Suppress("UNCHECKED_CAST")
class VideoViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        // create
        modelClass.isAssignableFrom(VideoViewModel::class.java) -> VideoViewModel() as T

        // otherwise
        else -> throw IllegalArgumentException("Unknown ViewModel")
    }
}