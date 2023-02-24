package com.mozhimen.basick.utilk.exts

import com.mozhimen.basick.utilk.datatype.regular.UtilKVerifyUrl

/**
 * @ClassName ExtsKVerifyUrl
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/4/15 4:29
 * @Version 1.0
 */
/**
 * IP是否合法
 * @receiver String
 * @return Boolean
 */
fun String.isIPValid(): Boolean = UtilKVerifyUrl.isIPValid(this)

/**
 * 域名是否合法
 * @receiver String
 * @return Boolean
 */
fun String.isDoMainValid(): Boolean = UtilKVerifyUrl.isDoMainValid(this)

/**
 * 端口是否合法
 * @receiver String
 * @return Boolean
 */
fun String.isPortValid(): Boolean = UtilKVerifyUrl.isPortValid(this)

/**
 * url是都合法
 * @receiver String
 * @return Boolean
 */
fun String.isUrlValid(): Boolean = UtilKVerifyUrl.isUrlValid(this)