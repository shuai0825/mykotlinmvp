package com.pmcc.basemodel.utils.requestutil.service


/**
 * ip请求接口合集
 */
object UrlContract {

    val base = "http://wx.pmcc.com.cn:"//ip
    val ip = base + "8893"//端口号
    val key = "$ip/app/"
    val login = key + "member/login"//登录
    val getNewVersion = key + "appVersion/getNewVersion"//版本更新
    val uploadHead = key + "shopInfo/uploadImage"//上传头像
    val uploadgoods = key + "appGoodsManagement/uploadGoods"//上传商品封面图


}
