package com.pmcc.mykotlinmvp

import android.content.Intent
import android.os.Environment
import android.util.Log
import com.pmcc.basemodel.common.BaseActivity
import com.pmcc.basemodel.mvp.Contract
import com.pmcc.basemodel.mvp.Presenter
import com.pmcc.basemodel.utils.requestutil.service.UrlContract
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*

class MainActivity : BaseActivity<Presenter>(), Contract.ViewIntfc {
    override val layoutResID: Int
        get() = R.layout.activity_main

    override fun createPresenter(): Presenter {
        return Presenter(this)
    }

    override fun initListener() {
        mainCeShi.setOnClickListener {
            startActivity(Intent(this,NetActivity::class.java))
        }
    }



    override fun getDataSuccess(url: String, data: String) {

    }

    override fun getDataFail(url: String, code: String, errorMessage: String?) {

    }


}
