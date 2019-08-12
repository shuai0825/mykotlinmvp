package com.pmcc.basemodel.utils.requestutil.observer


import android.text.TextUtils
import android.util.Log

import com.pmcc.basemodel.utils.requestutil.manager.RequestCancel
import com.pmcc.basemodel.utils.requestutil.manager.RequestManager
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * 为请求取消，添加标记
 * @param <T>
</T> */
abstract class BaseObserver<T> : Observer<T>, RequestCancel {

    /*请求标识*/
    private var mTag: String? = null

    /**
     * 是否已经处理
     *
     * @author ZhongDaFeng
     */
    val isDisposed: Boolean
        get() = if (TextUtils.isEmpty(mTag)) {
            true
        } else RequestManager.instance.isDisposed(mTag)

    override fun onNext(t: T) {
        if (!TextUtils.isEmpty(mTag)) {
            RequestManager.instance.remove(mTag)
        }
    }

    override fun onError(e: Throwable) {
        if (!TextUtils.isEmpty(mTag)) {
            RequestManager.instance.remove(mTag)
        }
    }

    abstract fun onProgressChanges(bytesWritten: Long, contentLength: Long, progress: Int)

    override fun onComplete() {
        if (!TextUtils.isEmpty(mTag) && !RequestManager.instance.isDisposed(mTag)) {
            cancel()
        }
    }

    override fun onSubscribe(d: Disposable) {
        if (!TextUtils.isEmpty(mTag)) {
            RequestManager.instance.add(mTag, d)
        }
    }


    override fun cancel() {
        if (!TextUtils.isEmpty(mTag)) {
            RequestManager.instance.cancel(mTag)
        }
    }

    override fun onCanceled() {
        if (!TextUtils.isEmpty(mTag)) {
            RequestManager.instance.cancel(mTag)
            Log.d("%s", "onCanceled2")
        }
    }

    /**
     * 设置标识请求的TAG
     *
     * @param tag
     */
    fun setTag(tag: String?) {
        this.mTag = tag
    }


}
