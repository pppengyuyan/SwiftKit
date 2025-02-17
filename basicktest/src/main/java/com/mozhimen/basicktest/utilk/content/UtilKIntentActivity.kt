package com.mozhimen.basicktest.utilk.content

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.mozhimen.basick.elemk.activity.bases.BaseActivityVB
import com.mozhimen.basick.elemk.cons.CMediaFormat
import com.mozhimen.basick.utilk.content.UtilKIntent
import com.mozhimen.basicktest.databinding.ActivityUtilkIntentBinding

/**
 * @ClassName UtilKIntentActivity
 * @Description TODO
 * @Author 83524
 * @Date 2023/4/16 20:51
 * @Version 1.0
 */
class UtilKIntentActivity : BaseActivityVB<ActivityUtilkIntentBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        val register = registerForActivityResult(ActivityResultContracts.GetContent()) {
            VB.utilkIntentImg.setImageURI(it)
        }
        VB.utilkIntentBtn.setOnClickListener {
            startActivityForResult(UtilKIntent.getPickImage(), 0)
        }
        VB.utilkIntentBtn2.setOnClickListener {
            register.launch(CMediaFormat.MIMETYPE_IMAGE_ALL)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
            val uri = data?.data
            if (uri != null) {
                VB.utilkIntentImg.setImageURI(uri)
            }
        }
    }
}