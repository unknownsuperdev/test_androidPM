package ru.tripster.guide.appbase.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.tripster.guide.databinding.DefaultLoadStateBinding
import ru.tripster.guide.extensions.makeVisibleGone

/**
 * Action to be executed when Try Again button is pressed
 */

/**
 * This adapter is used for rendering the load state (ProgressBar, error message and Try Again button)
 * in the list's header and footer.
 */
class DefaultLoadStateAdapter() : LoadStateAdapter<DefaultLoadStateAdapter.Holder>() {

    override fun onBindViewHolder(holder: Holder, loadState: LoadState) {
        holder.bind(loadState)
    }
    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DefaultLoadStateBinding.inflate(inflater, parent, false)
        return Holder(binding)
    }

    /**
     * The same layout is used for:
     * - footer
     * - main indicator
     */
    class Holder(
        private val binding: DefaultLoadStateBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState, callback: ((Boolean) -> Unit)? = null) {
            return with(binding) {
                callback?.let {
                    it(loadState is LoadState.Error)
                } ?: run {
                    shimmerLoadMore.isVisible = loadState is LoadState.Loading
                    layout.divider.makeVisibleGone()
                }
            }
        }
    }

}