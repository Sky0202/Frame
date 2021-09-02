package com.sky.frame

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CustomApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }

}