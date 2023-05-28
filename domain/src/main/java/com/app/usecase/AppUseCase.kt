package com.app.usecase

import android.util.Log
import com.app.data.model.pray_times.PrayItem
import com.app.data.model.pray_times.PrayerTimes
import com.app.data.repository.AppRepo
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


private const val TAG = "AppUseCase"

class AppUseCase @Inject constructor(private val repo: AppRepo) {


    suspend fun getPrayerTimes(
        latitude: Double,
        longitude: Double
    ) = repo.getPrayerTimes(getCurrentDate(), latitude, longitude).map { pray ->


        (pray.body()?.data as ArrayList<PrayerTimes>).map { item ->
            item.apply {
                this.timingsArrayList.clear()
                this.timingsArrayList.add(PrayItem("Fajr", convertTime(item.timings?.Fajr)))
                this.timingsArrayList.add(PrayItem("Sunrise", convertTime(item.timings?.Sunrise)))
                this.timingsArrayList.add(PrayItem("Dhuhr", convertTime(item.timings?.Dhuhr)))
                this.timingsArrayList.add(PrayItem("Asr", convertTime(item.timings?.Asr)))
                this.timingsArrayList.add(PrayItem("Maghrib", convertTime(item.timings?.Maghrib)))
                this.timingsArrayList.add(PrayItem("Isha", convertTime(item.timings?.Isha)))
//                this.timingsArrayList.add(PrayItem("Imsak", convertTime(item.timings?.Imsak)))
//                this.timingsArrayList.add(PrayItem("Midnight", convertTime(item.timings?.Midnight)))
//                this.timingsArrayList.add(PrayItem("Firstthird", convertTime(item.timings?.Firstthird)))
//                this.timingsArrayList.add(PrayItem("Lastthird", convertTime(item.timings?.Lastthird)))
                this.timingsArrayList.filter { !it.time.isNullOrBlank() || !it.time.isNullOrEmpty() }
            }



            item.nextPrayingTime = getNextPrayingItem(item.timingsArrayList)
        }
        pray
    }
        .transformResponseData {
            emit(it)
        }


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

    private fun getNextPrayingItem(timingArrayList: ArrayList<PrayItem>): PrayItem? {
        val currentTime = Calendar.getInstance()

        Log.e(TAG, "getNextPrayingItem: current time ${currentTime.time}")
        val sortedPrayerItems = timingArrayList
            .filter { !it.time.isNullOrBlank() }
            .sortedBy { parseTime(it.time, currentTime) }


        for (prayerItem in sortedPrayerItems) {
            val prayerTime = parseTime(prayerItem.time, currentTime)
            Log.e(TAG, "getNextPrayingItem: pray time ${prayerTime.time} ")
            if (prayerTime.after(currentTime)) {
                return prayerItem
            }
        }
        return sortedPrayerItems.firstOrNull()
    }

    private fun parseTime(time: String?, currentTime: Calendar): Calendar {
        val calendar = Calendar.getInstance()
        time?.let {
            val pattern = "HH:mm"
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

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/M", Locale.US)
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    suspend fun getQiblaDirection(
        latitude: Double,
        longitude: Double
    ) = repo.getQiblaDirection(latitude, longitude).transformResponseData { emit(it) }
}