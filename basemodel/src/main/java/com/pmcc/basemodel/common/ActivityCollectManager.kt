package com.pmcc.basemodel.common

import android.app.Activity
import android.util.ArrayMap

class ActivityCollectManager private constructor() {
    companion object {
        private val mMaps: ArrayMap<String, Activity> by lazy {
            ArrayMap<String, Activity>()
        }
        val instance: ActivityCollectManager by lazy {
            ActivityCollectManager()
        }
    }

    /**
     * 添加activity
     */
    fun add(tag: String, activity: Activity) {
        mMaps[tag] = activity
    }

    /**
     * 移除activity
     */
    fun remove(tag: String) {
        if (mMaps.containsKey(tag)) {
            mMaps.remove(tag)
        }
    }

    /**
     * 移除所有activity
     */
    fun removeAll() {
        if (mMaps.isEmpty()) {
            return
        }
        val keySet = mMaps.keys
        for (key in keySet) {
            if (!mMaps[key]!!.isFinishing) {
                mMaps[key]!!.finish()
            }
        }
        mMaps.clear()
    }


}
