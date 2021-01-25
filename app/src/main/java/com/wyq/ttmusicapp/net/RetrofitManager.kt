package com.wyq.ttmusicapp.net

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Roman on 2021/1/26
 */
class RetrofitManager {

    private val BASE_URL = "http://47.111.233.78:8080/api/user/"

    companion object{

        fun <T> getService(url:String,service: Class<T>):T{
            return createRetrofit(url).create(service)
        }

        private fun createRetrofit(url: String):Retrofit{
            val level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY
            val loggingInterceptor = HttpLoggingInterceptor(object :HttpLoggingInterceptor.Logger{
                override fun log(message: String) {
                    Log.i("kotlin", "OkHttp: $message")
                }
            })
            loggingInterceptor.level = level

            val okHttpClientBuilder = OkHttpClient.Builder()
            okHttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS)
            okHttpClientBuilder.readTimeout(10, TimeUnit.SECONDS)
            okHttpClientBuilder.addInterceptor(loggingInterceptor)

            return Retrofit.Builder()
                            .baseUrl(url)
                            .client(okHttpClientBuilder.build())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
        }
    }
}