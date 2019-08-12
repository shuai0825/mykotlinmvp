package com.pmcc.basemodel.mvp


import com.pmcc.basemodel.utils.requestutil.observer.BaseObserver
import com.pmcc.basemodel.utils.requestutil.response.BaseResponse
import com.trello.rxlifecycle3.LifecycleProvider
import io.reactivex.Observable
import okhttp3.ResponseBody

import java.io.File

/**
 * Created by Zaifeng on 2018/3/1.
 */

class Contract {

    interface PersenterIntfc {
        fun getDatas(url: String, parameter: Map<String, Any>, setTag: Boolean, lifecycle: LifecycleProvider<*>)

        fun postDatas(url: String, parameter: Map<String, Any>, setTag: Boolean, lifecycle: LifecycleProvider<*>)

        fun postFiles(
            url: String,
            parameter: Map<String, Any>,
            fileList: List<File>,
            setTag: Boolean,
            lifecycle: LifecycleProvider<*>
        )

        fun downFile(url: String, file: File, setTag: Boolean, lifecycle: LifecycleProvider<*>)
    }

    interface ViewIntfc {
        fun getDataSuccess(url: String, data: String)
        fun getDataProcess(url: String, bytesWritten: Long, contentLength: Long, progress: Int){

        }

        fun getDataFail(url: String, code: String, errorMessage: String?)
    }


    interface ModelIntfc {
        fun getData(
            url: String,
            parameter: Map<String, Any>,
            header: Map<String, Any>
        ): Observable<BaseResponse<String>>

        fun postData(
            url: String,
            parameter: Map<String, Any>,
            header: Map<String, Any>
        ): Observable<BaseResponse<String>>

        fun uploadData(
            url: String,
            parameter: Map<String, Any>,
            header: Map<String, Any>,
            fileList: List<File>,
            baseObserver: BaseObserver<*>
        ): Observable<BaseResponse<String>>

        fun downData(url: String, header: Map<String, Any>, baseObserver: BaseObserver<*>): Observable<ResponseBody>
    }

}
