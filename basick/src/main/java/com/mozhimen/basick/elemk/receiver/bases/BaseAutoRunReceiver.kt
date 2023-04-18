package com.mozhimen.basick.elemk.receiver.bases

import android.content.Context
import android.content.Intent
import com.mozhimen.basick.manifestk.cons.CPermission
import com.mozhimen.basick.manifestk.annors.AManifestKRequire
import com.mozhimen.basick.elemk.rxjava.bases.BaseObserver
import com.mozhimen.basick.utilk.os.thread.UtilKRxJavaTrans
import com.mozhimen.basick.utilk.content.UtilKContextStart
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * @ClassName PrefabKReceiverAutoRun
 * @Description

 * 权限:
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />

 * 继承:
class ElemKAutoRunReceiver : BaseAutoRunReceiver(BasicKActivity::class.java, 5000)

 * 静态注册:
<receiver
android:name=".elemk.receiver.ElemKAutoRunReceiver"
android:enabled="true"
android:exported="true">
<intent-filter android:priority="1000">
<action android:name="android.intent.action.BOOT_COMPLETED" />
</intent-filter>
</receiver>

 * @Author Kolin Zhao / Mozhimen
 * @Date 2022/6/13 11:55
 * @Version 1.0
 */
@AManifestKRequire(CPermission.RECEIVE_BOOT_COMPLETED)
open class BaseAutoRunReceiver(private val clazz: Class<*>, private val _delayTime: Long = 0L) : BaseBroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                if (_delayTime != 0L) {
                    Observable.just("").delay(_delayTime, TimeUnit.MILLISECONDS).compose(UtilKRxJavaTrans.io2mainObservable()).subscribe(object : BaseObserver<String>() {
                        override fun onComplete() {
                            UtilKContextStart.startContext(context, clazz)
                        }
                    })
                } else {
                    UtilKContextStart.startContext(context, clazz)
                }
            }
        }
    }
}