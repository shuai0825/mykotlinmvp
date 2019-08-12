package com.pmcc.basemodel.mvp

import android.util.Log
import com.pmcc.basemodel.utils.requestutil.exception.ApiException
import com.pmcc.basemodel.utils.requestutil.observer.HttpObserver
import com.pmcc.basemodel.utils.requestutil.response.ResponseTransformer
import com.pmcc.basemodel.utils.requestutil.schedulers.SchedulerProvider
import com.pmcc.basemodel.utils.requestutil.service.UrlContract
import com.trello.rxlifecycle3.LifecycleProvider
import io.reactivex.schedulers.Schedulers.io
import java.io.*
import java.util.*

class Presenter(view: Contract.ViewIntfc) : BasePresenter<Contract.ViewIntfc>(view) {
    private var model: Model? = null

    init {
        model = Model()
    }


    /**
     * 中断网络请求
     */
    fun onDisView() {
        detachView()
        model = null
    }


    /**
     * 通用get请求
     *
     * @param url
     * @param parameter
     */
    override fun getDatas(url: String, parameter: Map<String, Any>, setTag: Boolean, lifecycle: LifecycleProvider<*>) {
        val myHttpObserver = MyHttpObserver<String>(setTag, url)
        model!!.getData(url, parameter, hashMapOf())
                .compose(ResponseTransformer.handleResult())//泛型转化开始
                .compose(SchedulerProvider.instance.applySchedulers())
                .compose(SchedulerProvider.instance.composeContext(lifecycle))//防止内存泄漏
                .doOnDispose { myHttpObserver.onCanceled() }.subscribe(myHttpObserver)

    }

    /**
     * 通用post请求
     *
     * @param url
     * @param parameter
     */
    override fun postDatas(url: String, parameter: Map<String, Any>, setTag: Boolean, lifecycle: LifecycleProvider<*>) {
        val myHttpObserver = MyHttpObserver<String>(setTag, url)
        model!!.postData(UrlContract.getNewVersion, parameter, hashMapOf())
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.instance.applySchedulers())
                .compose(SchedulerProvider.instance.composeContext(lifecycle))
                .doOnDispose { myHttpObserver.onCanceled() }.subscribe(myHttpObserver)
    }

    /**
     * 通用上传文件
     *
     * @param url
     */
    override fun postFiles(
            url: String,
            parameter: Map<String, Any>,
            fileList: List<File>,
            setTag: Boolean,
            lifecycle: LifecycleProvider<*>
    ) {
        val myHttpObserver = MyHttpObserver<String>(setTag, url)
        model!!.uploadData(url, parameter, hashMapOf(), fileList, myHttpObserver)
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.instance.applySchedulers())
                .compose(SchedulerProvider.instance.composeContext(lifecycle))
                .doOnDispose { myHttpObserver.onCanceled() }.subscribe(myHttpObserver)

    }

    /**
     * 通用下载传文件
     *
     * @param url
     */
    override fun downFile(url: String, file: File, setTag: Boolean, lifecycle: LifecycleProvider<*>) {
        val myHttpObserver = MyHttpObserver<String>(setTag, url)

        model!!.downData(url, hashMapOf(), myHttpObserver)
                .subscribeOn(io())
                .map { responseBody ->
                    writeFile(responseBody.byteStream(), file.absolutePath)
                    file.absolutePath
                }
                .compose(SchedulerProvider.instance.applySchedulers())
                .compose(SchedulerProvider.instance.composeContext(lifecycle))
                .doOnDispose { myHttpObserver.onCanceled() }
                .subscribe(myHttpObserver)

    }

    internal inner class MyHttpObserver<T>(setTag: Boolean, private val url: String) : HttpObserver<T>() {

        init {
            if (setTag) {
                setTag(url)
            } else {
                setTag(null)
            }
        }

        override fun onSuccess(t: T) {
            mView!!.getDataSuccess(url, t.toString())
        }

        override fun onFail(e: ApiException) {
            mView!!.getDataFail(url, "" + e.code, e.message)
        }


        override fun onProgress(bytesWritten: Long, contentLength: Long, progress: Int) {
            mView!!.getDataProcess(url, bytesWritten,contentLength,progress)

        }

    }

    private fun writeFile(inputString: InputStream, filePath: String) {
        val file = File(filePath)
        if (file.exists()) {
            file.delete()
        }

        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)

            val b = ByteArray(1024)

            var len: Int = 0
            while (len != -1) {
                fos.write(b, 0, len)
                len = inputString.read(b)
            }
            inputString.close()
            fos.close()

        } catch (e: FileNotFoundException) {
            // listener.onFail("FileNotFoundException");
        } catch (e: IOException) {
            // listener.onFail("IOException");
        }

    }

}


