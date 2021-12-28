package com.warchaser.viewbinding.home.repository

import com.warchaser.libbase.network.bean.coroutine.Result
import com.warchaser.libbase.network.bean.service.coroutine.BaseRepository
import com.warchaser.viewbinding.network.NetworkRequest
import com.warchaser.viewbinding.network.bean.Body
import com.warchaser.viewbinding.network.bean.VIN
import kotlinx.coroutines.CoroutineScope

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

    suspend fun getVINArgus(successBlock: (suspend CoroutineScope.(Result.Success<Body<VIN>>) -> Unit)? = null, errorBlock: (suspend CoroutineScope.(String) -> Unit)? = null) =
        safeApiCallArgus({NetworkRequest.instance.testService.getVIN()}, successBlock, errorBlock)

    suspend fun getVINArgus1() = safeApiCallArgus { NetworkRequest.instance.testService.getVIN() }

}