package com.pmcc.basemodel.utils.requestutil.service


import com.pmcc.basemodel.utils.requestutil.response.BaseResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * 封装请求通用接口
 */
interface ApiService {

    /**
     * GET 请求
     *
     * @param url       api接口url
     * @param parameter 请求参数map
     * @param header    请求头map
     * @return
     */
    @GET
     fun getUrl(@Url url: String,@QueryMap parameter: Map<String, @JvmSuppressWildcards Any>): Observable<BaseResponse<String>>


    /**
     * POST 请求
     *
     * @param url       api接口url
     * @param parameter 请求参数map
     * @param header    请求头map
     * @return
     */
    @FormUrlEncoded
    @POST
    fun post(@Url url: String, @FieldMap parameter: Map<String, @JvmSuppressWildcards Any>, @HeaderMap header: Map<String, @JvmSuppressWildcards Any>): Observable<BaseResponse<String>>

    /**
     * 多文件上传，包含请求参数的
     *
     * @param url
     * @param map，请求参数
     * @param fileList（可以单个上传）
     * @return
     * @Multipart 文件上传注解 multipart/form-data
     */

    @Multipart
    @POST
    fun upload(@Url url: String, @PartMap map: Map<String, @JvmSuppressWildcards RequestBody>,  @Part fileList: List<MultipartBody.Part>): Observable<BaseResponse<String>>

    /**
     * 多文件上传,另种方式跟上传方式upload一致
     *
     * @param url
     * @param map
     * @return
     */
    @Multipart
    @POST
    fun uploadBody(@Url url: String, @PartMap map: Map<String, @JvmSuppressWildcards RequestBody>): Observable<BaseResponse<String>>

    /**
     * 多文件上传,该方法尚未测试
     *
     * @param url       api接口url
     * @param parameter 请求接口参数
     * @param header    请求头map
     * @param fileList  文件列表,上传单个，表示当个上传
     * @return
     * @Multipart 文件上传注解 multipart/form-data
     */
    @Multipart
    @POST
    fun uploadTest(@Url url: String, @PartMap parameter: Map<String, @JvmSuppressWildcards Any>, @HeaderMap header: Map<String, @JvmSuppressWildcards Any>, @Part fileList: List<MultipartBody.Part>): Observable<BaseResponse<String>>


    /**
     * 断点续传下载
     *
     * @param range  断点下载范围 bytes= start - end
     * @param url    下载地址
     * @param header 请求头map
     * @return
     * @Streaming 防止内容写入内存, 大文件通过此注解避免OOM
     */
    @Streaming
    @GET
    fun download(@Header("RANGE") range: String, @Url url: String, @HeaderMap header: Map<String, String>): Observable<ResponseBody>
}
