package com.no_more.lib

import android.app.Application
import com.no_more.base.utils.BuildType

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        BuildType.isDebug = BuildConfig.DEBUG

    }
}