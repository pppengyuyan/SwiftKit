package com.mozhimen.uicorek.layoutk.bases

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.mozhimen.uicorek.layoutk.commons.ILayoutK

/**
 * @ClassName LayoutKLinear
 * @Description TODO
 * @Author Kolin Zhao / Mozhimen
 * @Date 2021/12/24 14:39
 * @Version 1.0
 */
abstract class BaseLayoutKLinear : LinearLayout, ILayoutK {

    protected val TAG = "${this.javaClass.simpleName}>>>>>"

    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    override fun initFlag() {}
    override fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int) {}
    override fun initView() {}
}