package com.example.hellokitty.data.network

import okhttp3.Interceptor
import okhttp3.Response
import com.example.hellokitty.BuildConfig


class NetworkInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder().addHeader("x-api-key", BuildConfig.CAT_API_KEY).build()
        val response = chain.proceed(request)
        return response
    }
}