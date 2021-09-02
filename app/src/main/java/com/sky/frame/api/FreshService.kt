package com.sky.frame.api

import androidx.lifecycle.LiveData
import cn.com.tcsl.fengxing.fresh.api.Response
import com.sky.frame.repo.Constants
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface FreshService {

    companion object {
        fun create(): FreshService {
            val logger = HttpLoggingInterceptor().apply { level = Level.BODY }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            var baseUrl = Constants.BASE_URL_HTTP

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .build()
                .create(FreshService::class.java)
        }
    }

    /**
     * create time : 2021/1/19 14:16
     * desc : 请求拦截器   替换url
     *
     * @author : zpw
     */
    class UrlInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val inputUrl = ""
            val request = chain.request()
            //从request中获取原有的HttpUrl实例oldHttpUrl
            val oldHttpUrl = request.url
            val newBaseUrl = inputUrl.toHttpUrl()
            //重建新的HttpUrl，修改需要修改的url部分
            val newFullUrl = oldHttpUrl
                .newBuilder()
                .scheme(newBaseUrl.scheme)
                .host(newBaseUrl.host)
                .port(newBaseUrl.port)
                .build()
            //重建这个request，通过builder.url(newFullUrl).build()；
            //然后返回一个response至此结束修改
            return chain.proceed(request.newBuilder().url(newFullUrl).build())
        }
    }

    @POST("login")
    fun login(@Body loginRequest: Any): LiveData<ApiResponse<Response<Any>>>


}
