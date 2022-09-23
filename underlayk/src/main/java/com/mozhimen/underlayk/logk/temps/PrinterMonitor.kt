package com.mozhimen.underlayk.logk.temps

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.LinearLayout
import com.mozhimen.basick.stackk.StackK
import com.mozhimen.basick.stackk.commons.IStackKListener
import com.mozhimen.underlayk.R
import com.mozhimen.underlayk.logk.commons.IPrinter
import com.mozhimen.underlayk.logk.mos.LogKConfig
import com.mozhimen.basick.utilk.UtilKGlobal
import com.mozhimen.basick.utilk.UtilKOverlay
import com.mozhimen.underlayk.logk.LogK

/**
 * @ClassName PrinterMonitor
 * @Description TODO
 * @Author Kolin Zhao / Mozhimen
 * @Date 2022/9/22 15:52
 * @Version 1.0
 */
class PrinterMonitor : IPrinter {
    private val _printerMonitorProvider: PrinterMonitorProvider = PrinterMonitorProvider(UtilKGlobal.instance.getApp()!!)
    private var _isShow: Boolean = false

    init {
        StackK.addFrontBackListener(object : IStackKListener {
            override fun onChanged(isFront: Boolean) {
                if (isFront) {
                    LogK.dt(TAG, "PrinterMonitor onChanged log start")
                    _printerMonitorProvider.openMonitor(true)
                } else {
                    LogK.wt(TAG, "PrinterMonitor onChanged log stop")
                    _printerMonitorProvider.closeMonitor()
                }
            }
        })
    }

    fun toggleMonitor(isFold: Boolean = true) {
        if (_isShow) {
            _isShow = false
            _printerMonitorProvider.closeMonitor()
        } else {
            _isShow = true
            _printerMonitorProvider.openMonitor(isFold)
        }
    }

    fun getPrinterMonitorProvider(): PrinterMonitorProvider =
        _printerMonitorProvider

    override fun print(config: LogKConfig, level: Int, tag: String, printString: String) {
        _printerMonitorProvider.print(config, level, tag, printString)
    }
}