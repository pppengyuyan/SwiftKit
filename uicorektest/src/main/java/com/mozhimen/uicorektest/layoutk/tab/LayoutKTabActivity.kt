package com.mozhimen.uicorektest.layoutk.tab

import android.os.Bundle
import android.view.View
import com.mozhimen.basick.basek.BaseKActivityVB
import com.mozhimen.basick.extsk.start
import com.mozhimen.uicorektest.databinding.ActivityLayoutkTabBinding

/**
 * @ClassName LayoutKTabActivity
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/11/7 1:29
 * @Version 1.0
 */
class LayoutKTabActivity : BaseKActivityVB<ActivityLayoutkTabBinding>() {
    override fun initData(savedInstanceState: Bundle?) {

    }

    fun goLayoutKTabBottom(view: View) {
        start<LayoutKTabBottomActivity>()
    }

    fun goLayoutKTabBottomFragment(view: View) {
        start<LayoutKTabBottomFragmentActivity>()
    }

    fun goLayoutKTabBottomLayout(view: View) {
        start<LayoutKTabBottomLayoutActivity>()
    }

    fun goLayoutKTabTop(view: View) {
        start<LayoutKTabTopActivity>()
    }

    fun goLayoutKTabTopLayout(view: View) {
        start<LayoutKTabTopLayoutActivity>()
    }
}