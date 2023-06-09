package com.app.data.repository

import com.app.data.model.BaseEndPointResponse
import com.app.data.model.pray_times.PrayItem
import com.app.data.model.pray_times.PrayerTimes
import com.app.data.model.qibla.QiblaDirection
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface AppRepo {

    // get prayer times
    suspend fun getPrayerTimes(
        time: String,
        latitude: Double,
        longitude: Double
    ): Flow<Response<BaseEndPointResponse<ArrayList<PrayerTimes>>>>

    // get qibla direction
    suspend fun getQiblaDirection(
        latitude: Double,
        longitude: Double
    ): Flow<Response<BaseEndPointResponse<QiblaDirection>>>


    suspend fun saveToDB(array: Array<PrayerTimes>)

    suspend fun getFromDB(): List<PrayerTimes>
    suspend fun resetDB()
}