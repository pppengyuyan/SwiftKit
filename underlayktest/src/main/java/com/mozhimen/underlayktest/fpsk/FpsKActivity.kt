package com.mozhimen.underlayktest.fpsk

import android.os.Bundle
import com.mozhimen.basick.elemk.activity.bases.BaseActivityVB
import com.mozhimen.basick.utilk.exts.showToast
import com.mozhimen.underlayk.fpsk.FpsK
import com.mozhimen.underlayktest.databinding.ActivityFpskBinding


/**
 * @ClassName FpsKActivity
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2023/3/15 17:16
 * @Version 1.0
 */
class FpsKActivity : BaseActivityVB<ActivityFpskBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        vb.fpskBtn.setOnClickListener {
            FpsK.instance.toggle()
        }
        vb.fpskBtnTip.setOnClickListener {
            FpsK.instance.isShow().toString().showToast()
        }
    }
}