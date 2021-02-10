package com.wyq.ttmusicapp.net

import androidx.annotation.MainThread
import com.apkfuns.logutils.LogUtils
import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.wyq.ttmusicapp.BuildConfig
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.common.MusicApplication
import com.wyq.ttmusicapp.common.MyGsonConverterFactory
import com.wyq.ttmusicapp.utils.NetworkUtils
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Schedulers.io
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Created by Roman on 2021/2/8
 */
class APIManager {

    companion object{
        private var mApiManager: APIManager? = null
        @Volatile
        private var mOkHttpClient: OkHttpClient? = null
        private const val CONNECT_TIMEOUT = 60L
        private const val READ_TIMEOUT = 10L
        private const val WRITE_TIMEOUT = 10L

        //获取ApiManager的单例
        fun getInstance(): APIManager? {
            if (mApiManager == null) {
                synchronized(APIManager::class.java) {
                    if (mApiManager == null) {
                        mApiManager = APIManager()
                    }
                }
            }
            return mApiManager
        }


    }

    /**
     * 获取Service
     *
     * @param clazz
     * @param <T>
     * @return
    </T> */
    fun <T> create(clazz: Class<T>,baseUrl:String):T{
        val retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .client(getOkHttpClient())
            .addConverterFactory(MyGsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        return retrofit.create(clazz)
    }

    private fun getOkHttpClient(): OkHttpClient {
        if (mOkHttpClient == null) {
            // Cookie 持久化
            val cookieJar: ClearableCookieJar = PersistentCookieJar(
                SetCookieCache(),
                SharedPrefsCookiePersistor(MusicApplication.context)
            )
            synchronized(APIManager::class.java) {
                val cache = Cache(
                    File(
                        MusicApplication.context?.cacheDir,
                        "HttpCache"
                    ), 1024 * 1024 * 100
                )
                if (mOkHttpClient == null) {
                    val builder = OkHttpClient.Builder()

                    //设置http 日志拦截
                    if (BuildConfig.DEBUG) {
                        val httpLogging = HttpLoggingInterceptor()
                        httpLogging.setLevel(HttpLoggingInterceptor.Level.BODY)
                        builder.addInterceptor(httpLogging)
                    }
                    mOkHttpClient = builder.cache(cache)
                        .connectTimeout(
                            CONNECT_TIMEOUT,
                            TimeUnit.SECONDS
                        )
                        .readTimeout(
                            READ_TIMEOUT,
                            TimeUnit.SECONDS
                        )
                        .writeTimeout(
                            WRITE_TIMEOUT,
                            TimeUnit.SECONDS
                        )
                        .cookieJar(cookieJar)
                        .build()
                }
            }
        }
        return mOkHttpClient!!
    }

    fun <T> request(service: Observable<T>,result:RequestCallBack<T>){
        if (!NetworkUtils.checkNetworkConnect(MusicApplication.context)!!) {
            result.error(MusicApplication.context?.getString(R.string.error_connection))
            return
        }
        service.subscribeOn(io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :Observer<T> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: T) {
                    result.success(t)
                }

                override fun onError(e: Throwable) {
                    if (e is HttpException) {
                        var errorInfo = ""
                        try {
                            errorInfo = e.response()!!.errorBody()!!.string()
                            if (errorInfo.startsWith("{")) {
                                val jsonObject = JSONObject(errorInfo)
                                val error = jsonObject.getString("msg")
                                result.error(error)
                            } else {
                                result.error(errorInfo)
                            }
                        } catch (e1: IOException) {
                            e1.printStackTrace()
                            LogUtils.d("ApiManager", "errorInfo=$errorInfo")
                            if (errorInfo.contains("LeanEngine")) {
                                errorInfo = "默认LeanEngine服务器请求超出限制\n请稍后再试！"
                            }
                            if (e1 is JSONException) {
                                result.error(errorInfo)
                            } else {
                                result.error(MusicApplication.context?.getString(R.string.error_connection))
                            }
                        } catch (e1: JSONException) {
                            e1.printStackTrace()
                            LogUtils.d("ApiManager", "errorInfo=$errorInfo")
                            if (errorInfo.contains("LeanEngine")) {
                                errorInfo = "默认LeanEngine服务器请求超出限制\n请稍后再试！"
                            }
                            result.error(errorInfo)
                        }
                    } else {
                        if (e.message == null) {
                            result.error(MusicApplication.context?.getString(R.string.error_connection))
                        } else {
                            result.error(e.message)
                        }
                    }
                }
            })
    }



}