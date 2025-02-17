package com.mozhimen.basick.utilk.view.bar

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.mozhimen.basick.elemk.cons.CVersionCode
import com.mozhimen.basick.utilk.content.activity.UtilKActivity
import com.mozhimen.basick.utilk.view.display.UtilKScreen
import com.mozhimen.basick.utilk.os.UtilKOS
import com.mozhimen.basick.utilk.res.UtilKRes
import com.mozhimen.basick.utilk.view.window.UtilKWindow

/**
 * @ClassName UtilKBar
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/7/17 17:29
 * @Version 1.0
 */
object UtilKStatusBar {
    private const val TAG = "UtilKStatusBar>>>>>"

    /**
     * Return the status bar's height.
     * @return Int
     */
    @JvmStatic
    fun getStatusBarHeight(): Int {
        val dimensionId = UtilKRes.getSystemResources().getIdentifier("status_bar_height", "dimen", "android")
        return if (dimensionId != 0) UtilKRes.getDimensionPixelSize(dimensionId, UtilKRes.getSystemResources()) else 0
    }

    /**
     * 获取状态栏高度2
     * 优点: 依赖于WMS,是在界面构建后根据View获取的,所以高度是动态的
     * 缺点: 在Activity的回调方法onWindowFocusChanged()执行后,才能得到预期的结果
     * @param activity Activity
     * @return Int
     */
    @JvmStatic
    fun getStatusBarHeight(activity: Activity): Int {
        val rect = Rect()
        UtilKWindow.getDecorView(activity).getWindowVisibleDisplayFrame(rect)
        return rect.top
    }

    /**
     * 获取状态栏高度1
     * 优点: 不需要在Activity的回调方法onWindowFocusChanged()执行后才能得到结果
     * 缺点: 不管你是否设置全屏模式,或是不是显示状态栏,高度是固定的;因为系统资源属性是固定的,真实的,不管你是否隐藏(隐藏或显示),他都在那里
     * @param isCheckFullScreen Boolean 是否把全屏纳入考虑范围, 置true, 全屏返回0
     * @return Int
     */
    @JvmStatic
    fun getStatusBarHeight(isCheckFullScreen: Boolean): Int {
        if (isCheckFullScreen && UtilKScreen.isFullScreen()) return 0
        return getStatusBarHeight()
    }

    /**
     * 状态栏是否可见
     * @param context Context
     * @return Boolean
     */
    @JvmStatic
    fun isStatusBarVisible(context: Context): Boolean {
        val activity = UtilKActivity.getActivityByContext(context, true) ?: return true
        return try {
            UtilKWindow.get(activity).attributes.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN == 0
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            true
        }
    }

    /**
     * 隐藏状态栏
     * @param activity Activity
     */
    @JvmStatic
    fun hideStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= CVersionCode.V_23_6_M) {
            UtilKWindow.getDecorView(activity).systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    /**
     * 设置Miui状态栏字符
     * @param activity Activity
     * @param isDark Boolean
     */
    @JvmStatic
    fun setStatusBarFontIcon_MiuiUI(activity: Activity, isDark: Boolean) {
        if (Build.VERSION.SDK_INT > CVersionCode.V_23_6_M) {
            setStatusBarFontIcon_CommonUI(activity, isDark)
        } else {
            if (UtilKOS.isMIUILarger6()) {
                setStatusBarFontIcon_MiuiUILarger6(activity, isDark)
            } else {
                Log.e(TAG, "setStatusBarFontIcon_MiuiUI: dont support this miui version")
            }
        }
    }

    /**
     * 设置MIUI<6样式状态栏字符
     * @param activity Activity
     * @param isDark Boolean
     */
    @JvmStatic
    @SuppressLint("PrivateApi")
    fun setStatusBarFontIcon_MiuiUILarger6(activity: Activity, isDark: Boolean) {
        try {
            val window = UtilKWindow.get(activity)
            val layoutParams = Class.forName("android.view.MiuiWindowManager${'$'}LayoutParams")
            val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            val darkModeFlag = field.getInt(layoutParams)
            val extraFlagMethod = window.javaClass.getMethod("setExtraFlags", Int::class.java, Int::class.java)
            //状态栏亮色且黑色字体
            extraFlagMethod.invoke(window, if (isDark) darkModeFlag else 0, darkModeFlag)
        } catch (e: Exception) {
            Log.e(TAG, "setStatusBarFontIcon_MiuiUILarger6: ${e.message}")
            e.printStackTrace()
        }
    }

    /**
     * 设置原生状态栏字符
     * @param activity Activity
     * @param isDark Boolean
     */
    @JvmStatic
    fun setStatusBarFontIcon_CommonUI(activity: Activity, isDark: Boolean) {
        if (Build.VERSION.SDK_INT >= CVersionCode.V_23_6_M) {
            val window: Window = UtilKWindow.get(activity)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            val flag: Int = if (isDark)
                UtilKWindow.getDecorView(window).systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            else
                UtilKWindow.getDecorView(window).systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            UtilKWindow.getDecorView(window).systemUiVisibility = flag
        }
    }

    /**
     * 设置Flyme样式状态栏字符
     * @param activity Activity
     * @param isDark Boolean
     */
    @JvmStatic
    fun setStatusBarFontIcon_FlymeUI(activity: Activity, isDark: Boolean) {
        try {
            val window = UtilKWindow.get(activity)
            val layoutParams = window.attributes
            val darkFlag = WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
            val meizuFlags = WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
            darkFlag.isAccessible = true
            meizuFlags.isAccessible = true

            val bit = darkFlag.getInt(null)
            var value = meizuFlags.getInt(layoutParams)
            value = if (isDark) value or bit else value and bit.inv()
            meizuFlags.setInt(layoutParams, value)
            window.attributes = layoutParams
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 设置ColorOS的状态栏字符
     * @param activity Activity
     * @param isDark Boolean
     */
    @JvmStatic
    fun setStatusBarFontIcon_ColorOSUI(activity: Activity, isDark: Boolean) {
        //控制字体颜色，只有黑白两色
        UtilKWindow.getDecorView(activity).systemUiVisibility = 0 or if (isDark) 0x00000010 else 0x00190000
    }
}