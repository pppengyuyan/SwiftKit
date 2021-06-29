package com.mozhimen.swiftmk.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * @ClassName BaseFragment
 * @Description TODO
 * @Author Kolin Zhao
 * @Date 2021/6/28 19:16
 * @Version 1.0
 */
abstract class BaseFragment : Fragment() {
    /**
     * 作用: 打印日志
     */
    val mTag = this.javaClass.canonicalName.toString()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return getViewBinding(inflater,container).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData(savedInstanceState)
        initView()
    }

    /**
     * 作用: 回调ViewBinding
     * 用法: private var _vb: FragmentMainBinding? = null
     *  private val vb get() = _vb!!
     *  override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): ViewBinding {
     *      _vb = FragmentMainBinding.inflate(inflater, container, false)
     *      return vb}
     *  override fun onDestroy() {
     *      super.onDestroy()
     *      _vb = null}
     */
    abstract fun getViewBinding(inflater: LayoutInflater,container: ViewGroup?): ViewBinding

    /**
     * 作用: 初始化数据
     */
    abstract fun initData(savedInstanceState: Bundle?)

    /**
     * 作用: 初始化界面
     */
    abstract fun initView()
}