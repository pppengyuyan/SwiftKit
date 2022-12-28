package com.mozhimen.basick.utilk.bitmap.blur

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.os.Build
import android.view.View
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.renderscript.*
import com.mozhimen.basick.utilk.UtilKNumber
import com.mozhimen.basick.utilk.bar.UtilKStatusBar
import com.mozhimen.basick.utilk.context.UtilKApplication
import com.mozhimen.basick.utilk.log.UtilKSmartLog

/**
 * @ClassName RenderScriptHelper
 * @Description
 * warn:renderscript即将遗弃，后期将前移到Vulkan，使用GPU更快
 * https://developer.android.com/guide/topics/renderscript/compute?hl=zh-cn#additional-code-samples
 * https://developer.android.com/guide/topics/renderscript/migrate?hl=zh-cn
 * @Author 大灯泡 / mozhimen / Kolin Zhao
 * @Date 2022/12/24 15:18
 * @Version 1.0
 */
@SuppressLint("StaticFieldLeak")
object RenderScriptHelper {
    private const val TAG = "RenderScriptHelper>>>>>"
    private var _startTime: Long = 0

    @Volatile
    private var _renderScript: RenderScript? = null
    private val _context: Context = UtilKApplication.instance.get()

    @JvmStatic
    fun getRenderScriptInstance(context: Context): RenderScript? {
        if (_renderScript == null) {
            synchronized(RenderScriptHelper::class.java) {
                if (_renderScript == null) {
                    _renderScript = RenderScript.create(context.applicationContext)
                }
            }
        }
        return _renderScript
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @JvmStatic
    fun isRenderScriptSupported(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1
    }

    @JvmStatic
    fun blur(view: View, scaledRatio: Float, radius: Float): Bitmap? {
        return blur(view, scaledRatio, radius, true)
    }

    @JvmStatic
    fun blur(view: View, scaledRatio: Float, radius: Float, fullScreen: Boolean): Bitmap? {
        return blur(view, scaledRatio, radius, fullScreen, 0, 0)
    }

    @JvmStatic
    fun blur(view: View, scaledRatio: Float, radius: Float, fullScreen: Boolean, cutoutX: Int, cutoutY: Int): Bitmap? {
        return blur(getViewBitmap(view, scaledRatio, fullScreen, cutoutX, cutoutY), view.width, view.height, radius)
    }

    @JvmStatic
    fun blur(origin: Bitmap?, resultWidth: Int, resultHeight: Int, radius: Float): Bitmap? {
        _startTime = System.currentTimeMillis()
        return if (isRenderScriptSupported()) {
            UtilKSmartLog.i(TAG, "blur: 脚本模糊")
            scriptBlur(origin, resultWidth, resultHeight, radius)
        } else {
            UtilKSmartLog.i(TAG, "blur: 快速模糊")
            fastBlur(origin, resultWidth, resultHeight, radius)
        }
    }

    @JvmStatic
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun scriptBlur(origin: Bitmap?, outWidth: Int, outHeight: Int, radius: Float): Bitmap? {
        if (origin == null || origin.isRecycled) return null
        val renderScript = getRenderScriptInstance(_context)
        val blurInput = Allocation.createFromBitmap(renderScript, origin)
        val blurOutput = Allocation.createTyped(renderScript, blurInput.type)
        var blur: ScriptIntrinsicBlur? = null
        try {
            blur = ScriptIntrinsicBlur.create(renderScript, blurInput.element)
        } catch (e: RSIllegalArgumentException) {
            if (e.message != null && e.message!!.contains("unsupported element type")) {
                blur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
            }
        }
        if (blur == null) {
            UtilKSmartLog.e(TAG, "scriptBlur: 脚本模糊失败，转fastBlur")
            blurInput.destroy()
            blurOutput.destroy()
            return fastBlur(origin, outWidth, outHeight, radius)
        }
        blur.setRadius(UtilKNumber.normalize(radius, 0f, 20f))
        blur.setInput(blurInput)
        blur.forEach(blurOutput)
        blurOutput.copyTo(origin)

        //释放
        blurInput.destroy()
        blurOutput.destroy()
        val result = Bitmap.createScaledBitmap(origin, outWidth, outHeight, true)
        origin.recycle()
        val time = System.currentTimeMillis() - _startTime
        if (UtilKSmartLog.isOpenLog()) {
            UtilKSmartLog.i("scriptBlur: 模糊用时：【" + time + "ms】")
        }
        return result
    }

    @JvmStatic
    fun fastBlur(origin: Bitmap?, outWidth: Int, outHeight: Int, radius: Float): Bitmap? {
        var tempOrigin = origin
        if (tempOrigin == null || tempOrigin.isRecycled) return null
        tempOrigin = UtilKBitmapBlur.blurBitmap(tempOrigin, UtilKNumber.normalize(radius, 0f, 20f).toInt(), false)
        if (tempOrigin == null || tempOrigin.isRecycled) return null
        tempOrigin = Bitmap.createScaledBitmap(tempOrigin, outWidth, outHeight, true)
        val time = System.currentTimeMillis() - _startTime
        if (UtilKSmartLog.isOpenLog()) {
            UtilKSmartLog.i("fastBlur: 模糊用时：【" + time + "ms】")
        }
        return tempOrigin
    }

    @JvmStatic
    fun getViewBitmap(view: View, fullScreen: Boolean): Bitmap? {
        return getViewBitmap(view, 1.0f, fullScreen, 0, 0)
    }

    @JvmStatic
    fun getViewBitmap(view: View, scaledRatio: Float, fullScreen: Boolean, cutoutX: Int, cutoutY: Int): Bitmap? {
        if (view.width <= 0 || view.height <= 0) {
            UtilKSmartLog.e("getViewBitmap  >>  宽或者高为空")
            return null
        }
        val statusBarHeight = UtilKStatusBar.getStatusBarHeight(false)
        var tempBitmap: Bitmap
        UtilKSmartLog.i("getViewBitmap 模糊原始图像分辨率 [" + view.width + " x " + view.height + "]")
        tempBitmap = try {
            Bitmap.createBitmap((view.width * scaledRatio).toInt(), (view.height * scaledRatio).toInt(), Bitmap.Config.ARGB_8888)
        } catch (error: OutOfMemoryError) {
            System.gc()
            return null
        }
        val canvas = Canvas(tempBitmap)
        val matrix = Matrix()
        matrix.preScale(scaledRatio, scaledRatio)
        canvas.setMatrix(matrix)
        val bgDrawable = view.background
        if (bgDrawable == null) {
            canvas.drawColor(Color.parseColor("#FAFAFA"))
        } else {
            bgDrawable.draw(canvas)
        }
        if (fullScreen) {
            if (statusBarHeight > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && view.context is Activity) {
                val statusBarColor = (view.context as Activity).window.statusBarColor
                val paint = Paint(Paint.ANTI_ALIAS_FLAG)
                paint.color = statusBarColor
                val rect = Rect(0, 0, view.width, statusBarHeight)
                canvas.drawRect(rect, paint)
            }
        }
        view.draw(canvas)
        UtilKSmartLog.i("getViewBitmap 模糊缩放图像分辨率 [" + tempBitmap.width + " x " + tempBitmap.height + "]")
        if (cutoutX > 0 || cutoutY > 0) {
            try {
                val cutLeft = (cutoutX * scaledRatio).toInt()
                val cutTop = (cutoutY * scaledRatio).toInt()
                val cutWidth = tempBitmap.width - cutLeft
                val cutHeight = tempBitmap.height - cutTop
                tempBitmap = Bitmap.createBitmap(tempBitmap, cutLeft, cutTop, cutWidth, cutHeight, null, false)
            } catch (e: Exception) {
                System.gc()
            }
        }
        return tempBitmap
    }
}