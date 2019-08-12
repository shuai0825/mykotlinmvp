package com.pmcc.mykotlinmvp

import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import com.pmcc.basemodel.common.BaseActivity
import com.pmcc.basemodel.mvp.Contract
import com.pmcc.basemodel.mvp.Presenter
import com.pmcc.basemodel.utils.requestutil.manager.RequestManager
import com.pmcc.basemodel.utils.requestutil.service.UrlContract
import kotlinx.android.synthetic.main.activity_net.*
import kotlinx.coroutines.*

import java.io.File
import java.util.ArrayList
import java.util.HashMap

/**
 * mvp与网络封装请求demo
 * 1.设置请求tag，取消指定网络请求；注意设置tag后，如果网络没完成，页面销毁，要手动取消tag；
 * 不设置tag，页面销毁，直接取消网络请求
 *
 * 2.泛型的使用：在service中设置泛型，传递个ResponseTransformer（异常处理），再传给SchedulerProvider，
 * 最后传给MyHttpObserver
 *
 * 3.kotlin跟retrofit结合注意：请求参数的设置，get必须用QueryMap，其中Any、RequestBody，必须要使用
 * @JvmSuppressWildcards前缀，及@QueryMap parameter: Map<String, @JvmSuppressWildcards Any>
 *
 *
 * 4.进度的mvp:由于kotlin中的接口可以带方法低，因此可以使用view的接口中进度方法可以实现方法体
 */
class NetActivity : BaseActivity<Presenter>(), Contract.ViewIntfc {
    override val layoutResID: Int
        get() = R.layout.activity_net

    override fun createPresenter(): Presenter {

        return Presenter(this)
    }

    override fun initView() {

        Log.d("AA", "主线程协程后面代码执行，线程：${Thread.currentThread().name}")

    }
 


    override fun initListener() {
        netRequest.setOnClickListener { downFile() }
        //取消请求
        netStop.setOnClickListener {
            //请求时，设置tag，在页面关闭时，注意取消tag
            RequestManager.instance.cancelAll()
        }

    }


    private fun downFile() {
        val url =
            "http://imtt.dd.qq.com/16891/FC92B1B4471DE5AAD0D009DF9BF1AD01.apk?fsname=com.tencent.mobileqq_7.7.5_896.apk&csr=1bbd"
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "QQ.apk")
        mPresenter?.downFile(url, file, true, this)
    }

    /**
     * 上传文件
     */
    private fun postData() {
        val paraps = HashMap<String, Any>()
        paraps["shopId"] = "ff80808167148121016717a22dfe0095"
        val fileList = ArrayList<File>()
        fileList.add(File("/storage/emulated/0/DCIM/Screenshots/Screenshot_2019-05-28-10-20-29-026_com.zpjr.cunguan.png"))
        mPresenter?.postFiles(UrlContract.uploadHead, paraps, fileList, true, this)
    }

    /**
     *
     */
    private fun getData() {
        mPresenter?.getDatas(UrlContract.getNewVersion, hashMapOf(), false, this)
    }


    override fun getDataSuccess(url: String, data: String) {
        Log.d("%s", url + data)
    }

    override fun getDataFail(url: String, code: String, errorMessage: String?) {
        Log.d("%s", url + errorMessage)

    }

    override fun getDataProcess(url: String, bytesWritten: Long, contentLength: Long, progress: Int) {
        Log.d("%s", "$progress")


    }
}