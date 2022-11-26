package com.lucian.myvideoplayer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lucian.myvideoplayer.VideoRepository.DataItem

/**
 * View model to connect [MainActivity] and [VideoRepository]
 **/
class VideoViewModel: ViewModel() {
    // Fields.
    private val repository = VideoRepository()
    val queryState = MutableLiveData<QueryState>().apply { value = QueryState.IDLE }

    // Query online video data.
    suspend fun queryOnline(): List<DataItem> {
        // set query state
        queryState.value = QueryState.RUNNING

        // start query
        repository.fetchOnlineData().also { response ->
            // check response code
            queryState.value = response.code().let { code ->
                if (code == 200)
                    QueryState.SUCCESS
                else
                    QueryState.ERROR
            }

            // check response body
            return response.body()?.let { body ->
                mutableListOf<DataItem>().also { dataList ->
                    for (data in body.root) {
                        dataList.add(DataItem(data.source[0] as String))
                    }
                }
            } ?: emptyList()
        }
    }
}