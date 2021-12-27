package com.warchaser.viewbinding.home.repository

import com.warchaser.libbase.network.bean.coroutine.Result
import com.warchaser.libbase.network.bean.service.coroutine.BaseRepository
import com.warchaser.viewbinding.network.NetworkRequest
import com.warchaser.viewbinding.network.bean.Body
import com.warchaser.viewbinding.network.bean.VIN
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.suspendCoroutine

class MVVMDemoRepository : BaseRepository(){

//    suspend fun getVIN() = flow<Body<VIN>>{
//        NetworkRequest.instance.testService.getVIN()
//            .onResponse{ body ->
//                emit(body)
//            }
//            .onError { error ->
////                emit(error)
//            }
//    }

    suspend fun getVIN() =
        safeApiCallResponse { NetworkRequest.instance.testService.getVIN() }


}