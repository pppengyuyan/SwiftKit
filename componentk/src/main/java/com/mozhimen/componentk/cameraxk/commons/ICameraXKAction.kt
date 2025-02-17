package com.mozhimen.componentk.cameraxk.commons

import androidx.camera.core.ImageCapture
import com.mozhimen.componentk.cameraxk.annors.ACameraXKFacing
import com.mozhimen.componentk.cameraxk.cons.ECameraXKTimer

/**
 * @ClassName ICameraXKAction
 * @Description TODO
 * @Author Kolin Zhao / Mozhimen
 * @Date 2022/6/9 14:32
 * @Version 1.0
 */
interface ICameraXKAction {
    fun setCameraXKListener(listener: ICameraXKListener)
    fun setCameraXKCaptureListener(listener: ICameraXKCaptureListener)
    fun setCameraXKFrameListener(listener: ICameraXKFrameListener)
    fun changeHdr(isOpen: Boolean)
    fun changeFlash(@ImageCapture.FlashMode flashMode: Int)
    fun changeCountDownTimer(timer: ECameraXKTimer)
    fun changeCameraFacing(@ACameraXKFacing facing: Int)
    fun takePicture()
}