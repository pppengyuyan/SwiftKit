package com.mozhimen.debugk

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.mozhimen.basick.manifestk.annors.AManifestKRequire
import com.mozhimen.basick.manifestk.cons.CManifest
import com.mozhimen.underlayk.logk.exts.ExtsLogK.et

/**
 * @ClassName DebugK
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/5/27 12:03
 * @Version 1.0
 */
object DebugK {
    private const val TAG = "DebugK>>>>>"

    fun toggleDialog(fragmentManager: FragmentManager){
        try {
            val clazz = Class.forName("com.mozhimen.debugk.temps.DebugKDialogFragment")
            val target = clazz.getConstructor().newInstance() as DialogFragment
            target.show(fragmentManager, "debugk_dialog")
        } catch (e: Exception) {
            e.printStackTrace()
            e.message?.et(TAG)
        }
    }
}