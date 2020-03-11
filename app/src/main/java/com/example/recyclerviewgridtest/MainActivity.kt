package com.example.recyclerviewgridtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewgridtest.databinding.ActivityMainBinding

const val SPAN_COUNT = 4
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Recycler view for header
        binding.headerRecyclerView.apply {
            adapter = GridAdapter(this@MainActivity, createHeaderList(), SPAN_COUNT)
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
            adapter = GridAdapter(this@MainActivity, createItemList(), SPAN_COUNT)
            layoutManager=GridLayoutManager(
                    this@MainActivity, SPAN_COUNT, GridLayoutManager.VERTICAL, false
            )
            addItemDecoration(
                DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
            addItemDecoration(
                DividerItemDecoration(this@MainActivity, DividerItemDecoration.HORIZONTAL))
        }

    }

    private fun createHeaderList(): List<GridData> {
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

    private fun createItemList(): List<GridData> {
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
}
