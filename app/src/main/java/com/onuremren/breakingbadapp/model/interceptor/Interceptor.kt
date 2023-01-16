package com.onuremren.breakingbadapp.model.interceptor

import com.onuremren.breakingbadapp.core.util.Constants
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class Interceptor : okhttp3.Interceptor {

    companion object {
        const val ENDPOINT = "characters"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        val originalHttp : HttpUrl =original.url

        val url = originalHttp.newBuilder()
            .addQueryParameter(ENDPOINT, Constants.ENDPOINT)
            .build()

        val requestBuilder: Request.Builder = original.newBuilder().url(url)
        val request: Request = requestBuilder.build()
        return chain.proceed(request)

    }
}