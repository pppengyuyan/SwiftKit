package com.mozhimen.basick.utilk.content

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.mozhimen.basick.elemk.cons.CMediaFormat
import com.mozhimen.basick.elemk.cons.CVersionCode
import com.mozhimen.basick.manifestk.annors.AManifestKRequire
import com.mozhimen.basick.manifestk.cons.CPermission
import com.mozhimen.basick.utilk.content.activity.UtilKActivity
import com.mozhimen.basick.utilk.content.pm.UtilKPackageManager
import com.mozhimen.basick.utilk.java.datatype.UtilKString

/**
 * @ClassName UtilKIntent
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2023/2/26 23:03
 * @Version 1.0
 */
object UtilKIntent {
    /**
     * 选择系统文件
     * @return Intent
     */
    @JvmStatic
    fun getPick(): Intent =
        Intent(Intent.ACTION_PICK)

    /**
     * 选择系统图像
     * @return Intent
     */
    @JvmStatic
    fun getPickImage(): Intent =
        getPick().apply { setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, CMediaFormat.MIMETYPE_IMAGE_ALL) }

    /**
     * 获取设置无障碍
     * @return Intent
     */
    @JvmStatic
    fun getSettingAccessibility(): Intent =
        Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)

    /**
     * 管理APP设置
     * @param context Context
     * @return Intent
     */
    @JvmStatic
    fun getSettingAppDetails(context: Context): Intent =
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, UtilKUri.getPackageUri2(context))

    /**
     * 获取管理所有APP
     * @param context Context
     * @return Intent
     */
    @JvmStatic
    @RequiresPermission(CPermission.MANAGE_EXTERNAL_STORAGE)
    fun getManageAll(context: Context): Intent =
        Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, UtilKUri.getPackageUri(context))

    /**
     * 获取管理悬浮窗
     * @param context Context
     * @return Intent
     */
    @JvmStatic
    @RequiresApi(CVersionCode.V_30_11_R)
    fun getManageOverlay(context: Context): Intent =
        Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, UtilKUri.getPackageUri(context))

    /**
     * 获取管理安装
     * @param context Context
     * @return Intent
     */
    @JvmStatic
    fun getManageInstallSource(context: Context): Intent =
        Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, UtilKUri.getPackageUri(context))

    /**
     * 获取mainLauncher
     * @param packageName String
     * @param launcherActivityName String
     * @return Intent
     */
    @JvmStatic
    fun getMainLauncher(packageName: String, launcherActivityName: String): Intent =
        Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
            setClassName(packageName, launcherActivityName)
        }

    /**
     * 获取mainLauncher
     * @param packageName String
     * @param uri Uri?
     * @return Intent
     */
    @JvmStatic
    fun getMainLauncher(packageName: String, uri: Uri? = null): Intent =
        Intent(Intent.ACTION_MAIN, uri).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
            setPackage(packageName)
        }

    /**
     * 获取启动App的Intent
     * @param packageName String
     * @return Intent?
     */
    @JvmStatic
    fun getLauncherActivity(context: Context, packageName: String): Intent? {
        val launcherActivityName: String = UtilKActivity.getLauncherActivityName(context, packageName)
        if (UtilKString.isHasSpace(launcherActivityName) || launcherActivityName.isEmpty()) return getLauncherFromPackage(context)
        return getMainLauncher(packageName, launcherActivityName)
    }

    /**
     * getLaunchIntentForPackage
     * @return Intent?
     */
    @JvmStatic
    fun getLauncherFromPackage(context: Context): Intent? =
        UtilKPackageManager.get(context).getLaunchIntentForPackage(UtilKContext.getPackageName(UtilKApplication.instance.get()))

    /**
     * 获取安装app的intent
     * @param filePathWithName String
     * @return Intent?
     */
    @JvmStatic
    @RequiresPermission(allOf = [CPermission.REQUEST_INSTALL_PACKAGES])
    fun getInstall(context: Context, filePathWithName: String): Intent? {
        val intent = Intent(Intent.ACTION_VIEW)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//判断安卓系统是否大于7.0  大于7.0使用以下方法
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) //增加读写权限//添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.setDataAndType(UtilKUri.filePathStr2Uri(filePathWithName) ?: return null, "application/vnd.android.package-archive")
        return intent
    }
}