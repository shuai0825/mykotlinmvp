package com.pmcc.basemodel.utils.requestutil.response


/**
 * 后台统一返回值，根据自己的项目修改
 * @param <T>
</T> */

data class BaseResponse<out T>(val resultCode: Int, val resultDesc: String, val data: T )

