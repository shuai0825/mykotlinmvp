package com.pmcc.basemodel.utils.requestutil.interceptor


import com.pmcc.basemodel.utils.requestutil.observer.BaseObserver
import okhttp3.Interceptor
import okhttp3.Response

import java.io.IOException

/**
 * 文件下载拦截器
 */
class HttpResponseInterceptor(private val progressObserver: BaseObserver<*>) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        //包装响应体
        return response.newBuilder().body(ProgressResponseBody(response.body()!!, progressObserver)).build()
    }
}
