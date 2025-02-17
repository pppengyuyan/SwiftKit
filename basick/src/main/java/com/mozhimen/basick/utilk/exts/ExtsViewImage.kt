package com.mozhimen.basick.utilk.exts

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.mozhimen.basick.utilk.view.UtilKImageView

/**
 * @ClassName ExtsKViewImage
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/11/6 14:47
 * @Version 1.0
 */
/**
 * 自适应图片
 * @receiver ImageView
 * @param drawable Drawable
 */
fun ImageView.fitImage(drawable: Drawable) {
    UtilKImageView.fitImage(this, drawable)
}