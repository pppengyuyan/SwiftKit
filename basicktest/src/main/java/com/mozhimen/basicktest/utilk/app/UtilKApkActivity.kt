package com.mozhimen.basicktest.utilk.app

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.mozhimen.basick.elemk.activity.bases.BaseActivityVB
import com.mozhimen.basick.utilk.app.UtilKApk
import com.mozhimen.basick.utilk.java.io.file.UtilKFile
import com.mozhimen.basick.utilk.os.UtilKPath
import com.mozhimen.basick.utilk.res.UtilKAssets
import com.mozhimen.basicktest.databinding.ActivityUtilkApkBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @ClassName UtilKApkActivity
 * @Description TODO
 * @Author Mozhimen & Kolin
 * @Date 2023/4/18 13:44
 * @Version 1.0
 */
class UtilKApkActivity : BaseActivityVB<ActivityUtilkApkBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        lifecycleScope.launch(Dispatchers.IO) {
            val apkPathWithName = UtilKPath.Absolute.Internal.getCacheDir() + "/temp/${UtilKFile.dateStr2FileName()}.apk"
            if (!UtilKFile.isFileExist(apkPathWithName)) {
                UtilKAssets.asset2File("basicktest-debug.apk", apkPathWithName)
            }
            UtilKApk.printApkInfo(apkPathWithName)
        }
    }
}