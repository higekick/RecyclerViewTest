package com.example.recyclerviewgridtest

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.recyclerviewgridtest.databinding.ContentItemGridBinding
import com.example.recyclerviewgridtest.databinding.HeaderItemGridBinding
import java.util.*


const val VIEWTYPE_ITEM = 0
const val VIEWTYPE_SIDE_HEADER = 1

class GridAdapter(context: Context, private val dataList: MutableList<GridData>, private val colSpan: Int, private val onStartDragListener: OnStartDragListener)
    : RecyclerView.Adapter<GridAdapter.MyBaseViewHolder>(),
        ItemTouchHelperAdapter {
    private val layoutInflater = LayoutInflater.from(context)

    open class MyBaseViewHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root)
    class ItemViewHolder(binding: ContentItemGridBinding) : MyBaseViewHolder(binding), ItemTouchHelperViewHolder {
        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }
    }

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
                holderMy.binding.textView.setOnTouchListener { v, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        onStartDragListener.onStartDrag(holderMy)
                    }
                    false
                }
            }
            else -> {
                throw IllegalStateException("Holder is Illegal")
            }
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Log.v("ON_ITEM_MOVE", "Log position$fromPosition $toPosition")
        if (fromPosition < dataList.size && toPosition < dataList.size ) {
            if (fromPosition < toPosition) {
                for (i in fromPosition until toPosition) {
                    Collections.swap(dataList, i, i + 1)
                }
            } else {
                for (i in fromPosition downTo toPosition + 1) {
                    Collections.swap(dataList, i, i - 1)
                }
            }
            notifyItemMoved(fromPosition, toPosition)
        }
        return true
    }

    override fun onItemDismiss(position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)
    }

    class EditItemTouchHelperCallback(adapter: GridAdapter) : ItemTouchHelper.Callback() {
        private val mAdapter: GridAdapter = adapter
        override fun isLongPressDragEnabled(): Boolean {
            return true
        }

        override fun isItemViewSwipeEnabled(): Boolean {
            return false
        }

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: ViewHolder
        ): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            return makeMovementFlags(
                dragFlags,
                swipeFlags
            )
        }

        override fun onMove(
            recyclerView: RecyclerView, viewHolder: ViewHolder,
            target: ViewHolder
        ): Boolean {
            mAdapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
            return true
        }

        override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
            mAdapter.onItemDismiss(viewHolder.adapterPosition)
        }

    }

}

interface OnStartDragListener {
    fun onStartDrag(viewHolder: ViewHolder?)
}

interface ItemTouchHelperViewHolder {
    fun onItemSelected()
    fun onItemClear()
}

interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
    fun onItemDismiss(position: Int)
}
