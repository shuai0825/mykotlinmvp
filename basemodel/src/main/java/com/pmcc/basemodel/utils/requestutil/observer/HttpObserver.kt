package com.pmcc.basemodel.utils.requestutil.observer

import com.pmcc.basemodel.utils.requestutil.exception.ApiException


/**
 * 请求返回值
 *
 * @param <T>
</T> */
abstract class HttpObserver<T> : BaseObserver<T>() {

    override fun onNext(t: T) {
        super.onNext(t)
        onSuccess(t)
    }

    override fun onError(e: Throwable) {
        super.onError(e)
        if (e is ApiException) {
            onFail(e as ApiException)
        }

    }

    /**
     * 该方法主要用于baseobserver回调，直接使用调用onProgress
     *
     * @param bytesWritten
     * @param contentLength
     * @param progress
     */
    override fun onProgressChanges(bytesWritten: Long, contentLength: Long, progress: Int) {
        onProgress(bytesWritten, contentLength, progress)
    }

    //进度成功的回调
    abstract fun onSuccess(t: T)

    //进度失败回调
    abstract fun onFail(e: ApiException)

    //进度回调变化
    open fun onProgress(bytesWritten: Long, contentLength: Long, progress: Int) {}


}
