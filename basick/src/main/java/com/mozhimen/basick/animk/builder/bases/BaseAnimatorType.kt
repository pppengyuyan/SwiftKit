package com.mozhimen.basick.animk.builder.bases

import android.animation.Animator
import com.mozhimen.basick.animk.builder.commons.IAnimatorType
import com.mozhimen.basick.animk.builder.mos.AnimKConfig

/**
 * @ClassName BaseAnimatorType
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/11/26 17:45
 * @Version 1.0
 */
abstract class BaseAnimatorType<T> : BaseType<T>(), IAnimatorType {
    override fun formatAnimator(animKConfig: AnimKConfig, animator: Animator) {
        super.formatAnimator(animKConfig, animator)
        animator.interpolator = _interpolator ?: animKConfig.interpolator
    }
}