package com.mozhimen.basick.utilk.device

import android.util.Log
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/**
 * @ClassName UtilKDate
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/1/15 14:08
 * @Version 1.0
 */
object UtilKDate {
    private const val TAG = "UtilKDate>>>>>"

    const val FORMAT_yyyyMMddHHmmssS = "yyyy-MM-dd HH:mm:ss S"

    const val FORMAT_yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss"

    const val FORMAT_yyyyMMddHHmm = "yyyy-MM-dd HH:mm"

    const val FORMAT_yyyyMMdd = "yyyy-MM-dd"

    const val FORMAT_HHmmss = "HH:mm:ss"

    const val FORMAT_HHmm = "HH:mm"

    const val FORMAT_mmss = "mm:ss"

    const val FORMAT_yyyy = "yyyy"

    const val FORMAT_MM = "MM"

    const val FORMAT_dd = "dd"

    const val FORMAT_HH = "HH"

    const val FORMAT_mm = "mm"

    const val FORMAT_ss = "ss"

    /**
     * 获取现在日期
     * @return Long
     */
    @JvmStatic
    fun getNowLong(): Long =
        date2Long(getNowDate())

    /**
     * 获取现在日期
     * @return Date
     */
    @JvmStatic
    fun getNowDate(): Date =
        Date()

    /**
     * 获取当前时间
     * @param locale Locale
     * @return String
     */
    @JvmStatic
    fun getNowStr(formatDate: String = FORMAT_yyyyMMddHHmmss, locale: Locale = Locale.CHINA): String =
        date2Str(getNowDate(), formatDate, locale)

    /**
     * 今日
     * @param locale Locale
     * @return String
     */
    @JvmStatic
    fun getTodayStr(locale: Locale = Locale.CHINA): String =
        date2Str(getNowDate(), FORMAT_yyyyMMdd, locale)

    /**
     * 今日Long
     * @param locale Locale
     * @return Long
     */
    @JvmStatic
    fun getTodayLong(locale: Locale = Locale.CHINA): Long =
        str2Long(getTodayStr(locale), FORMAT_yyyyMMdd)

    /**
     * 获得Format
     * @param formatDate String
     * @param locale Locale?
     * @return SimpleDateFormat
     */
    @JvmStatic
    fun getSdf(
        formatDate: String, locale: Locale = Locale.CHINA
    ): SimpleDateFormat =
        SimpleDateFormat(formatDate, locale)

    /**
     * date转long
     * @param date Date
     * @return Long
     */
    fun date2Long(date: Date): Long {
        return date.time
    }

    /**
     * long转date
     * @param date Long
     * @return Date
     */
    fun long2Date(date: Long): Date {
        return Date(date)
    }

    /**
     * 日期转字符串
     * @param date Date
     * @param formatDate String
     * @param locale Locale? 时区
     * @return String
     */
    @JvmStatic
    fun date2Str(
        date: Date, formatDate: String, locale: Locale = Locale.CHINA
    ): String {
        return getSdf(formatDate, locale).format(date)
    }

    /**
     * 字符串转日期
     * @param dateStr String
     * @param formatDate String
     * @param locale Locale?
     * @return Date
     */
    @JvmStatic
    fun str2Date(
        dateStr: String, formatDate: String, locale: Locale = Locale.CHINA
    ): Date {
        return getSdf(formatDate, locale).parse(dateStr) ?: kotlin.run {
            Log.e(TAG, "str2Date Exception time format fail!")
            throw Exception("time format fail!")
        }
    }

    /**
     * 长整型转字符串
     * @param date Long
     * @param formatDate String
     * @param locale Locale?
     * @return String
     */
    @JvmStatic
    fun long2Str(
        date: Long, formatDate: String, locale: Locale = Locale.CHINA
    ): String {
        return getSdf(formatDate, locale).format(date)
    }

    /**
     * String转long
     * @param dateStr String
     * @param formatDate String
     * @param locale Locale
     * @return Long
     */
    @JvmStatic
    fun str2Long(
        dateStr: String, formatDate: String, locale: Locale = Locale.CHINA
    ): Long {
        return date2Long(str2Date(dateStr, formatDate, locale))
    }

    /**
     * 时间比较
     * @param date1 Date
     * @param date2 Date
     * @return Int -1: date2 larger, 1: date1 larger, 0: same
     */
    @JvmStatic
    fun dateCompare(date1: Date, date2: Date): Int =
        date1.compareTo(date2)

    /**
     * 时间比较
     * @param date1 String
     * @param date2 String
     * @param formatDate String
     * @return Int
     */
    @JvmStatic
    fun dateCompare(date1: String, date2: String, formatDate: String) =
        dateCompare(str2Date(date1, formatDate), str2Date(date2, formatDate))
}