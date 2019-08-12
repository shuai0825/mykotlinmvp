package com.pmcc.basemodel.utils.requestutil.exception

/**
 * 自定义服务器错误,401或500等等
 *
 */
class ServerException(val code: Int, val msg: String) : RuntimeException()
