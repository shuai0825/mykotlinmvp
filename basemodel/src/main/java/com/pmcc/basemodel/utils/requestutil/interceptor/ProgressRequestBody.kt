package com.pmcc.basemodel.utils.requestutil.interceptor


import com.pmcc.basemodel.utils.requestutil.observer.BaseObserver
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*

import java.io.IOException

/**
 * 监听文件上传进度
 */
class ProgressRequestBody(//实际的待包装请求体
    private val mRequestBody: RequestBody, //进度回调接口
    private val progressObserver: BaseObserver<*>?
) : RequestBody() {
    //包装完成的BufferedSink
    private var mSink: BufferedSink? = null

    override fun contentType(): MediaType? {
        return mRequestBody.contentType()
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        try {
            return mRequestBody.contentLength()
        } catch (e: IOException) {
            e.printStackTrace()
            return -1
        }

    }

    @Throws(IOException::class)
    override fun writeTo(s: BufferedSink) {
        if (mSink == null) {
            mSink = Okio.buffer(CountingSink(s))
        }
        //写入
        mRequestBody.writeTo(mSink!!)
        //必须调用flush，否则最后一部分数据可能不会被写入
        mSink!!.flush()
    }


    private fun CountingSink(sink: Sink): Sink {
        return object : ForwardingSink(sink) {
            //当前写入字节数
            internal var currentSize = 0L
            //总字节长度，避免多次调用contentLength()方法
            internal var totalSize = 0L

            @Throws(IOException::class)
            override fun write(source: Buffer, byteCount: Long) {
                super.write(source, byteCount)
                if (totalSize == 0L) {
                    //获得contentLength的值，后续不再调用
                    totalSize = contentLength()
                }
                //增加当前写入的字节数
                currentSize += byteCount
                //当前上传的百分比进度
                val progress = (currentSize * 100 / totalSize).toInt()
                progressObserver?.onProgressChanges(
                    currentSize,
                    totalSize, progress
                )
            }
        }
    }

}
