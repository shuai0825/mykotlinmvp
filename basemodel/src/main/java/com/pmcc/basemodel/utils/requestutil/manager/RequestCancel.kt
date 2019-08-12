package com.pmcc.basemodel.utils.requestutil.manager

/**
 * 请求接口管理
 */
interface RequestCancel {
    /**
     * 取消请求
     */
    fun cancel()

    /**
     * 请求被取消
     */
    fun onCanceled()
}
