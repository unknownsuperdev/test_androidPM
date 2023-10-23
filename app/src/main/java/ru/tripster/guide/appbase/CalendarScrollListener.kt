package ru.tripster.guide.appbase

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


abstract class CalendarScrollListener(
    private val layoutManager: LinearLayoutManager,
    private val isOneSidedScroll: Boolean
) :
    RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (!isLoading) {
            if (!isOneSidedScroll) {
                if (firstVisibleItemPosition == 0 || (visibleItemCount + firstVisibleItemPosition >= totalItemCount)) {
                    loadMoreItems(firstVisibleItemPosition)
                }
            } else {
                if ((visibleItemCount + firstVisibleItemPosition >= totalItemCount)) {
                    loadMoreItems(firstVisibleItemPosition)
                }
            }
        }
    }

    protected abstract fun loadMoreItems(firstVisibleItemPosition: Int)
    abstract val isLoading: Boolean

}