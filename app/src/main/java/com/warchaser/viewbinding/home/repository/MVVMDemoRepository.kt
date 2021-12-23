package com.warchaser.viewbinding.home.repository

import com.warchaser.libbase.network.bean.coroutine.onError
import com.warchaser.libbase.network.bean.coroutine.onResponse
import com.warchaser.libbase.network.bean.service.coroutine.BaseRepository
import com.warchaser.viewbinding.home.state.MVVMDemoUIState
import com.warchaser.viewbinding.network.NetworkRequest
import com.warchaser.viewbinding.network.bean.Body
import com.warchaser.viewbinding.network.bean.VIN
import kotlinx.coroutines.flow.flow

class MVVMDemoRepository : BaseRepository(){

    suspend fun getVIN() = flow<MVVMDemoUIState<Body<VIN>>>{
        NetworkRequest.instance.testService.getVIN()
            .onResponse{ body ->
                body.responseBody
                emit(MVVMDemoUIState())
            }
            .onError {
                emit(MVVMDemoUIState())
            }
    }

}