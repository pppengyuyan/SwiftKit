package com.mozhimen.basick.animk.builder.temps

import android.animation.*
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.annotation.FloatRange
import com.mozhimen.basick.animk.builder.bases.BaseAnimKType
import com.mozhimen.basick.animk.builder.cons.EDirection
import com.mozhimen.basick.animk.builder.mos.AnimKConfig

/**
 * @ClassName ScaleConfig
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/11/17 23:02
 * @Version 1.0
 */
open class ScaleType : BaseAnimKType<ScaleType>() {
    private var _scaleFromX = 0f
    private var _scaleToX = 1f
    private var _scaleFromY = 0f
    private var _scaleToY = 1f
    private var _isDismiss = false
    override lateinit var _animator: Animator

    init {
        setPivot(.5f, .5f)
        setPivot2(.5f, .5f)
    }

    fun fromDirection(vararg from: EDirection): ScaleType {
        if (from.isNotEmpty()) {
            var flag = 0
            for (direction in from) {
                flag = flag or direction.flag
            }
            if (EDirection.isDirectionFlag(EDirection.LEFT, flag)) {
                _pivotX = 0f
            }
            if (EDirection.isDirectionFlag(EDirection.RIGHT, flag)) {
                _pivotX = 1f
            }
            if (EDirection.isDirectionFlag(EDirection.CENTER_HORIZONTAL, flag)) {
                _pivotX = 0.5f
            }
            if (EDirection.isDirectionFlag(EDirection.TOP, flag)) {
                _pivotY = 0f
            }
            if (EDirection.isDirectionFlag(EDirection.BOTTOM, flag)) {
                _pivotY = 1f
            }
            if (EDirection.isDirectionFlag(EDirection.CENTER_VERTICAL, flag)) {
                _pivotY = 0.5f
            }
        }
        return this
    }

    fun toDirection(vararg to: EDirection): ScaleType {
        if (to.isNotEmpty()) {
            var flag = 0
            for (direction in to) {
                flag = flag or direction.flag
            }
            if (EDirection.isDirectionFlag(EDirection.LEFT, flag)) {
                _pivotX2 = 0f
            }
            if (EDirection.isDirectionFlag(EDirection.RIGHT, flag)) {
                _pivotX2 = 1f
            }
            if (EDirection.isDirectionFlag(EDirection.CENTER_HORIZONTAL, flag)) {
                _pivotX2 = 0.5f
            }
            if (EDirection.isDirectionFlag(EDirection.TOP, flag)) {
                _pivotY2 = 0f
            }
            if (EDirection.isDirectionFlag(EDirection.BOTTOM, flag)) {
                _pivotY2 = 1f
            }
            if (EDirection.isDirectionFlag(EDirection.CENTER_VERTICAL, flag)) {
                _pivotY2 = 0.5f
            }
        }
        return this
    }

    fun show(): ScaleType {
        _isDismiss = false
        return this
    }

    fun hide(): ScaleType {
        _isDismiss = true
        return this
    }

    fun scaleX(@FloatRange(from = 0.0, to = 1.0) fromVal: Float, @FloatRange(from = 0.0, to = 1.0) toVal: Float): ScaleType {
        _scaleFromX = fromVal
        _scaleToX = toVal
        return this
    }

    fun scaleY(@FloatRange(from = 0.0, to = 1.0) fromVal: Float, @FloatRange(from = 0.0, to = 1.0) toVal: Float): ScaleType {
        _scaleFromY = fromVal
        _scaleToY = toVal
        return this
    }

    fun scale(@FloatRange(from = 0.0, to = 1.0) fromVal: Float, @FloatRange(from = 0.0, to = 1.0) toVal: Float): ScaleType {
        scaleX(fromVal, toVal)
        scaleY(fromVal, toVal)
        return this
    }

    override fun buildAnimation(animKConfig: AnimKConfig): Animation {
        val values = genConfigs()
        val animation: Animation = ScaleAnimation(
            values[0], values[1], values[2], values[3],
            Animation.RELATIVE_TO_SELF, values[4],
            Animation.RELATIVE_TO_SELF, values[5]
        )
        formatAnimation(animKConfig, animation)
        return animation
    }

    override fun buildAnimator(animKConfig: AnimKConfig): Animator {
        val values = genConfigs()
        _animator = AnimatorSet()
        val scaleX: Animator = ObjectAnimator.ofFloat(null, View.SCALE_X, values[0], values[1])
        val scaleY: Animator = ObjectAnimator.ofFloat(null, View.SCALE_Y, values[2], values[3])
        scaleX.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                val target = (animation as ObjectAnimator).target
                if (target is View) {
                    target.pivotX = target.width * values[4]
                }
            }
        })
        scaleY.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                val target = (animation as ObjectAnimator).target
                if (target is View) {
                    target.pivotY = target.height * values[5]
                }
            }
        })
        (_animator as AnimatorSet).playTogether(scaleX, scaleY)
        formatAnimator(animKConfig, _animator)
        return _animator
    }

    private fun genConfigs(): FloatArray {
        val result = FloatArray(6)
        result[0] = if (!_isDismiss) _scaleFromX else _scaleToX
        result[1] = if (!_isDismiss) _scaleToX else _scaleFromX
        result[2] = if (!_isDismiss) _scaleFromY else _scaleToY
        result[3] = if (!_isDismiss) _scaleToY else _scaleFromY
        result[4] = if (!_isDismiss) _pivotX else _pivotX2
        result[5] = if (!_isDismiss) _pivotY else _pivotY2
        return result
    }

    companion object {
        val LEFT_TO_RIGHT_SHOW = ScaleType().apply {
            fromDirection(EDirection.LEFT).toDirection(EDirection.RIGHT).show()
        }

        val LEFT_TO_RIGHT_HIDE = ScaleType().apply {
            fromDirection(EDirection.LEFT).toDirection(EDirection.RIGHT).hide()
        }

        val RIGHT_TO_LEFT_SHOW = ScaleType().apply {
            fromDirection(EDirection.RIGHT).toDirection(EDirection.LEFT).show()
        }

        val RIGHT_TO_LEFT_HIDE = ScaleType().apply {
            fromDirection(EDirection.RIGHT).toDirection(EDirection.LEFT).hide()
        }

        val TOP_TO_BOTTOM_SHOW = ScaleType().apply {
            fromDirection(EDirection.TOP).toDirection(EDirection.BOTTOM).show()
        }

        val TOP_TO_BOTTOM_HIDE = ScaleType().apply {
            fromDirection(EDirection.TOP).toDirection(EDirection.BOTTOM).hide()
        }

        val BOTTOM_TO_TOP_SHOW = ScaleType().apply {
            fromDirection(EDirection.BOTTOM).toDirection(EDirection.TOP).show()
        }

        val BOTTOM_TO_TOP_HIDE = ScaleType().apply {
            fromDirection(EDirection.BOTTOM).toDirection(EDirection.TOP).hide()
        }

        val CENTER_SHOW = ScaleType().apply {
            fromDirection(EDirection.CENTER).toDirection(EDirection.CENTER).show()
        }

        val CENTER_HIDE = ScaleType().apply {
            fromDirection(EDirection.CENTER).toDirection(EDirection.CENTER).hide()
        }
    }
}

