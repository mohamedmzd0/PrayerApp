package com.app.di

import android.content.Context
import androidx.room.Room
import com.app.data.cache.PrayerAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDBModule {

    @Provides
    @Singleton
    fun provideRoomDB(@ApplicationContext context: Context): PrayerAppDatabase {
        return Room.databaseBuilder(
            context,
            PrayerAppDatabase::class.java,
            "pray_items_db"
        ).allowMainThreadQueries().build()
    }

}