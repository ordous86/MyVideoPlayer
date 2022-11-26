package com.lucian.myvideoplayer

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Service for web access.
 */
object WebService {
    // Fields.
    val api: WebApi by lazy {
        Retrofit.Builder()
            .baseUrl(SOURCE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WebApi::class.java)
    }

    // Define interface for web APIs.
    interface WebApi {
        @POST("test1.0/backstage/exm1/")
        @Headers(
            "Accept: application/json",
            "Content-type: application/json",
            "Authorization: 0123456789#0#examId"
        )
        suspend fun requestOnlineData(): Response<WebDataStruct>
    }

    // Define structure for web data.
    data class WebDataStruct (
        @SerializedName("p")
        var root: List<WebSubStruct>
    )

    // Define structure for web sub data.
    data class WebSubStruct (
        @SerializedName("source")
        var source: List<Any>
    )
}