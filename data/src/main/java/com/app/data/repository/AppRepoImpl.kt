package com.app.data.repository

import com.app.data.remote.UserApi
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AppRepoImpl @Inject constructor(private val userApi: UserApi) : AppRepo {
    // get pray times
    override suspend fun getPrayerTimes(
        time: String,
        latitude: Double,
        longitude: Double
    ) = flow { emit(userApi.getPrayerTimes(time, latitude, longitude)) }

    //qibla direction
    override suspend fun getQiblaDirection(
        latitude: Double,
        longitude: Double
    ) = flow { emit(userApi.getQiblaDirection(latitude, longitude)) }


}