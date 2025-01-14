package com.mozhimen.uicorektest.adapterk

import android.view.View
import com.mozhimen.basick.elemk.activity.bases.BaseActivityVB
import com.mozhimen.basick.utilk.exts.start
import com.mozhimen.uicorektest.databinding.ActivityAdapterkBinding

class AdapterKActivity : BaseActivityVB<ActivityAdapterkBinding>() {
    fun goAdapterKRecycler(view: View) {
        start<AdapterKRecyclerActivity>()
    }

    fun goAdapterKRecyclerStuffed(view: View) {
        start<AdapterKRecyclerStuffedActivity>()
    }

    fun goAdapterKRecyclerVb2(view: View) {
        start<AdapterKRecyclerVB2Activity>()
    }
}