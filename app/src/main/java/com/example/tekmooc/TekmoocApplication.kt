package com.example.tekmooc

import android.app.Application
import timber.log.Timber

class TekmoocApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}