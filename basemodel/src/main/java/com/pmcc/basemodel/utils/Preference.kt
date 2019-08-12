package com.pmcc.basemodel.utils

import android.content.Context
import android.content.SharedPreferences
import com.pmcc.basemodel.common.BaseApp
import com.pmcc.basemodel.common.ConstantConfig
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * shareprefre数据存储的工具类
 */
class Preference<T>(val name: String, val default: T) : ReadWriteProperty<Any?, T> {
    //自定义委托类实现ReadWriteProperty接口
    companion object {
        const val IS_LOGIN = "is_login"
        const val USER_GSON = "user_gson"
    }

    //延迟的委托代理
    val prefs: SharedPreferences by lazy {
        BaseApp.instance.applicationContext.getSharedPreferences(ConstantConfig.SP_NAME, Context.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getSpValue(name, default)
    }


    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        setSpValue(name, value)
    }

    private fun setSpValue(name: String, value: T) = with(prefs.edit()) {
        //可在else该处做序列化
        when (value) {
            is Int -> putInt(name, value)
            is Float -> putFloat(name, value)
            is Long -> putLong(name, value)
            is Boolean -> putBoolean(name, value)
            is String -> putString(name, value)
            else -> throw IllegalArgumentException("SharedPreference can't be save this type")
        }.apply()
    }


    private fun getSpValue(name: String, default: T): T = with(prefs) {
        //可在else该处做序列化
        val res: Any = when (default) {
            is Int -> getInt(name, default)
            is Float -> getFloat(name, default)
            is Long -> getLong(name, default)
            is Boolean -> getBoolean(name, default)
            is String -> getString(name, default)
            else -> throw IllegalArgumentException("SharedPreference can't be get this type")

        }
        return res as T
    }
}