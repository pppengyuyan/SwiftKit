package com.mozhimen.underlayk.logk.temps.printer

import com.mozhimen.basick.manifestk.cons.CPermission
import com.mozhimen.basick.manifestk.annors.AManifestKRequire
import com.mozhimen.basick.stackk.StackK
import com.mozhimen.basick.stackk.commons.IStackKListener
import com.mozhimen.basick.utilk.content.UtilKApplication
import com.mozhimen.underlayk.logk.LogK
import com.mozhimen.underlayk.logk.commons.ILogKPrinter
import com.mozhimen.underlayk.logk.bases.BaseLogKConfig

/**
 * @ClassName PrinterMonitor
 * @Description TODO
 * @Author Kolin Zhao / Mozhimen
 * @Date 2022/9/22 15:52
 * @Version 1.0
 */
@AManifestKRequire(CPermission.SYSTEM_ALERT_WINDOW)
class LogKPrinterMonitor : ILogKPrinter {
    private val _printerMonitorProvider: LogKPrinterMonitorProvider by lazy { LogKPrinterMonitorProvider(UtilKApplication.instance.get()) }

    init {
        StackK.addFrontBackListener(object : IStackKListener {
            override fun onChanged(isFront: Boolean) {
                if (!isFront && isOpen()) {
                    LogK.wt(TAG, "PrinterMonitor onChanged log stop")
                    _printerMonitorProvider.close()
                }
//                if (isFront) {
//                    if (_isShow) {
//                        LogK.dt(TAG, "PrinterMonitor onChanged log start")
//                        _printerMonitorProvider.openMonitor(true)
//                    }
//                }
            }
        })
    }

    fun isOpen(): Boolean {
        return _printerMonitorProvider.isOpen()
    }

    fun toggle(isFold: Boolean = true) {
        if (isOpen()) {
            _printerMonitorProvider.close()
        } else {
            _printerMonitorProvider.open(isFold)
        }
    }

    fun getPrinterMonitorProvider(): LogKPrinterMonitorProvider =
        _printerMonitorProvider

    override fun print(config: BaseLogKConfig, level: Int, tag: String, printString: String) {
        _printerMonitorProvider.print(config, level, tag, printString)
    }
}