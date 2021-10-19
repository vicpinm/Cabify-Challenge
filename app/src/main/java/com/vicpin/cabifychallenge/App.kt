package com.vicpin.cabifychallenge

import android.app.Application
import com.vicpin.cabifychallenge.di.getAppModule
import org.koin.android.ext.android.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(getAppModule(this)))
    }
}
