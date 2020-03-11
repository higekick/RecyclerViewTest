package com.example.recyclerviewgridtest

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewgridtest.databinding.ContentItemGridBinding
import com.example.recyclerviewgridtest.databinding.HeaderItemGridBinding
import java.lang.IllegalStateException

const val VIEWTYPE_ITEM = 0
const val VIEWTYPE_SIDE_HEADER = 1

class GridAdapter(context: Context, private val dataList: List<GridData>, private val colSpan: Int) : RecyclerView.Adapter<GridAdapter.MyBaseViewHolder>() {
    private val layoutInflater = LayoutInflater.from(context)

    open class MyBaseViewHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root)
    class ItemViewHolder(binding: ContentItemGridBinding) : MyBaseViewHolder(binding)
    class HeaderViewHolder(binding: HeaderItemGridBinding) : MyBaseViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyBaseViewHolder {
        return when (viewType){
            VIEWTYPE_SIDE_HEADER ->
                HeaderViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.header_item_grid, parent, false))
            VIEWTYPE_ITEM ->
                ItemViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.content_item_grid, parent, false))
            else ->
                throw IllegalStateException("Item View Type is illegal.")
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when {
            itemCount == colSpan -> {
                VIEWTYPE_SIDE_HEADER
            }
            position % colSpan == 0 -> {
                VIEWTYPE_SIDE_HEADER
            }
            else -> {
                VIEWTYPE_ITEM
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holderMy: MyBaseViewHolder, position: Int) {
        val data = dataList[position]
        when (holderMy.binding) {
            is HeaderItemGridBinding -> {
                holderMy.binding.item = data
            }
            is ContentItemGridBinding -> {
                holderMy.binding.item = data
            }
            else -> {
                throw IllegalStateException("Holder is Illegal")
            }
        }
    }

}
