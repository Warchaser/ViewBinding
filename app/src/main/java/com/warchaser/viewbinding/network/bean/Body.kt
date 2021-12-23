package com.warchaser.viewbinding.network.bean

data class Body<T>(
    val code: String,
    val description: String,
    val notFound: Boolean,
    val responseBody: T,
    val responseTime: String,
    val success: Boolean
)