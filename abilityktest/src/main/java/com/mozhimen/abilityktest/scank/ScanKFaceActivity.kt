package com.mozhimen.abilityktest.scank

import android.graphics.Bitmap
import android.graphics.PointF
import android.graphics.RectF
import android.media.FaceDetector
import android.os.Bundle
import android.util.Log
import androidx.camera.core.ImageProxy
import com.mozhimen.abilityktest.databinding.ActivityScankFaceBinding
import com.mozhimen.basick.elemk.activity.bases.BaseActivityVB
import com.mozhimen.basick.manifestk.cons.CPermission
import com.mozhimen.basick.manifestk.permission.ManifestKPermission
import com.mozhimen.basick.manifestk.permission.annors.APermissionCheck
import com.mozhimen.basick.manifestk.annors.AManifestKRequire
import com.mozhimen.basick.manifestk.cons.CUseFeature
import com.mozhimen.basick.utilk.content.activity.UtilKLaunchActivity
import com.mozhimen.basick.utilk.graphics.bitmap.UtilKBitmapDeal
import com.mozhimen.basick.utilk.graphics.bitmap.UtilKBitmapFormat
import com.mozhimen.componentk.cameraxk.annors.ACameraXKFacing
import com.mozhimen.componentk.cameraxk.annors.ACameraXKFormat
import com.mozhimen.componentk.cameraxk.commons.ICameraXKFrameListener
import com.mozhimen.componentk.cameraxk.helpers.ImageConverter
import com.mozhimen.componentk.cameraxk.mos.CameraXKConfig
import com.mozhimen.uicorek.viewk.scan.ViewKScanOverlay


/**
 * @ClassName ScanKFaceActivity
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2023/1/3 14:46
 * @Version 1.0
 */
@AManifestKRequire(CPermission.CAMERA, CUseFeature.CAMERA, CUseFeature.CAMERA_AUTOFOCUS)
@APermissionCheck(CPermission.CAMERA)
class ScanKFaceActivity : BaseActivityVB<ActivityScankFaceBinding>() {
    override fun initData(savedInstanceState: Bundle?) {
        ManifestKPermission.initPermissions(this) {
            if (it) {
                super.initData(savedInstanceState)
            } else {
                UtilKLaunchActivity.startSettingAppDetails(this)
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        VB.scankFaceCamera.initCamera(this, CameraXKConfig(facing = ACameraXKFacing.FRONT, format = ACameraXKFormat.RGBA_8888))
        VB.scankFaceCamera.setCameraXKFrameListener(_frameAnalyzer)
        VB.scankFaceCamera.startCamera()
    }

    private var _rgb565Bitmap: Bitmap? = null
    private var _faceDetector: FaceDetector? = null
    private var _faces: Array<FaceDetector.Face?> = Array(1) { null }
    private var _facePointF = PointF()
    private var _currentTime = System.currentTimeMillis()
    private val _frameAnalyzer: ICameraXKFrameListener by lazy {
        object : ICameraXKFrameListener {
            override fun onFrame(image: ImageProxy) {
                if (System.currentTimeMillis() - _currentTime > 2000L) {
                    _currentTime = System.currentTimeMillis()
                    _rgb565Bitmap =
                        UtilKBitmapFormat.bitmap2Rgb565Bitmap(
                            UtilKBitmapDeal.rotateBitmap(
                                ImageConverter.rgba8888Image2Rgba8888Bitmap(image), -90, flipX = true
                            )
                        )
                    if (_faceDetector == null) {
                        _faceDetector = FaceDetector(_rgb565Bitmap!!.width, _rgb565Bitmap!!.height, 1)
                    }
                    val faceCount = _faceDetector!!.findFaces(_rgb565Bitmap!!, _faces)
                    Log.v(TAG, "faceCount: $faceCount")

                    runOnUiThread {
                        VB.scankFaceImg.setImageBitmap(_rgb565Bitmap)
                        if (_faces.getOrNull(0) != null && faceCount != 0) {
                            _faces[0]!!.getMidPoint(_facePointF)
                            val eyeDistance = _faces[0]!!.eyesDistance()
                            VB.scankFaceOverlay.setObjectRect(
                                _rgb565Bitmap!!.width, _rgb565Bitmap!!.height, listOf(
                                    ViewKScanOverlay.Detection(
                                        RectF(
                                            _facePointF.x - eyeDistance,
                                            _facePointF.y - eyeDistance,
                                            _facePointF.x + eyeDistance,
                                            _facePointF.y + eyeDistance,
                                        ), null
                                    )
                                )
                            )
                        } else {
                            VB.scankFaceOverlay.clearObjectRect()
                        }
                    }
                }
                image.close()
            }
        }
    }
}