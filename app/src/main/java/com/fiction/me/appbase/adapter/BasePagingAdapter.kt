package com.fiction.me.appbase.adapter

import android.annotation.SuppressLint
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.fiction.core.CallException
import com.fiction.core.DiffUtilModel
import com.fiction.domain.utils.Constants
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

abstract class BasePagingAdapter<ItemViewBinding : ViewBinding, Item : DiffUtilModel<*>, ViewHolder : BaseViewHolder<Item, ItemViewBinding>> :
    PagingDataAdapter<Item, ViewHolder>(EventsDiffCallback()) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            getItem(position)?.let { item ->
                bind(item, itemView.context)
                itemView.setOnClickListener {
                    if (position <= itemCount - 1)
                        onItemClick(item)
                }
            }
        }
    }

    class EventsDiffCallback<Item : DiffUtilModel<*>> : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item) =
            (oldItem as DiffUtilModel<*>).id == (newItem as DiffUtilModel<*>).id

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
            oldItem == newItem
    }

    fun observeLoadState(
        viewLifecycleOwner: LifecycleOwner,
        callback: (state: LoadingState, error: CallException?) -> Unit
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            loadStateFlow.debounce(200).collectLatest { state ->
                when (state.refresh) {
                    is LoadState.Loading -> callback(LoadingState.LOADING, null)
                    is LoadState.NotLoading -> callback(LoadingState.LOADED, null)
                    is LoadState.Error -> callback(
                        LoadingState.ERROR,
                        CallException(Constants.ERROR_NULL_DATA)
                    ) // TODO error case
                }
            }
        }
    }
}