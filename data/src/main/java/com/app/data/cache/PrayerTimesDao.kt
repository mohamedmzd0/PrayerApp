package com.app.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.data.model.pray_times.PrayerTimes

@Dao
interface PrayerTimesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrayerTimes(prayerTimes: List<PrayerTimes>)

    @Query("SELECT * FROM prayer_times")
    fun getAllPrayerTimes(): List<PrayerTimes>

    @Query("DELETE FROM prayer_times")
    fun deleteAll()

}
