package com.mozhimen.basicktest.utilk.jetpack

import android.os.Bundle
import com.mozhimen.basick.elemk.activity.bases.BaseActivityVB
import com.mozhimen.basick.utilk.exts.showToast
import com.mozhimen.basick.utilk.jetpack.lifecycle.UtilKDataBus
import com.mozhimen.basicktest.databinding.ActivityUtilkDataBusBinding

class UtilKDataBusActivity : BaseActivityVB<ActivityUtilkDataBusBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        UtilKDataBus.with<String>("stickyData").observeSticky(this, true) {
            "黏性事件: $it".showToast()
        }

        UtilKDataBus.with<String>("stickyData").observe(this) {
            "非黏性事件: $it".showToast()
        }

        VB.utilkDataBusMsgBtn.setOnClickListener {
            UtilKDataBus.with<String>("stickyData").setStickyData("即时消息当前界面")
        }
    }
}