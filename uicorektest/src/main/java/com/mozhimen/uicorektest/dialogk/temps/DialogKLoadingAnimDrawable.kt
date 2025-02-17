package com.mozhimen.uicorektest.dialogk.temps

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.mozhimen.basick.manifestk.cons.CPermission
import com.mozhimen.basick.manifestk.annors.AManifestKRequire
import com.mozhimen.basick.utilk.res.UtilKRes
import com.mozhimen.uicorek.dialogk.bases.BaseDialogK
import com.mozhimen.uicorek.dialogk.commons.IDialogKClickListener
import com.mozhimen.uicorektest.R

/**
 * @ClassName DialogKLoading
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/11/24 23:39
 * @Version 1.0
 */
@AManifestKRequire(CPermission.SYSTEM_ALERT_WINDOW)
class DialogKLoadingAnimDrawable @JvmOverloads internal constructor(context: Context, desc: String? = null) : BaseDialogK<IDialogKClickListener>(context, R.style.DialogK_Theme_Translucent) {
    private var _imgProgress: ImageView? = null
    private var _txtDesc: TextView? = null
    private var _txtAction: TextView? = null
    private var _animationDrawable: AnimationDrawable? = null
    private var _descStr: String?
    private var _actionStr: String? = null

    init {
        _descStr = desc
        setDialogCancelable(true)
        setOnDismissListener {
            if (_animationDrawable != null) {
                _animationDrawable!!.selectDrawable(0)
                _animationDrawable!!.setVisible(false, true)
                _animationDrawable!!.stop()
            }
        }
        setOnShowListener {
            if (_animationDrawable != null) {
                _animationDrawable!!.setVisible(true, true)
                _animationDrawable!!.start()
            }
        }
    }

    companion object {
        @JvmOverloads
        fun create(context: Context, desc: String? = null): DialogKLoadingAnimDrawable {
            return DialogKLoadingAnimDrawable(context, desc)
        }
    }

    override fun onCreateView(inflater: LayoutInflater): View {
        return inflater.inflate(R.layout.dialogk_loading_anim_drawable, null)
    }

    override fun onFindView(dialogView: View) {
        _imgProgress = dialogView.findViewById(R.id.dialogk_loading_img_progress)
        _txtDesc = dialogView.findViewById(R.id.dialogk_loading_txt_desc)
        _txtAction = dialogView.findViewById(R.id.dialogk_loading_txt_action)
        _animationDrawable = UtilKRes.getDrawable(R.drawable.anim_dialogk_loading) as AnimationDrawable
        _imgProgress!!.setImageDrawable(_animationDrawable)
        if (!TextUtils.isEmpty(_descStr)) {
            _txtDesc!!.text = _descStr
        }
    }

    fun setDesc(desc: String) {
        _descStr = desc
        if (this._txtDesc != null) {
            this._txtDesc!!.text = desc
        }
    }

    fun setAction(desc: String) {
        _actionStr = desc
        if (_txtAction != null) {
            _txtAction!!.text = desc
            if (TextUtils.isEmpty(desc)) {
                _txtAction!!.visibility = View.GONE
            } else {
                _txtAction!!.visibility = View.VISIBLE
            }
        }
    }

    fun setAction(desc: String, listener: View.OnClickListener) {
        _actionStr = desc
        if (_txtAction != null) {
            _txtAction!!.text = desc
            _txtAction!!.setOnClickListener(listener)
            if (TextUtils.isEmpty(desc)) {
                _txtAction!!.visibility = View.GONE
            } else {
                _txtAction!!.visibility = View.VISIBLE
            }
        }
    }
}
