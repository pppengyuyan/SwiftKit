package com.mozhimen.componentktest.netk.customs

import com.mozhimen.componentk.netk.NetKAsync
import com.mozhimen.componentk.netk.NetKCoroutine
import com.mozhimen.componentk.netk.NetKRxJava
import com.mozhimen.componentk.netk.customs.AsyncInterceptorLog
import com.mozhimen.componentk.netk.customs.AsyncFactory

/**
 * @ClassName ApiFactory
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2021/12/13 22:16
 * @Version 1.0
 */
object ApiFactory {
    private val _baseUrl = "https://api.caiyunapp.com/v2.5/cIecnVlovchAFYIk/"
    private val _baseUrl1 = "http://121.229.39.86:8123/"

    private val _netkAsync: NetKAsync = NetKAsync(_baseUrl, AsyncFactory(_baseUrl))
    private val _netkCoroutine: NetKCoroutine = NetKCoroutine(_baseUrl)
    private val _netkCoroutineTest: NetKCoroutine = NetKCoroutine(_baseUrl1)
    private val _netkRxJava: NetKRxJava = NetKRxJava(_baseUrl)

    init {
        //netkAsync.addIntercept(BizInterceptor())
        //netkAsync.addIntercept(RouteInterceptor())
        _netkAsync.addInterceptor(AsyncInterceptorLog())
        //netkAsync.addIntercept(RouteInterceptor())
        //netkRxJava.addIntercept(RouteInterceptor())
    }

    fun <T> createAsync(api: Class<T>): T {
        return _netkAsync.create(api)
    }

    fun <T> createCoroutine(api: Class<T>): T {
        return _netkCoroutine.create(api)
    }

    fun <T> createCoroutineTest(api: Class<T>): T {
        return _netkCoroutineTest.create(api)
    }

    fun <T> createRxJava(api: Class<T>): T {
        return _netkRxJava.create(api)
    }
}