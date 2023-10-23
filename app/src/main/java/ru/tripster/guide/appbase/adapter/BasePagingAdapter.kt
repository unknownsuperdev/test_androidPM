package ru.tripster.guide.appbase.adapter

import android.annotation.SuppressLint
import androidx.lifecycle.*
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.tripster.core.DiffUtilModel

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
        viewLifecycleOwner: LifecycleOwner?,
        callback: (state: LoadingState, error: Throwable?) -> Unit
    ) {
        viewLifecycleOwner?.lifecycleScope?.launch {
            var dataLoading = false

            loadStateFlow.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                minActiveState = Lifecycle.State.RESUMED
            ).collectLatest { state ->

                when (state.refresh) {
                    is LoadState.Loading -> {
                        dataLoading = true
                        callback(
                            LoadingState.LOADING,
                            null
                        )
                    }
                    is LoadState.Error -> {
                        if (dataLoading)
                            callback(
                                LoadingState.ERROR,
                                null
                            )
                    }
                    else -> callback(LoadingState.LOADED, null)
                }
            }
        }
    }

//    fun observeLoadState(
//        viewLifecycleOwner: LifecycleOwner,
//        callback: (state: LoadingState, error: CallException?) -> Unit
//    ) {
//        viewLifecycleOwner.lifecycleScope.launch {
//            sFlow.debounce(200).collectLatest { state ->
//                when (state.refresh) {
//                    is LoadState.Loading -> callback(LoadingState.LOADING, null)
//                    is LoadState.NotLoading -> callback(LoadingState.LOADED, null)
//                    is LoadState.Error -> callback(LoadingState.ERROR, state.toCallException())
//                }
//
////                Log.e("@err", "$state")
////                if (state.refresh !is LoadState.Loading)
////                    callback(state)
//            }
//        }
//    }

//    fun initStateListener(callback: (LoadStat) -> CallException) {
//        addLoadStateListener { loadState ->
//
//            val state = when{
//                loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading -> LoadStat.LOADING
//                loadState.append is LoadState.Error -> LoadStat.ERROR
//                loadState.prepend is LoadState.Error -> LoadStat.ERROR
//                loadState.refresh is LoadState.Error -> LoadStat.ERROR
//                else -> LoadStat.LOADED
//            }
//
//            callback(state)
//
//            // show empty list
//            if (loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading)
//            //viewModel.animateWhiteProgress(true)
//            else {
//                viewModel.animateWhiteProgress(false)
//                // If we have an error, show a toast
//                val errorState = when {
//                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
//                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
//                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
//                    else -> null
//                }
//                viewModel.callExceptionError((errorState?.error as? CallException))
//            }
//        }
//    }

}
