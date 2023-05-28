package com.mohamed.prayerapp.app

import android.app.Application
import com.mohamed.prayerapp.BuildConfig
import com.app.data.utils.ConstantsApi
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        ConstantsApi.VARIANT_URL = BuildConfig.BASE_URL

    }
}