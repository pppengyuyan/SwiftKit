package com.mozhimen.basick.utilk.exts

import com.mozhimen.basick.utilk.java.datatype.regular.UtilKFilter


/**
 * @ClassName ExtsFilter
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2022/12/19 11:18
 * @Version 1.0
 */
fun String.filterNumber(): String {
    return UtilKFilter.outNumber(this)
}

fun String.filterAlphabet(): String {
    return UtilKFilter.outAlphabet(this)
}

fun String.filterChinese(): String {
    return UtilKFilter.outChinese(this)
}

fun String.filter(): String {
    return UtilKFilter.outNAC(this)
}

fun String.filterLength(endIndex: Int): String {
    return UtilKFilter.outLength(this, endIndex)
}