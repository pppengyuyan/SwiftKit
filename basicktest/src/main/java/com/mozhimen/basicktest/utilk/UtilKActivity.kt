package com.mozhimen.basicktest.utilk

import android.os.Bundle
import android.view.View
import com.mozhimen.basick.basek.BaseKActivityVB
import com.mozhimen.basick.extsk.start
import com.mozhimen.basick.utilk.UtilKDataBus
import com.mozhimen.basicktest.databinding.ActivityUtilkBinding

class UtilKActivity : BaseKActivityVB<ActivityUtilkBinding>() {

    override fun initData(savedInstanceState: Bundle?) {
        UtilKDataBus.with<String>("stickyData").setStickyData("即时消息主界面")
    }

    fun goUtilKAsset(view: View) {
        start<UtilKAssetActivity>()
    }

    fun goUtilKBitmap(view: View) {
        start<UtilKBitmapActivity>()
    }

    fun goUtilKDataBus(view: View) {
        start<UtilKDataBusActivity>()
    }

    fun goUtilKFile(view: View) {
        start<UtilKFileActivity>()
    }

    fun goUtilKGesture(view: View) {
        start<UtilKGestureActivity>()
    }

    fun goUtilKVerifyUrl(view: View) {
        start<UtilKVerifyUrlActivity>()
    }
}