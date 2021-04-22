package com.mozhimen.swiftmk.utils.http

import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * @ClassName HttpUtil
 * @Description TODO
 * @Author mozhimen
 * @Date 2021/4/15 11:19
 * @Version 1.0
 */
interface HttpCallbackListener {
    fun onFinish(response: String)
    fun onError(e: Exception)
}

object HttpUtil {

    /**
     * 作用: 网络请求回调
     * 用法:
     * HttpUtil.sendHttpRequest(address, object : HttpCallbackListener {
     *  override fun onFinish(response: String) {//得到服务器返回的具体内容}
     *  override fun onError(e: Exception) {//在这里对异常情况进行处理} })
     */
    fun sendHttpRequest(address: String, listener: HttpCallbackListener) {
        var connection: HttpURLConnection? = null
        try {
            val response = StringBuilder()
            val url = URL(address)
            connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 8000
            connection.readTimeout = 8000
            val input = connection.inputStream
            val reader = BufferedReader(InputStreamReader(input))
            reader.use {
                reader.forEachLine {
                    response.append(it)
                }
            }
            listener.onFinish(response.toString())//回调onFinish()方法
        } catch (e: Exception) {
            e.printStackTrace()
            listener.onError(e)//回调onError()方法
        } finally {
            connection?.disconnect()
        }
    }

    /**
     * 作用: 网络请求回调
     * 用法:
     * HttpUtil.sendOkHttpRequest(address, object : Callback {
     *  override fun onResponse(call: Call, response: Response) {
     *  //得到服务器返回的具体内容val responseData = response.body?.string()}
     *  override fun onFailure(call: Call, e: IOException) {//在这里对异常情况进行处理} })
     */
    fun sendOkHttpRequest(address: String,callback:Callback){
        val client=OkHttpClient()
        val request= Request.Builder()
            .url(address)
            .build()
        client.newCall(request).enqueue(callback)
    }
}