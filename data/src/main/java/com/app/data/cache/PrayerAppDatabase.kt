package com.app.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.data.model.pray_times.*


@Database(
    entities = [Timings::class, Date::class, Meta::class, PrayerTimes::class, PrayItem::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class PrayerAppDatabase : RoomDatabase() {
    abstract fun prayerTimesDao(): PrayerTimesDao
}
