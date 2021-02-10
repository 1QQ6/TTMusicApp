package com.wyq.ttmusicapp.common

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.IOException

/**
 * Created by D22434 on 2018/1/16.
 */
class MyGsonResponseBodyConverter<T> internal constructor(
    private val gson: Gson,
    private val adapter: TypeAdapter<T>
) :
    Converter<ResponseBody, T> {

    /**
     * 重写转换类
     *
     * @param value 巨坑。ResponseBody只能使用一次。使用完后会自动关闭，所以多次使用会报错 closed
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T {
        var response = value.string()
        println("GOSN:$response")
        return try {
            adapter.fromJson(response)
        } catch (e: Exception) {
            println("GOSN error:" + e.message)
            //解析处理错误
            if (response.substring(0, 17) == "MusicJsonCallback") {
                response = response.substring(response.indexOf("{"), response.lastIndexOf(")"))
                println(response)
            }
            adapter.fromJson(response)
        } finally {
            value.close()
        }
    }

}