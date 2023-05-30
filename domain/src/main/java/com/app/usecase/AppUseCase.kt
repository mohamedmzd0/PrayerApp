package com.app.usecase

import com.app.data.model.BaseEndPointResponse
import com.app.data.model.pray_times.PrayItem
import com.app.data.model.pray_times.PrayerTimes
import com.app.data.model.qibla.QiblaDirection
import com.app.data.repository.AppRepo
import com.app.data.utils.ErrorAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


private const val TAG = "AppUseCase"

class AppUseCase @Inject constructor(private val repo: AppRepo) {



    suspend fun loadFromDB(): List<PrayerTimes> = withContext(Dispatchers.IO) {
        repo.getFromDB()
    }



    suspend fun getPrayerTimes(
        latitude: Double,
        longitude: Double,
        time: String? = null
    ) = repo.getPrayerTimes(
        time ?: getCurrentDate(),
        latitude,
        longitude
    ).map {
        it.body()?.data?.let { prayerTimes ->
            prayerTimes.map { item ->
                item.apply {
                    timingsArrayList.apply {
                        clear()
                        add(PrayItem("Fajr", convertTime(item.timings?.Fajr)))
                        add(PrayItem("Sunrise", convertTime(item.timings?.Sunrise)))
                        add(PrayItem("Dhuhr", convertTime(item.timings?.Dhuhr)))
                        add(PrayItem("Asr", convertTime(item.timings?.Asr)))
                        add(PrayItem("Maghrib", convertTime(item.timings?.Maghrib)))
                        add(PrayItem("Isha", convertTime(item.timings?.Isha)))
                        filter { !it.time.isNullOrBlank() || !it.time.isNullOrEmpty() }

                    }
                }
                item.nextPrayingTime = getNextPrayingItem(item.timingsArrayList)
            }
            repo.resetDB()
            repo.saveToDB(prayerTimes.toTypedArray())
        }
        it
    }.transformResponseData { emit(it) }


    private fun convertTime(string: String?) = convertToAMPM(extractTimeFromString(string))
    private fun extractTimeFromString(timeString: String?): String? {
        val timeParts = timeString?.split(" ")
        return timeParts?.get(0)
    }

    private fun convertToAMPM(timeString: String?): String {
        val sdf24 = SimpleDateFormat("HH:mm", Locale.getDefault())
        val sdf12 = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val date = sdf24.parse(timeString ?: "")
        return sdf12.format(date ?: "")
    }

    fun getCalender() = Calendar.getInstance()

    private fun getNextPrayingItem(sortedList: kotlin.collections.List<PrayItem>): PrayItem? {
        val currentTime = getCalender()
        for (prayerItem in sortedList) {
            val prayerTime = parseTime(prayerItem.time, currentTime)
            if (prayerTime.after(currentTime)) {
                return prayerItem
            }
        }
        return sortedList.firstOrNull()
    }


    private fun parseTime(time: String?, currentTime: Calendar): Calendar {
        val calendar = getCalender()
        time?.let {
            val pattern = "hh:mm a"
            val formatter = SimpleDateFormat(pattern, Locale.getDefault())
            val parsedDate = formatter.parse(time.substringBefore("(").trim())
            parsedDate?.let { date ->
                calendar.time = date
                calendar.set(Calendar.YEAR, currentTime.get(Calendar.YEAR))
                calendar.set(Calendar.MONTH, currentTime.get(Calendar.MONTH))
                calendar.set(Calendar.DAY_OF_YEAR, currentTime.get(Calendar.DAY_OF_YEAR))
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
            }
        }
        return calendar
    }

    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/M", Locale.US)
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    suspend fun getQiblaDirection(
        latitude: Double,
        longitude: Double
    ) = repo.getQiblaDirection(latitude, longitude).transformResponseData { emit(it) }


}