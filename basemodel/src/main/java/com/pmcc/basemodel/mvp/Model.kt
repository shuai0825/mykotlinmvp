package com.pmcc.basemodel.mvp

import com.pmcc.basemodel.utils.requestutil.manager.HttpManager
import com.pmcc.basemodel.utils.requestutil.observer.BaseObserver
import com.pmcc.basemodel.utils.requestutil.response.BaseResponse
import com.pmcc.basemodel.utils.requestutil.service.ApiService
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.File
import java.util.ArrayList
import java.util.HashMap

class Model : Contract.ModelIntfc {

    override fun getData(
        url: String,
        parameter: Map<String, Any>,
        header: Map<String, Any>
    ): Observable<BaseResponse<String>> {
        return HttpManager.instance.createService(ApiService::class.java).getUrl(url,HashMap<String, String>())
    }

    override fun postData(
        url: String,
        parameter: Map<String, Any>,
        header: Map<String, Any>
    ): Observable<BaseResponse<String>> {
        return HttpManager.instance.createService(ApiService::class.java).post(url, parameter, header)
    }

    override fun uploadData(
        url: String,
        parameter: Map<String, Any>,
        header: Map<String, Any>,
        fileList: List<File>,
        baseObserver: BaseObserver<*>
    ): Observable<BaseResponse<String>> {
        val parts = ArrayList<MultipartBody.Part>()
        for (file in fileList) {
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
            parts.add(body)
        }
        //处理参数
        val params = HashMap<String, RequestBody>()
        val textType = MediaType.parse("text/plain")
        for (key in parameter.keys) {
            params[key] = RequestBody.create(textType, parameter[key].toString())
        }
        return HttpManager.instance.createReqeustService(ApiService::class.java, baseObserver)
            .upload(url,params,parts)
    }

    override fun downData(
        url: String,
        header: Map<String, Any>,
        baseObserver: BaseObserver<*>
    ): Observable<ResponseBody> {
        return HttpManager.instance.createResponseService(ApiService::class.java, baseObserver)
            .download("12", url, hashMapOf())
    }
}