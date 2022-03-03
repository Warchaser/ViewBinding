package com.warchaser.libbase.network.bean.service.coroutine

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
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

    private val mLogSwitch : Boolean = true

    protected open fun getConnectTimeOut() : Long = mConnectTimeOut

    protected open fun getReadTimeOut() : Long = mReadTimeOut

    protected open fun getBaseURL() : String = mBaseURL

    protected open fun getResponseInterceptor() : Interceptor? = null

    protected open fun logSwitch() : Boolean = mLogSwitch

    private val mClient : OkHttpClient
        get(){
            val builder = OkHttpClient.Builder()

            if(logSwitch()){
                val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger{
                    override fun log(message: String) {
                        onLogging(message)
                    }
                })

                logging.level = HttpLoggingInterceptor.Level.BODY

                builder.addInterceptor(logging)
            }

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

    protected open fun overrideOkHttpClient(builder : OkHttpClient.Builder){

    }

    fun <S> getService(serviceInterface : Class<S>) : S = Retrofit.Builder()
        .client(mClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
        .baseUrl(getBaseURL())
        .build()
        .create(serviceInterface)

}