package com.onuremren.breakingbadapp.core.util

sealed class NetworkResource<T>(val data: T?, val throwable: Throwable?) {
    class Success<T>(data: T?) : NetworkResource<T>(data, null)
    class Error<T>(throwable: Throwable?) : NetworkResource<T>(null, throwable)
}

class AuthError : Throwable() {

}

class UnknownError : Throwable() {

}

class InternetConnectionError : Throwable() {

}