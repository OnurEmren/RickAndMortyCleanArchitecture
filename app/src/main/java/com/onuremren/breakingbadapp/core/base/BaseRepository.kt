package com.onuremren.breakingbadapp.core.base

import com.onuremren.breakingbadapp.core.util.*
import com.onuremren.breakingbadapp.di.DispatchersProvider
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.UnknownError

abstract class BaseRepository constructor(
    private val networkHelper: NetworkHelper,
    private val dispatcher: DispatchersProvider
) {
    private suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): NetworkResource<T> {
        return withContext(dispatcher.io) {
            if (networkHelper.isNetworkConnected()) {
                try {
                    val result = apiCall.invoke()
                    when ((result as Response<*>).code()) {
                        in 200..300 -> NetworkResource.Success(result)
                        401 -> NetworkResource.Error(AuthError())
                        else -> NetworkResource.Error(UnknownError())
                    }
                } catch (throwable: Throwable) {
                    NetworkResource.Error(throwable)
                }
            } else {
                NetworkResource.Error(InternetConnectionError())
            }
        }
    }

    fun <T> baseRequestFlow(
        apiCall: suspend () -> Response<T>
    ) = flow {
        emit(Resource.Loading)
        val networkResponse = safeApiCall {
            apiCall.invoke()
        }
        val response = when (networkResponse) {
            is NetworkResource.Success -> Resource.Success(networkResponse.data?.body())
            is NetworkResource.Error -> {
                Resource.Failure(networkResponse.throwable)
            }
        }
        emit(response)
    }
}