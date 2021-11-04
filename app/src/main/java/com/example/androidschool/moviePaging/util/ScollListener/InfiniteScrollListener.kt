package com.example.androidschool.moviePaging.util.ScollListener

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class InfiniteScrollListener(val func: () -> Unit, private val layoutManager: LinearLayoutManager): RecyclerView.OnScrollListener() {

    private var previousTotal = 0
    private var loading = true
    private var visibleThreshold = 2
    private var firstVisibleItem = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (dy > 0) {
            visibleItemCount = recyclerView.childCount
            Log.e("vis", visibleItemCount.toString())
            totalItemCount = layoutManager.itemCount
            Log.e("tot", totalItemCount.toString())
            firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
            Log.e("first", firstVisibleItem.toString())
            Log.e("tot+vis", (totalItemCount + visibleItemCount).toString())
            Log.e("fir+vis", (firstVisibleItem + visibleItemCount).toString())

            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false
                    previousTotal = totalItemCount
                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                // End has been reached
                func()
                loading = true
            }
        }
    }
}