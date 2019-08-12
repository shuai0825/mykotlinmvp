package com.pmcc.basemodel.common

import android.app.Application
import android.content.Context
import android.util.Log
import com.pmcc.basemodel.utils.Preference
import kotlin.properties.Delegates

class BaseApp : Application() {
    companion object {
        var instance: BaseApp by Delegates.notNull()//notNull（引用、基本数据）,lateinit(引用)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

    }

}