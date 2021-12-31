package com.warchaser.libbase.network.bean.service.coroutine

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Streaming
import retrofit2.http.Url

interface StreamService {

    @Streaming
    @GET
    suspend fun downloadFile(@Url fileUrl: String): Response<Nothing>

    @Streaming
    @GET
    suspend fun downloadFile(@Url fileUrl: String, @Header("RANGE") range :String) : Response<Nothing>

}