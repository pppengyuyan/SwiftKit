package com.mozhimen.abilitymk.restfulmk.annors.methods

/**
 * @ClassName Get
 * @Description TODO
 * @Author mozhimen
 * @Date 2021/9/23 21:24
 * @Version 1.0
 */
/**
 * @Get("/cities/all")
 * fun test(@Field("province") provinceId: Int)
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class GETMK(val value: String)