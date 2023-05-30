package com.app.data.repository

import com.app.data.cache.PrayerAppDatabase
import com.app.data.model.pray_times.PrayerTimes
import com.app.data.remote.AppApi
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AppRepoImpl @Inject constructor(
    private val userApi: AppApi, private val room: PrayerAppDatabase
) : AppRepo {
    // get pray times
    override suspend fun getPrayerTimes(
        time: String, latitude: Double, longitude: Double
    ) = flow { emit(userApi.getPrayerTimes(time, latitude, longitude)) }

    //qibla direction
    override suspend fun getQiblaDirection(
        latitude: Double, longitude: Double
    ) = flow { emit(userApi.getQiblaDirection(latitude, longitude)) }

    override suspend fun saveToDB(array: Array<PrayerTimes>) {
        room.prayerTimesDao().insertPrayerTimes(array.toList())
    }

    override suspend fun getFromDB() = room.prayerTimesDao().getAllPrayerTimes()

    override suspend fun resetDB() =room.prayerTimesDao().deleteAll()


}