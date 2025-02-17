package com.mozhimen.app.demo

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mozhimen.app.BR
import com.mozhimen.app.R
import com.mozhimen.app.databinding.ActivityDemoBinding
import com.mozhimen.app.databinding.ItemDemoListBinding
import com.mozhimen.basick.elemk.activity.bases.BaseActivityVBVM
import com.mozhimen.basick.statusbark.StatusBarK
import com.mozhimen.uicorek.adapterk.AdapterKRecyclerStuffedVB
import kotlin.math.abs

@com.mozhimen.basick.statusbark.annors.AStatusBarK(statusBarType = com.mozhimen.basick.statusbark.annors.AStatusBarKType.FULL_SCREEN)
class DemoActivity : BaseActivityVBVM<ActivityDemoBinding, DemoViewModel>() {
    private var _scrollY = 0
    private var _alpha = 0
    override fun initFlag() {
        StatusBarK.initStatusBar(this)
    }
    override fun initView(savedInstanceState: Bundle?) {
        val list = arrayListOf(
            Astro("白羊座", "晴天", 90),
            Astro("天蝎座", "雨天", 70),
            Astro("金牛座", "晴天", 90),
            Astro("水瓶座", "雷阵雨", 80),
            Astro("处女座", "晴天", 90),
            Astro("双子座", "晴天", 90),
            Astro("射手座", "晴天", 90),
        )
        VB.demoRecycler.layoutManager = LinearLayoutManager(this)
        VB.demoRecycler.adapter =
            AdapterKRecyclerStuffedVB<Astro, ItemDemoListBinding>(
                list,
                R.layout.item_demo_list,
                R.layout.item_demo_header,
                null,
                BR.itemAstro
            ) { holder, itemData, position, _ ->
                if (position in 1 until list.size) {
                    holder.binding?.demoItemListBtn?.setOnClickListener {
                        Log.i(TAG, itemData.name)
                    }
                }
            }
        VB.demoRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                _scrollY += dy
                _alpha = if (abs(_scrollY) > 1000) {
                    VB.demoBg.setBlurOffset(100)
                    100
                } else {
                    VB.demoBg.setBlurOffset(_scrollY / 10)
                    abs(_scrollY) / 10
                }
                VB.demoBg.setBlurLevel(_alpha)
            }
        })
    }

    override fun bindViewVM(vb: ActivityDemoBinding) {
    }
}