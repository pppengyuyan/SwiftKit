package com.mozhimen.uicorektest.layoutk

import android.os.Bundle
import com.mozhimen.basick.elemk.activity.bases.BaseActivityVB
import com.mozhimen.basick.utilk.exts.showToast
import com.mozhimen.basick.elemk.mos.MKey
import com.mozhimen.uicorektest.databinding.ActivityLayoutkChipGroupBinding

/**
 * @ClassName LayoutKChipGroupActivity
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/11/6 23:41
 * @Version 1.0
 */
class LayoutKChipGroupActivity : BaseActivityVB<ActivityLayoutkChipGroupBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        VB.layoutkChipGroup.bindKeys(
            arrayListOf(
                MKey("0", "赛博朋克2077"),
                MKey("1", "老头环"),
                MKey("2", "塞尔达"),
                MKey("3", "使命召唤19"),
                MKey("4", "全战三国"),
                MKey("5", "荒野大镖客"),
                MKey("6", "GTA6"),
                MKey("7", "文明6")
            )
        )
        VB.layoutkChipGroup.setOnCheckedListener { _, i, dataKKey ->
            "index: $i dataKey: ${dataKKey.id} ${dataKKey.name}".showToast()
        }
        VB.layoutkChipGroupAdd.setOnClickListener {
            VB.layoutkChipGroup.addKey(MKey("ss", "原神"))
        }
        VB.layoutkChipGroupRemove.setOnClickListener {
            VB.layoutkChipGroup.removeKey(MKey("ss", "原神"))
        }
    }
}