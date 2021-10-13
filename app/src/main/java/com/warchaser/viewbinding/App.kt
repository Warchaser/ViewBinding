package com.warchaser.viewbinding

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.warchaser.libbase.ui.gloading.Gloading
import com.warchaser.viewbinding.gloading.GloadingAdapter

class App : MultiDexApplication(){

    override fun onCreate() {
        super.onCreate()
        Gloading.initDefault(GloadingAdapter())
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }

}