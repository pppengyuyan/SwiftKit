package com.mozhimen.basick.utilk.view.keyboard

import android.R
import android.app.Activity
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import com.mozhimen.basick.utilk.content.activity.UtilKActivity
import com.mozhimen.basick.utilk.content.UtilKApplication
import com.mozhimen.basick.utilk.view.UtilKView
import com.mozhimen.basick.utilk.view.window.UtilKWindow


/**
 * @ClassName UtilKKeyboardChange
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2023/2/20 15:16
 * @Version 1.0
 */
object UtilKInputChange {
    private const val TAG_ON_GLOBAL_LAYOUT_LISTENER = -8

    /**
     * Register soft input changed listener.
     * @param activity The activity.
     * @param listener The soft input changed listener.
     */
    @JvmStatic
    fun registerKeyBoardChangeListener(activity: Activity, listener: IUtilKKeyboardChangeListener2) {
        registerKeyBoardChangeListener(activity.window, listener)
    }

    /**
     * Register soft input changed listener.
     * @param window The window.
     * @param listener The soft input changed listener.
     */
    @JvmStatic
    fun registerKeyBoardChangeListener(window: Window, listener: IUtilKKeyboardChangeListener2) {
        val flags = window.attributes.flags
        if (flags and WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS != 0) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
        val contentView = window.findViewById<FrameLayout>(R.id.content)
        val decorViewInvisibleHeightPre = intArrayOf(UtilKWindow.getDecorViewInvisibleHeight(window))
        val onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            val height = UtilKWindow.getDecorViewInvisibleHeight(window)
            if (decorViewInvisibleHeightPre[0] != height) {
                listener.onChange(height)
                decorViewInvisibleHeightPre[0] = height
            }
        }
        contentView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
        contentView.setTag(TAG_ON_GLOBAL_LAYOUT_LISTENER, onGlobalLayoutListener)
    }

    /**
     * Unregister soft input changed listener.
     * @param window The window.
     */
    @JvmStatic
    fun unregisterKeyBoardChangedListener(window: Window) {
        val contentView = window.findViewById<View>(R.id.content) ?: return
        val tag = contentView.getTag(TAG_ON_GLOBAL_LAYOUT_LISTENER)
        if (tag is ViewTreeObserver.OnGlobalLayoutListener) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                contentView.viewTreeObserver.removeOnGlobalLayoutListener(tag)
                //这里会发生内存泄漏 如果不设置为null
                contentView.setTag(TAG_ON_GLOBAL_LAYOUT_LISTENER, null)
            }
        }
    }

    @JvmStatic
    fun observerKeyboardChangeByView(view: View): ViewTreeObserver.OnGlobalLayoutListener? {
        val activity: Activity = UtilKActivity.getActivityByContext(view.context, true) ?: return null
        return observerKeyboardChange(activity, object : IUtilKKeyboardChangeListener {
            private val _location = intArrayOf(0, 0)
            override fun onChange(keyboardBounds: Rect, isVisible: Boolean) {
                if (isVisible) {
                    view.getLocationOnScreen(_location)
                    view.translationY = view.translationY + keyboardBounds.top - (_location[1] + view.height)
                } else {
                    view.animate().translationY(0f).setDuration(300).setStartDelay(100).start()
                }
            }
        })
    }

    @JvmStatic
    fun observerKeyboardChange(activity: Activity, listener: IUtilKKeyboardChangeListener): ViewTreeObserver.OnGlobalLayoutListener {
        val decorView = UtilKWindow.getDecorView(activity)
        val onGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
            private var _rect = Rect()
            private var _keyboardRect = Rect()
            private var _originalContentRect = Rect()
            private var _lastVisible = false
            private var _lastHeight = 0

            override fun onGlobalLayout() {
                val contentView = decorView.findViewById<View>(android.R.id.content) ?: return
                if (_originalContentRect.isEmpty) {
                    val destView: View = UtilKView.findViewFromParentByView(decorView, contentView)!!
                    _originalContentRect[destView.left, destView.top, destView.right] = destView.bottom
                }
                decorView.getWindowVisibleDisplayFrame(_rect)
                _keyboardRect[_rect.left, _rect.bottom, _rect.right] = _originalContentRect.bottom
                val isVisible = _keyboardRect.height() > _originalContentRect.height() shr 2 && UtilKInputManager.isActive(UtilKApplication.instance.get())
                if (isVisible == _lastVisible && _keyboardRect.height() == _lastHeight) return
                _lastVisible = isVisible
                _lastHeight = _keyboardRect.height()
                listener.onChange(_keyboardRect, isVisible)
            }
        }
        UtilKView.safeAddGlobalLayoutListener(decorView, onGlobalLayoutListener)
        return onGlobalLayoutListener
    }

    interface IUtilKKeyboardChangeListener {
        fun onChange(keyboardBounds: Rect, isVisible: Boolean)
    }

    interface IUtilKKeyboardChangeListener2 {
        fun onChange(height: Int)
    }
}