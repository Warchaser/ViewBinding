package com.warchaser.viewbinding.network

import com.warchaser.libbase.network.bean.coroutine.Result
import com.warchaser.viewbinding.network.bean.Body
import com.warchaser.viewbinding.network.bean.VIN
import retrofit2.Response
import retrofit2.http.GET

interface TestService {

    /**
     * 获取token
     * */
    @GET("reachcloud-vsp-sdk-bff-auth/api/v1/vehicle/login/mini/vin_code=47ae3f207ddb8443033bfb8ac1e16099")
    suspend fun getVIN() : Response<Body<VIN>>

    @GET("reachcloud-vsp-sdk-bff-auth/api/v1/vehicle/login/mini/vin_code=47ae3f207ddb8443033bfb8ac1e16099")
    suspend fun getVINResult() : Result<Body<VIN>>
}