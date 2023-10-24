package com.name.jat.ui.fragments.search.mainsearch

import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.name.jat.R
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.databinding.FragmentSearchBinding
import com.name.jat.extensions.hideKeyboard
import com.name.jat.ui.fragments.search.SearchMainFragment.Companion.WORD
import com.name.jat.ui.fragments.search.SearchMainFragment.Companion.WORD_OF_SEARCH
import com.name.jat.ui.fragments.search.adapters.SearchListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : FragmentBaseNCMVVM<SearchViewModel, FragmentSearchBinding>() {

    override val viewModel: SearchViewModel by viewModel()
    override val binding: FragmentSearchBinding by viewBinding()
    private lateinit var bookSearchAdapter: SearchListAdapter

    companion object {
        const val NAVIGATE_TO_BOOK_SUMMARY = "NAVIGATE_TO_BOOK_SUMMARY"
        const val ID_OF_BOOK = "ID_OF_BOOK"
        const val CLEAR_RECENT_EVENT = "CLEAR_RECENT_EVENT"
        const val CLEAR_RECENT = "CLEAR_RECENT"
        const val NOT_FOUND_BOOKS_EVENT = "NOT_FOUND_BOOKS_EVENT"
        const val QUERY_BY_TAG_EVENT = "QUERY_BY_TAG_EVENT"
        const val TAG_NAME = "TAG_NAME"
    }

    override fun onView() {
        initSearchAdapter()
        getFragmentResults()
        viewModel.getHistoryList()
    }

    override fun onEach() {
        onEach(viewModel.searchResultsList) {
            with(binding) {
                bookSearchAdapter.submitList(it)
                if (it.isNullOrEmpty()) {
                    noResultLayout.isVisible = true
                    searchRv.isGone = true
                    activity?.supportFragmentManager?.setFragmentResult(
                        NOT_FOUND_BOOKS_EVENT,
                        bundleOf(ID_OF_BOOK to true)
                    )
                    binding.root.hideKeyboard()
                } else {
                    noResultLayout.isGone = true
                    searchRv.isVisible = true
                }
                recentSearch.isGone = true
                clear.isGone = true
            }
        }

        onEach(viewModel.recentInfo) {
            with(binding) {
                recentSearch.isVisible = it
                clear.isVisible = it
            }
        }
    }

    override fun onViewClick() {
        with(binding) {
            clear.setOnClickListener {
                val questionDialog =
                    QuestionDialog(onClearClick = {
                        viewModel.clearSearchHistory()
                        bookSearchAdapter.submitList(emptyList())
                        recentSearch.isGone = true
                        clear.isGone = true
                        activity?.supportFragmentManager?.setFragmentResult(
                            CLEAR_RECENT_EVENT,
                            bundleOf(CLEAR_RECENT to true)
                        )
                    })
                questionDialog.setQuestionText(resources.getString(R.string.are_you_sure_to_clear))
                questionDialog.setPositiveBtnText(resources.getString(R.string.clear))
                questionDialog.show(
                    childFragmentManager,
                    context?.resources?.getString(R.string.clear_dialog)
                )
            }
        }
    }

    private fun initSearchAdapter() {
        bookSearchAdapter = SearchListAdapter(
            onClickSearchItemWithName = { item ->
                activity?.supportFragmentManager?.setFragmentResult(
                    NAVIGATE_TO_BOOK_SUMMARY,
                    bundleOf(ID_OF_BOOK to item.id)
                )
            },
            onClickSearchItemWithTag = { item ->
                viewModel.getSearchResults(item.tagName, true)
                activity?.supportFragmentManager?.setFragmentResult(
                    QUERY_BY_TAG_EVENT,
                    bundleOf(TAG_NAME to item.tagName)
                )
            })

        with(binding) {
            searchRv.layoutManager = object : LinearLayoutManager(context, VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            searchRv.adapter = bookSearchAdapter
        }
    }

    private fun getFragmentResults() {
        activity?.supportFragmentManager?.setFragmentResultListener(
            WORD_OF_SEARCH,
            viewLifecycleOwner
        ) { _, result ->
            with(viewModel) {
                val searchWord = result.getString(WORD)
                searchWord?.let {
                    getSearchResults(searchWord, false)
                } ?: run {
                    viewModel.clearSearchHistory()
                    bookSearchAdapter.submitList(emptyList())
                }
            }
        }
    }
}
