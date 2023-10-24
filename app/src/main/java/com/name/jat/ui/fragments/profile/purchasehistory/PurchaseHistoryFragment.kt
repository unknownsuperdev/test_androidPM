package com.name.jat.ui.fragments.profile.purchasehistory

import android.util.Log
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.name.jat.R
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.databinding.FragmentPurchaseHistoryBinding
import com.name.jat.extensions.debounce
import com.name.jat.extensions.onTextChanged
import org.koin.androidx.viewmodel.ext.android.viewModel

class PurchaseHistoryFragment :
    FragmentBaseNCMVVM<PurchaseHistoryViewModel, FragmentPurchaseHistoryBinding>() {
    override val viewModel: PurchaseHistoryViewModel by viewModel()
    override val binding: FragmentPurchaseHistoryBinding by viewBinding()
    private var purchaseHistoryAdapter = PurchaseHistoryAdapter()

    override fun onView() {
        val onSearchTextChange: (String) -> Unit = debounce(
            viewLifecycleOwner.lifecycleScope,
            ::getSearchResult
        )
        viewModel.getPurchaseHistoryList()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        with(binding) {
            collapseToolbar.run {
                title = resources.getString(R.string.purchase_history)
                setExpandedTitleTextAppearance(R.style.Headings_h1)
                setCollapsedTitleTextAppearance(R.style.Title_Medium_primary_white_18)
            }
            toolbar.setNavigationIcon(R.drawable.ic_back)
            purchaseHistoryRV.apply {
                context?.let {
                    layoutManager =
                        LinearLayoutManager(it)
                    adapter = purchaseHistoryAdapter
                }
                itemAnimator = null
                this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        val firstVisibleItem: Int =
                            (layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                        if (!recyclerView.canScrollVertically(-1) && firstVisibleItem == 0 && newState == RecyclerView.SCROLL_STATE_IDLE) {
                            searchContainer.isVisible = true
                        }
                    }
                })
            }

            appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (collapseToolbar.height + verticalOffset < 2.6 * ViewCompat.getMinimumHeight(
                        collapseToolbar
                    )
                ) {
                    searchContainer.isVisible = false
                }
            })

            edSearch.run {
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus && text?.length == 0) {
                        icClear.isVisible = false
                    }
                    if (hasFocus && text?.length!! > 0) {
                        icClear.isVisible = true
                    }
                }
                onTextChanged(onSearchTextChange)
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.purchaseHistoryBooks) {
            purchaseHistoryAdapter.submitList(it)
        }
    }

    override fun onViewClick() {
        with(binding) {
            toolbar.setNavigationOnClickListener {
                popBackStack()
            }
            lastItem.setOnClickListener {
                edSearch.isVisible = true
            }
            icClear.setOnClickListener {
                edSearch.text = null
            }
        }
    }

    private fun getSearchResult(text: String) {
        if (text.isNotEmpty() && text.isNotBlank()) {
            // viewModel.getSearchResult(text)
            Log.i("TAG", "getSearchResult: ${text.length}")
            binding.icClear.isVisible = true
        } else {
            binding.icClear.isVisible = false
        }
    }

}