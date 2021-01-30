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

    companion object{

        fun <T> getService(url:String,service: Class<T>):T{
            //根据你传的service来进行不同的请求
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
            //连接超时
            okHttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS)
            //读取超时
            okHttpClientBuilder.readTimeout(10, TimeUnit.SECONDS)
            //添加拦截器
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
            //创建retrofit对象
            return Retrofit.Builder()
                            .baseUrl(url)
                            .client(okHttpClientBuilder.build())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
        }
    }
}