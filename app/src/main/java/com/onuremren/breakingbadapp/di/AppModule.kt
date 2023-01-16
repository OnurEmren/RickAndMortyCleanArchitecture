package com.onuremren.breakingbadapp.di

import android.content.Context
import com.onuremren.breakingbadapp.BuildConfig
import com.onuremren.breakingbadapp.core.util.Constants
import com.onuremren.breakingbadapp.core.util.NetworkHelper
import com.onuremren.breakingbadapp.model.interceptor.Interceptor
import com.onuremren.breakingbadapp.network.ApiHelper
import com.onuremren.breakingbadapp.network.ApiHelperImpl
import com.onuremren.breakingbadapp.network.ApiService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    abstract fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper

    @Binds
    abstract fun provideDispatchers(dispatcherImpl: DispatchersImpl): DispatchersProvider

    companion object{
        @Provides
        @Named("BASE_URL")
        fun provideBaseUrl() = Constants.BASE_URL

        @Provides
        @Singleton
        fun provideApiKeyInterceptor(): Interceptor = Interceptor()

        @Provides
        @Singleton
        fun provideOkHttpClient(
            apiKeyAndLanguageInterceptor: Interceptor, @ApplicationContext context: Context
        ) = if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            OkHttpClient.Builder().addInterceptor(loggingInterceptor)
                .addInterceptor(apiKeyAndLanguageInterceptor)
                .build()
        } else {
            OkHttpClient.Builder().addInterceptor(apiKeyAndLanguageInterceptor).build()
        }


        @Provides
        @Singleton
        fun provideRetrofit(
            okHttpClient: OkHttpClient, @Named("BASE_URL") baseUrl: String
        ): Retrofit =
            Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(baseUrl)
                .client(okHttpClient).build()

        @Provides
        @Singleton
        fun provideApiService(retrofit: Retrofit): ApiService =
            retrofit.create(ApiService::class.java)

//        @Provides
//        @Singleton
//        fun provideResourceProvider(@ApplicationContext context: Context) =
//            ResourceProvider(context)

        @Provides
        @Singleton
        fun provideNetworkHelper(@ApplicationContext context: Context) = NetworkHelper(context)
    }


}