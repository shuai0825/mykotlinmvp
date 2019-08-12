package com.pmcc.basemodel.mvp

import com.trello.rxlifecycle3.LifecycleProvider

import java.io.File

/**
 * 该页面主要将present跟view绑定
 * @param <T>
</T> */
open class BasePresenter<T>(view: T) : Contract.PersenterIntfc {
    protected var mView: T? = null

    init {
        attachView(view)
    }

    /**
     * 绑定view
     * @param view
     */
    private fun attachView(view: T) {
        mView = view
    }

    /**
     * 分离view
     */
    fun detachView() {
        mView = null
    }

    override fun getDatas(url: String, parameter: Map<String, Any>, setTag: Boolean, lifecycle: LifecycleProvider<*>) {

    }

    override fun postDatas(url: String, parameter: Map<String, Any>, setTag: Boolean, lifecycle: LifecycleProvider<*>) {

    }

    override fun postFiles(
        url: String,
        parameter: Map<String, Any>,
        fileList: List<File>,
        setTag: Boolean,
        lifecycle: LifecycleProvider<*>
    ) {

    }

    override fun downFile(url: String, file: File, setTag: Boolean, lifecycle: LifecycleProvider<*>) {

    }
}
