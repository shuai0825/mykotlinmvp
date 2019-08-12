package com.pmcc.basemodel.utils.requestutil.interceptor


import com.pmcc.basemodel.utils.requestutil.observer.BaseObserver
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

import java.io.IOException

/**
 * 文件上传拦截器
 */
class HttpReqeustInterceptor(private val progressObserver: BaseObserver<*>) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (null == request.body()) {
            return chain.proceed(request)
        }

        val build = request.newBuilder()
            .method(
                request.method(),
                ProgressRequestBody(
                    request.body()!!,
                    progressObserver
                )
            )
            .build()
        return chain.proceed(build)
    }

}
