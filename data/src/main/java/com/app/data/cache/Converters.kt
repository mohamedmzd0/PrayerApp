package com.app.data.cache

import androidx.room.TypeConverter
import com.app.data.model.pray_times.Date
import com.app.data.model.pray_times.Meta
import com.app.data.model.pray_times.PrayItem
import com.app.data.model.pray_times.Timings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromTimings(timings: Timings): String {
        return gson.toJson(timings)
    }

    @TypeConverter
    fun toTimings(timingsString: String): Timings {
        val type = object : TypeToken<Timings>() {}.type
        return gson.fromJson(timingsString, type)
    }



    @TypeConverter
    fun fromTimingsList(value: List<Timings?>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toTimingsList(value: String?): List<Timings?> {
        val type = object : TypeToken<List<Timings?>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromDate(value: Date?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toDate(value: String?): Date? {
        val type = object : TypeToken<Date>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromMeta(value: Meta?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toMeta(value: String?): Meta? {
        val type = object : TypeToken<Meta>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromPrayItemList(value: ArrayList<PrayItem>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toPrayItemList(value: String?): ArrayList<PrayItem> {
        val listType = object : TypeToken<ArrayList<PrayItem>>() {}.type
        return gson.fromJson(value, listType)
    }
}
