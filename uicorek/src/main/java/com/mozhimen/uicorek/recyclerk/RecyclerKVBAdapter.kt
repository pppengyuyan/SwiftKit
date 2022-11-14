package com.mozhimen.uicorek.recyclerk

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * @ClassName RecyclerAdapterK
 * @Description TODO
 * @Author Kolin Zhao
 * @Date 2021/6/4 20:07
 * @Version 1.0
 */
/**
 * 作用: 通用RecyclerView适配器
 * 用法: viewBinding.mainList.layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
 * val adapter: RecyclerAdapterK<User> = object : RecyclerAdapterK<User>(viewModel.userList, R.layout.item_user, BR.item) {
 *  override fun addListener(view: View, itemData: User, position: Int) {
 *      (view.findViewById(R.id.user_pane) as LinearLayout).setOnClickListener {
 *          //逻辑
 * }}}
 * viewBinding.mainList.adapter=adapter
 */
typealias IAdapterKRecyclerListener<BEAN, VB> = (holder: RecyclerKVBViewHolder<VB>, item: BEAN, position: Int) -> Unit

open class AdapterKRecycler<BEAN, VB : ViewDataBinding>(
    private var _itemDatas: List<BEAN>,
    private val _defaultLayout: Int,
    private val _brId: Int,
    private val _listener: IAdapterKRecyclerListener<BEAN, VB>? = null /* = (com.mozhimen.uicorek.recyclerk.datak.BindKViewHolder<androidx.databinding.ViewDataBinding>, T, kotlin.Int) -> kotlin.Unit */
) : RecyclerView.Adapter<RecyclerKVBViewHolder<VB>>() {

    @SuppressLint("NotifyDataSetChanged")
    fun onItemDataChanged(newItemDatas: List<BEAN>) {
        _itemDatas = newItemDatas
        notifyDataSetChanged()
    }

    fun onItemRangeChanged(newItemDatas: List<BEAN>, positionStart: Int, itemCount: Int) {
        _itemDatas = newItemDatas
        notifyItemChanged(positionStart, itemCount)
    }

    fun onItemRangeInserted(newItemDatas: List<BEAN>, positionStart: Int, itemCount: Int) {
        _itemDatas = newItemDatas
        notifyItemRangeInserted(positionStart, itemCount)
    }

    fun onItemRangeRemoved(newItemDatas: List<BEAN>, positionStart: Int, itemCount: Int) {
        _itemDatas = newItemDatas
        notifyItemRangeRemoved(positionStart, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerKVBViewHolder<VB> {
        val binding = DataBindingUtil.inflate<VB>(
            LayoutInflater.from(parent.context),
            viewType,
            parent,
            false
        )
        return RecyclerKVBViewHolder(binding.root, binding)
    }

    override fun getItemCount() = if (_itemDatas.isEmpty()) 0 else _itemDatas.size

    override fun onBindViewHolder(holder: RecyclerKVBViewHolder<VB>, position: Int) {
        holder.vb.setVariable(_brId, _itemDatas[position])
        _listener?.invoke(holder, _itemDatas[position], position)
        holder.vb.executePendingBindings()
    }

    override fun getItemViewType(position: Int) = _defaultLayout
}