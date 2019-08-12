package com.pmcc.basemodel.utils.requestutil.manager

import androidx.collection.ArrayMap
import io.reactivex.disposables.Disposable

/**
 * http请求连接管理类
 */
class RequestManager {

    companion object {
        //第一次访问时初始化
        private val mMaps: ArrayMap<String, Disposable> by lazy {
            ArrayMap<String, Disposable>()
        }
        //单例
        val instance: RequestManager by lazy {
            RequestManager()
        }
    }


    /**
     * 添加tag
     */
    fun add(tag: String?, disposable: Disposable) {
        mMaps[tag] = disposable
    }

    /**
     * 移除tag
     * @param tag
     */
    fun remove(tag: String?) {
        if (!mMaps.isEmpty) {
            mMaps.remove(tag)
        }
    }

    /**
     * 移除指定tag
     * @param tag
     */
    fun cancel(tag: String?) {
        if (mMaps.isEmpty) {
            return
        }
        if (mMaps[tag] == null) {
            return
        }
        if (!mMaps[tag]!!.isDisposed) {
            mMaps[tag]!!.dispose()
        }
        mMaps.remove(tag)
    }


    /**
     * 移除所有的请求连接
     */
    fun cancelAll() {
        if (mMaps.isEmpty) {
            return
        }
        //遍历取消请求
        var disposable: Disposable?
        val keySet = mMaps.keys
        for (key in keySet) {
            disposable = mMaps[key]
            if (!disposable!!.isDisposed) {
                disposable.dispose()
            }
        }
        mMaps.clear()
    }

    fun isDisposed(tag: String?): Boolean {
        return if (mMaps.isEmpty || mMaps[tag] == null) true else mMaps[tag]!!.isDisposed
    }


}
