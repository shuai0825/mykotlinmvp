package com.pmcc.basemodel.common

import android.app.ActivityManager
import android.os.Bundle

import com.pmcc.basemodel.mvp.BasePresenter
import com.trello.rxlifecycle3.components.support.RxFragmentActivity

abstract class BaseActivity<T : BasePresenter<*>> : RxFragmentActivity() {
     var mPresenter: T? = null

    /**
     * 设置布局
     */
    abstract val layoutResID: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initConfig()
    }

    private fun initConfig() {
        ActivityCollectManager.instance.add(localClassName, this)
        mPresenter = createPresenter()
        setContentView(layoutResID)
        try {
            initData()
            initView()
            initListener()
        } catch (e: Exception) {
            //异常处理
        }

    }


    /**
     * 设置mvp
     */
    protected abstract fun createPresenter(): T



    /**
     * 初始化数据
     */
    @Throws(Exception::class)
    open fun initData() {

    }

    /**
     * 初始化界面
     */
    @Throws(Exception::class)
    open fun initView() {

    }

    /**
     * 设置监听
     */
    @Throws(Exception::class)
    open fun initListener() {

    }

    override fun onDestroy() {
        ActivityCollectManager.instance.remove(localClassName)
        if (mPresenter != null) {
            mPresenter!!.detachView()
            mPresenter = null
        }
        super.onDestroy()
    }
}
