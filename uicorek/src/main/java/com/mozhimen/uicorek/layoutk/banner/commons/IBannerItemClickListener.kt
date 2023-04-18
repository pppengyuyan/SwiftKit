package com.mozhimen.uicorek.layoutk.banner.commons

import com.mozhimen.uicorek.layoutk.banner.helpers.BannerViewHolder
import com.mozhimen.uicorek.layoutk.banner.bases.BaseBannerItem

/**
 * @ClassName IOnBannerClickListener
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/11/7 2:32
 * @Version 1.0
 */
interface IBannerItemClickListener {
    fun onBannerClick(viewHolder: BannerViewHolder, item: BaseBannerItem, position: Int)
}