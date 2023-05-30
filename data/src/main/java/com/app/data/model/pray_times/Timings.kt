package com.app.data.model.pray_times

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "timings")
data class Timings(
    var Fajr: String? = null,
    var Sunrise: String? = null,
    var Dhuhr: String? = null,
    var Asr: String? = null,
    var Sunset: String? = null,
    var Maghrib: String? = null,
    var Isha: String? = null,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
)

@Entity(tableName = "date")
data class Date(
    var readable: String? = null,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
)

@Entity(tableName = "meta")
data class Meta(
    var timezone: String? = null,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
)

@Entity(tableName = "prayer_times")
data class PrayerTimes(
    var timings: Timings? = null,
    var date: Date? = null,
    var meta: Meta? = null,
    var timingsArrayList: ArrayList<PrayItem> = ArrayList(),
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
) {
    @Transient
    var nextPrayingTime: PrayItem? = null
}

@Entity(tableName = "pray_items")
data class PrayItem(
    var name: String?,
    var time: String?,
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
)
