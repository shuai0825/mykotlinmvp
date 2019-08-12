package com.pmcc.basemodel.utils.requestutil.schedulers

import com.trello.rxlifecycle3.LifecycleProvider
import com.trello.rxlifecycle3.android.ActivityEvent
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * retrofit的线程切换
 */
class SchedulerProvider private constructor() {
    //私有构造，不可以直接创建对象
    companion object {
        val instance: SchedulerProvider by lazy {
            SchedulerProvider()
        }

    }

    /**
     * 用于计算
     */
    fun computation(): Scheduler {
        return Schedulers.computation()
    }

    /**
     * 子线程
     */
    fun io(): Scheduler {
        return Schedulers.io()
    }

    /**
     * ui线程
     */
    fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    /**
     * io线程转化为ui线程
     */
    fun <T> applySchedulers(): ObservableTransformer<T, T> {

        return ObservableTransformer { upstream ->
            upstream.subscribeOn(io())
                .observeOn(ui())
        }
    }

    /**
     * 利用Rxlifecycle取消订阅，避免rxjava内存泄漏
     */
    fun <T> composeContext(lifecycle: LifecycleProvider<*>): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream -> upstream.compose(lifecycle.bindToLifecycle()) }
    }




}
