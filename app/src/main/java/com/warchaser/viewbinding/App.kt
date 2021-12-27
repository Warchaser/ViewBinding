package com.warchaser.viewbinding

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.warchaser.libbase.ui.gloading.Gloading
import com.warchaser.libcommonutils.CrashHandler
import com.warchaser.libcommonutils.NLog
import com.warchaser.viewbinding.gloading.GloadingAdapter

class App : MultiDexApplication(){

    override fun onCreate() {
        super.onCreate()
        NLog.initLogFile(this)
        Gloading.initDefault(GloadingAdapter())
        CrashHandler.getInstance().init()
        CrashHandler.OnHandleEvent { throwable ->
            NLog.printStackTrace("CrashHandler", throwable)
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }

}