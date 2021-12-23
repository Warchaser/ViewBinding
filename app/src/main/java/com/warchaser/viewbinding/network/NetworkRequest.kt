package com.warchaser.viewbinding.network

import com.warchaser.libbase.network.bean.service.coroutine.BaseRetrofitProvider
import com.warchaser.libcommonutils.NLog

class NetworkRequest private constructor(): BaseRetrofitProvider(){

    companion object{
        val instance : NetworkRequest by lazy {
            NetworkRequest()
        }
    }

    private val TAG : String = "NetworkRequest"

    val testService by lazy {
        getService(TestService::class.java)
    }

    override fun onLogging(msg: String) {
        when {
            msg.startsWith("{") -> {
                NLog.printJson(TAG, msg)
            }
            msg.startsWith("<!DOCTYPE html>") -> {
                NLog.printHtml(TAG, msg)
            }
            else -> {
                NLog.i(TAG, msg)
            }
        }
    }

    override fun getBaseURL(): String = "https://hc4-test.reachauto-mobility.com/"
}