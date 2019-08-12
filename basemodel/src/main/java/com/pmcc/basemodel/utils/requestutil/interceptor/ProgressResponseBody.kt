package com.pmcc.basemodel.utils.requestutil.interceptor


import com.pmcc.basemodel.utils.requestutil.observer.BaseObserver
import io.reactivex.annotations.Nullable
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*

import java.io.IOException

/**
 * 监听文件下载进度
 */
class ProgressResponseBody(private val mResponseBody: ResponseBody, private val progressObserver: BaseObserver<*>?) :
    ResponseBody() {
    private var mBufferedSource: BufferedSource? = null

    @Nullable
    override fun contentType(): MediaType? {
        return mResponseBody.contentType()
    }

    override fun contentLength(): Long {
        return mResponseBody.contentLength()
    }

    override fun source(): BufferedSource? {
        if (mBufferedSource == null) {
            mBufferedSource = Okio.buffer(source(mResponseBody.source()))
        }
        return mBufferedSource
    }


    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
             var currentSize = 0L
             var totalSize = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val nowSize = super.read(sink, byteCount)
                if (nowSize != -1L) {
                    currentSize += nowSize
                }else{
                    currentSize+=0
                }
                if (totalSize == 0L) {
                    //获得contentLength的值，后续不再调用
                    totalSize = contentLength()
                }
                //当前下载的百分比进度
                val progress = (currentSize * 100 / totalSize).toInt()
                if (progressObserver != null && progress >= 0) {
                    progressObserver.onProgressChanges(
                        currentSize, totalSize,
                        progress
                    )
                }
                return currentSize
            }
        }
    }

}
