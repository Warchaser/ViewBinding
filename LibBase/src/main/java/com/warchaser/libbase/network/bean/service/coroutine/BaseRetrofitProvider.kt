package com.warchaser.libbase.network.bean.service.coroutine

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

abstract class BaseRetrofitProvider {

    private val mConnectTimeOut : Long = 30

    private val mReadTimeOut : Long = 30

    private val mBaseURL : String = "https://www.fakeurl.com"

    protected open fun getConnectTimeOut() : Long = mConnectTimeOut

    protected open fun getReadTimeOut() : Long = mReadTimeOut

    protected open fun getBaseURL() : String = mBaseURL

    protected open fun getResponseInterceptor() : Interceptor? = null

    private val mClient : OkHttpClient
        get(){
            val builder = OkHttpClient.Builder()
            val logging = HttpLoggingInterceptor {
                onLogging(it)
            }

            logging.level = HttpLoggingInterceptor.Level.BODY

            builder.addInterceptor(logging)
            val responseInterceptor = getResponseInterceptor()
            responseInterceptor?.run {
                builder.addNetworkInterceptor(this)
            }
            builder.connectTimeout(getConnectTimeOut(), TimeUnit.SECONDS)
            builder.readTimeout(getReadTimeOut(), TimeUnit.SECONDS)

            overrideOkHttpClient(builder)

            return builder.build()
        }

    protected abstract fun onLogging(msg : String)

    protected abstract fun overrideOkHttpClient(builder : OkHttpClient.Builder)

    fun <S> getService(serviceInterface : Class<S>) : S = Retrofit.Builder()
        .client(mClient)
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(getBaseURL())
        .build()
        .create(serviceInterface)

}