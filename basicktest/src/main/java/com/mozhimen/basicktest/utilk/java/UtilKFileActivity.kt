package com.mozhimen.basicktest.utilk.java

import android.os.Bundle
import android.os.Environment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mozhimen.basick.elemk.activity.bases.BaseActivityVB
import com.mozhimen.basick.manifestk.cons.CPermission
import com.mozhimen.basicktest.BR
import com.mozhimen.basick.utilk.java.io.file.UtilKFile
import com.mozhimen.basicktest.R
import com.mozhimen.basicktest.databinding.ActivityUtilkFileBinding
import com.mozhimen.basicktest.databinding.ItemUtilkFileLogBinding
import com.mozhimen.basick.manifestk.permission.ManifestKPermission
import com.mozhimen.basick.manifestk.permission.annors.APermissionCheck
import com.mozhimen.basick.manifestk.annors.AManifestKRequire
import com.mozhimen.uicorek.adapterk.AdapterKRecyclerVB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AManifestKRequire(CPermission.WRITE_EXTERNAL_STORAGE, CPermission.READ_EXTERNAL_STORAGE)
@APermissionCheck(CPermission.WRITE_EXTERNAL_STORAGE, CPermission.READ_EXTERNAL_STORAGE)
class UtilKFileActivity : BaseActivityVB<ActivityUtilkFileBinding>() {
    private lateinit var _adapterKRecycler: AdapterKRecyclerVB<UtilKFileLogBean, ItemUtilkFileLogBinding>
    private val _logs = arrayListOf(
        UtilKFileLogBean(0, "start file process >>>>>")
    )

    override fun initData(savedInstanceState: Bundle?) {
        ManifestKPermission.initPermissions(this) {
            if (it) {
                VB.utilkFileRecycler.layoutManager = LinearLayoutManager(this)
                _adapterKRecycler = AdapterKRecyclerVB<UtilKFileLogBean, ItemUtilkFileLogBinding>( _logs, R.layout.item_utilk_file_log, BR.item_utilk_file_log)
                VB.utilkFileRecycler.adapter = _adapterKRecycler

                super.initData(savedInstanceState)
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        lifecycleScope.launch(Dispatchers.IO) {
            "section file".log()
            "filePath getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ${this@UtilKFileActivity.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!.absolutePath}".log()
            val deviceInfoPath = this@UtilKFileActivity.filesDir.absolutePath + "/deviceInfo"
            "isFile deviceInfo ${UtilKFile.isFile(deviceInfoPath)}".log()
            val deviceInfo1Path = this@UtilKFileActivity.filesDir.absolutePath + "/deviceInfo1"
            "createFile deviceInfo1 ${UtilKFile.createFile(deviceInfo1Path).absolutePath}".log()
            "deleteFile deviceInfo1 ${UtilKFile.deleteFile(deviceInfo1Path)}".log()
            "getFileSize deviceInfo size ${UtilKFile.getFileSize(deviceInfoPath)}".log()

            val str2File1Path = this@UtilKFileActivity.filesDir.absolutePath + "/tmp1.txt"
            val str2File1Time = System.currentTimeMillis()
            "str2File1 tmp1 ${UtilKFile.str2File("第一行\n第二行", str2File1Path)} time ${System.currentTimeMillis() - str2File1Time}".log()
            val str2File2Path = this@UtilKFileActivity.filesDir.absolutePath + "/tmp2.txt"
            val str2File2Time = System.currentTimeMillis()
            "str2File2 tmp2 ${UtilKFile.str2File2("第一行\n第二行", str2File2Path)} time ${System.currentTimeMillis() - str2File2Time}".log()

            val file2StrTime = System.currentTimeMillis()
            "file2Str tmp ${UtilKFile.file2Str(str2File1Path)} time ${System.currentTimeMillis() - file2StrTime}".log()

            val copyFileTime = System.currentTimeMillis()
            val destTmpFilePath = this@UtilKFileActivity.filesDir.absolutePath + "/tmp3.txt"
            "copyFile tmp -> tmp3 ${UtilKFile.copyFile(str2File1Path, destTmpFilePath)?.absolutePath} time ${System.currentTimeMillis() - copyFileTime}".log()

            "section folder".log()
            val deviceInfoFolder = this@UtilKFileActivity.filesDir.absolutePath
            "isFolder filesDir ${UtilKFile.isFolder(deviceInfoFolder)}".log()
            val createFolderPath = this@UtilKFileActivity.filesDir.absolutePath + "/folder/"
            "createFolder folder ${UtilKFile.createFolder(createFolderPath).absolutePath}".log()
            "deleteFolder folder ${UtilKFile.deleteFolder(createFolderPath)}".log()
        }
    }

    private suspend fun String.log() {
        addLog(this)
    }

    private suspend fun addLog(log: String) {
        withContext(Dispatchers.Main) {
            _logs.add(UtilKFileLogBean(_logs.size, "$log..."))
            _adapterKRecycler.onItemDataChanged(_logs)
        }
    }

    data class UtilKFileLogBean(
        val num: Int,
        val log: String
    ) {
        fun getJoinContent(): String = "$num:$log"
    }
}