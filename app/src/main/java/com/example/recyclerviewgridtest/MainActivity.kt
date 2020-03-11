package com.example.recyclerviewgridtest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewgridtest.GridAdapter.EditItemTouchHelperCallback
import com.example.recyclerviewgridtest.databinding.ActivityMainBinding


const val SPAN_COUNT = 5
class MainActivity : AppCompatActivity(), OnStartDragListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mItemTouchHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Recycler view for header
        binding.headerRecyclerView.apply {
            adapter = GridAdapter(this@MainActivity, createHeaderList(), SPAN_COUNT, this@MainActivity)
            layoutManager=GridLayoutManager(
                this@MainActivity, SPAN_COUNT, GridLayoutManager.VERTICAL, false
            )
            addItemDecoration(
                DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
            addItemDecoration(
                DividerItemDecoration(this@MainActivity, DividerItemDecoration.HORIZONTAL))
        }

        // Recycler view for contents
        binding.itemRecyclerView.apply {
            adapter = GridAdapter(this@MainActivity, createItemList(), SPAN_COUNT, this@MainActivity)
            layoutManager=GridLayoutManager(
                    this@MainActivity, SPAN_COUNT, GridLayoutManager.VERTICAL, false
            )
            addItemDecoration(
                DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
            addItemDecoration(
                DividerItemDecoration(this@MainActivity, DividerItemDecoration.HORIZONTAL))
            val callback: ItemTouchHelper.Callback = EditItemTouchHelperCallback(this.adapter as GridAdapter)
            mItemTouchHelper = ItemTouchHelper(callback)
            mItemTouchHelper.attachToRecyclerView(this)
        }


    }

    private fun createHeaderList(): MutableList<GridData> {
        val dataList = mutableListOf<GridData>()
        for(i in 0 until SPAN_COUNT) {
            dataList.add(
                GridData(
                    text = "header$i"
                )
            )
        }
        return dataList
    }

    private fun createItemList(): MutableList<GridData> {
        val dataList = mutableListOf<GridData>()
        for(i in 0 until 100) {
            dataList.add(
                    GridData(
                            text = "test$i"
                    )
            )
        }
        return dataList
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder?) {
        viewHolder?.let {
            mItemTouchHelper.startDrag(it)
        }
    }
}
