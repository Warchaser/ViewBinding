package com.warchaser.viewbinding.network

import com.google.gson.JsonObject
import com.warchaser.libbase.network.bean.coroutine.Response
import retrofit2.http.GET

interface TestService {

    /**
     * 获取VIN吗
     * */
    @GET("reachcloud-vsp-sdk-bff-auth/api/v1/vehicle/login/mini/vin_code=47ae3f207ddb8443033bfb8ac1e16099")
    suspend fun getVIN() : Response<JsonObject>

}