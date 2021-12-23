package com.warchaser.viewbinding.home.repository

import com.google.gson.JsonObject
import com.warchaser.libbase.network.bean.coroutine.onError
import com.warchaser.libbase.network.bean.coroutine.onResponse
import com.warchaser.libbase.network.bean.service.coroutine.BaseRepository
import com.warchaser.viewbinding.home.state.MVVMDemoUIState
import com.warchaser.viewbinding.network.NetworkRequest
import kotlinx.coroutines.flow.flow

class MVVMDemoRepository : BaseRepository(){

    suspend fun getVIN() = flow<MVVMDemoUIState<JsonObject>>{
        NetworkRequest.instance.getTestService().getVIN()
            .onResponse{ string ->
                emit(MVVMDemoUIState())
            }
            .onError {
                emit(MVVMDemoUIState())
            }
    }

}