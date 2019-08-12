package com.pmcc.basemodel.utils.requestutil.exception

/**
 * Created by Zaifeng on 2018/2/28.
 * 异常处理
 */

class ApiException : Exception {
    var code: Int = 0
    var msg: String? = null

    constructor(code: Int, msg: String) {
        this.code = code
        this.msg = msg
    }

    constructor(throwable: Throwable, code: Int) : super(throwable) {
        this.code = code
    }


}
