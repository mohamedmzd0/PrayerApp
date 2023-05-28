package com.app.data.remote

import com.app.data.model.EndPointResponse
import com.app.data.model.pray_times.PrayerTimes
import com.app.data.model.qibla.QiblaDirection
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {

    // get prayer times
    @GET("calendar/{time}")
    suspend fun getPrayerTimes(
        @Path("time") time: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
    ): Response<EndPointResponse<ArrayList<PrayerTimes>>>


    // get qibla direction
    @GET("qibla/{latitude}/{longitude}")
    suspend fun getQiblaDirection(
        @Path("latitude") latitude: Double,
        @Path("longitude") longitude: Double,
    ): Response<EndPointResponse<QiblaDirection>>
}