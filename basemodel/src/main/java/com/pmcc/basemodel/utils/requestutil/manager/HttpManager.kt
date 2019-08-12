package com.pmcc.basemodel.utils.requestutil.manager

import android.util.Log
import com.pmcc.basemodel.utils.requestutil.interceptor.HttpReqeustInterceptor
import com.pmcc.basemodel.utils.requestutil.interceptor.HttpResponseInterceptor
import com.pmcc.basemodel.utils.requestutil.observer.BaseObserver
import com.pmcc.basemodel.utils.requestutil.service.UrlContract
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class HttpManager private constructor(){
    //双重校验锁式
    companion object {
        private var mClient: OkHttpClient.Builder? = null
        private var builder: Retrofit.Builder? = null
        val instance: HttpManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            init()//用lazy修饰，在第一次访问时，访问该句；其他的只访问最后一句
            HttpManager()
        }
        private fun init() {
            Log.d("%s","加载");
            initOkHttp()
            initRetrofit()
        }

        private fun initRetrofit() {
            builder = Retrofit.Builder()
                .baseUrl(UrlContract.ip)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
        }

        private fun initOkHttp() {
            mClient = OkHttpClient.Builder()
            mClient!!.connectTimeout(15, TimeUnit.SECONDS)//15S连接超时
                .readTimeout(20, TimeUnit.SECONDS)//20s读取超时
                .writeTimeout(20, TimeUnit.SECONDS)//20s写入超时
                .retryOnConnectionFailure(true)//错误重连
        }

    }
    //获取对应的Service
    fun <T> createService(service: Class<T>): T {
        return builder!!.client(mClient!!.build()).build().create(service)
    }

    //获取对应的上传Service(带响应进度)
    fun <T> createReqeustService(service: Class<T>, progressObserver: BaseObserver<*>): T {
        mClient!!.addInterceptor(HttpReqeustInterceptor(progressObserver))
        return builder!!.client(mClient!!.build()).build().create(service)
    }

    //获取对应的下载Service(带响应进度)
    fun <T> createResponseService(service: Class<T>, progressObserver: BaseObserver<*>): T {
        mClient!!.addInterceptor(HttpResponseInterceptor(progressObserver))
        return builder!!.client(mClient!!.build()).build().create(service)
    }


}