package com.mozhimen.basicktest.utilk.graphics

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import androidx.lifecycle.lifecycleScope
import com.mozhimen.basick.elemk.activity.bases.BaseActivityVB
import com.mozhimen.basick.manifestk.cons.CPermission
import com.mozhimen.basick.manifestk.permission.annors.APermissionCheck
import com.mozhimen.basick.manifestk.annors.AManifestKRequire
import com.mozhimen.basick.utilk.exts.drawable2Bitmap
import com.mozhimen.basick.utilk.res.UtilKRes
import com.mozhimen.basick.utilk.graphics.bitmap.UtilKBitmapDeal
import com.mozhimen.basick.utilk.graphics.bitmap.UtilKBitmapIO
import com.mozhimen.basick.utilk.os.UtilKPath
import com.mozhimen.basicktest.R
import com.mozhimen.basicktest.databinding.ActivityUtilkBitmapBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AManifestKRequire(CPermission.INTERNET)
@APermissionCheck(CPermission.INTERNET)
class UtilKBitmapActivity : BaseActivityVB<ActivityUtilkBitmapBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        lifecycleScope.launch(Dispatchers.Main) {
            val bitmap: Bitmap?
            withContext(Dispatchers.IO) {
                bitmap =
                    UtilKBitmapIO.url2BitmapCoroutine("http://192.168.2.6/construction-sites-images/person/20221101/93ea2a3e11e54a76944dfc802519e3cc.jpg")//http://img.crcz.com/allimg/202003/25/1585100748975745.jpg
            }
            bitmap?.let {
                VB.utilKBitmapImg.setImageBitmap(it)
            }
        }

        val bitmap = UtilKRes.getDrawable(R.mipmap.utilk_img)!!.drawable2Bitmap()
        VB.utilkBitmapSeekbarBmpZoom.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val progress = seekBar!!.progress
                val ratio: Float = 1 + progress / 100f
                Log.i(TAG, "onStopTrackingTouch: ratio $ratio")
                Log.i(TAG, "onStopTrackingTouch: bmp w ${bitmap.width} h ${bitmap.height}")
                val zoomBmp = UtilKBitmapDeal.zoomBitmap(bitmap, ratio)
                Log.i(TAG, "onStopTrackingTouch: zoomBmp w ${zoomBmp.width} h ${zoomBmp.height}")
                val scaleBmp = UtilKBitmapDeal.scaleBitmap(zoomBmp, bitmap.width, bitmap.height)
                Log.i(TAG, "onStopTrackingTouch: scaleBmp w ${scaleBmp.width} h ${scaleBmp.height}")
                Log.i(TAG, "onStopTrackingTouch: --->")
                VB.utilkBitmapImgBmpZoom.setImageBitmap(scaleBmp)
            }
        })

        VB.utilkBitmapSeekbarBmpScale.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val progress = seekBar!!.progress
                var ratio: Float = 5 * progress / 100f
                if (ratio < 1) ratio = 1f
                val scaleBmp = UtilKBitmapDeal.scaleBitmap(bitmap, (bitmap.width / ratio).toInt(), (bitmap.height / ratio).toInt())
                Log.i(TAG, "onStopTrackingTouch: scaleBmp w ${scaleBmp.width} h ${scaleBmp.height}")
                VB.utilkBitmapImgBmpScale.setImageBitmap(scaleBmp)
            }
        })

        VB.utilkBitmapBtnSave.setOnClickListener {
            UtilKBitmapIO.bitmap2JpegAlbumFile(bitmap, UtilKPath.Absolute.Internal.getCacheDir())
        }
    }
}