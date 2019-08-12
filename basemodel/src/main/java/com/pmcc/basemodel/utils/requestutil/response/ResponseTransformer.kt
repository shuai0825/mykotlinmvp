package com.pmcc.basemodel.utils.requestutil.response


import com.pmcc.basemodel.utils.requestutil.exception.ApiException
import com.pmcc.basemodel.utils.requestutil.exception.CustomException

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.functions.Function

/**
 * 对返回的数据进行处理，区分异常的情况。
 */

object ResponseTransformer {

  //  fun <T> handleResult(): ObservableTransformer<BaseResponse<T>, T> {
    fun <T> handleResult(): ObservableTransformer<BaseResponse<T>, String> {
        return ObservableTransformer { upstream ->
            upstream
                .onErrorResumeNext(ErrorResumeFunction())
                .flatMap(ResponseFunction())
        }

    }


    /**
     * 非服务器产生的异常，比如本地无无网络请求，Json数据解析错误等等。
     *
     * @param <T>
    </T> */
    private class ErrorResumeFunction<T> : Function<Throwable, ObservableSource<out BaseResponse<T>>> {

        @Throws(Exception::class)
        override fun apply(throwable: Throwable): ObservableSource<out BaseResponse<T>> {
            return Observable.error(CustomException.handleException(throwable))
        }
    }

    /**
     * 服务其返回的数据解析
     * 正常服务器返回数据和服务器可能返回的exception
     *
     * @param <T>
    </T> */
   //private class ResponseFunction<T> : Function<BaseResponse<T>, ObservableSource<T>> {
   private class ResponseFunction<T> : Function<BaseResponse<T>, ObservableSource<String>> {
        @Throws(Exception::class)
        override fun apply(tResponse: BaseResponse<T>): ObservableSource<String> {
            val code = tResponse.resultCode
            val message = tResponse.resultDesc
            return if (code == 1) {
                Observable.just(tResponse.data.toString())
            } else {
                Observable.error(ApiException(code, message!!))
            }
        }
    }


}
