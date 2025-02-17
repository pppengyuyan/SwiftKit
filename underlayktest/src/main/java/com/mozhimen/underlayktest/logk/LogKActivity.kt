package com.mozhimen.underlayktest.logk

import android.os.Bundle
import android.util.Log
import com.mozhimen.basick.elemk.activity.bases.BaseActivityVB
import com.mozhimen.basick.manifestk.permission.annors.APermissionCheck
import com.mozhimen.basick.manifestk.cons.CPermission
import com.mozhimen.basick.manifestk.annors.AManifestKRequire
import com.mozhimen.underlayk.logk.LogK
import com.mozhimen.underlayk.logk.LogKMgr
import com.mozhimen.underlayk.logk.bases.BaseLogKConfig
import com.mozhimen.basick.utilk.log.cons.CLogType
import com.mozhimen.underlayk.logk.temps.printer.LogKPrinterMonitor
import com.mozhimen.underlayk.logk.temps.printer.LogKPrinterView
import com.mozhimen.underlayktest.databinding.ActivityLogkBinding

@AManifestKRequire(CPermission.SYSTEM_ALERT_WINDOW)
@APermissionCheck(CPermission.SYSTEM_ALERT_WINDOW)
class LogKActivity : BaseActivityVB<ActivityLogkBinding>() {
    private val _printerView: LogKPrinterView<LogKActivity> by lazy { LogKPrinterView(this) }
    private val _printerMonitor: LogKPrinterMonitor by lazy {
        LogKMgr.instance.getPrinters().filterIsInstance<LogKPrinterMonitor>().getOrNull(0) ?: kotlin.run {
            Log.d(TAG, "_printerMonitor: init")
            LogKPrinterMonitor().also { LogKMgr.instance.addPrinter(it) }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        initPrinterView()
        initPrinterMonitor()

        VB.logkBtnPrint.setOnClickListener {
            printLog()
        }
        VB.logkBtnPrinterList.setOnClickListener {
            printLog1()
        }
    }

    private fun initPrinterView() {
        _printerView.bindLifecycle(this)
        _printerView.toggleView()
    }

    private fun initPrinterMonitor() {
        //添加_printerMonitor的时候一定Application要继承BaseApplication, 因为其中实现了前后台切换的监听
        _printerMonitor.toggle()
    }

    private fun printLog() {
        //初级用法
        LogK.i("just a test1!")

        //中级用法
        LogK.log(CLogType.W, TAG, "just a test2!")

        //高级用法
        LogK.log(object : BaseLogKConfig() {
            override fun includeThread(): Boolean {
                return true
            }

            override fun stackTraceDepth(): Int {
                return 5
            }
        }, CLogType.E, TAG, "just a test3!")
    }

    private fun printLog1() {
        val printers: String = LogKMgr.instance.getPrinters().joinToString { it.getName() }
        LogK.dt(TAG, printers)
    }

    override fun onResume() {
        super.onResume()
        LogKMgr.instance.addPrinter(_printerView)
    }
}