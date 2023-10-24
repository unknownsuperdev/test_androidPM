package com.fiction.me.ui.fragments.search.mainsearch

import android.util.Log
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.fiction.domain.model.SearchBookWithName
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentSearchBinding
import com.fiction.me.extensions.hideKeyboard
import com.fiction.me.ui.fragments.search.SearchMainFragment.Companion.WORD
import com.fiction.me.ui.fragments.search.SearchMainFragment.Companion.WORD_OF_SEARCH
import com.fiction.me.ui.fragments.search.adapters.SearchListAdapter
import com.fiction.me.ui.fragments.search.mainsearch.questiondialog.QuestionDialog
import com.fiction.me.utils.Events
import com.fiction.me.utils.Events.Companion.BOOK_ID_PROPERTY
import com.fiction.me.utils.Events.Companion.BOOK_SUMMARY_SHOWN
import com.fiction.me.utils.Events.Companion.PLACEMENT
import com.fiction.me.utils.Events.Companion.REFERRER
import com.fiction.me.utils.Events.Companion.SEARCH_SCREEN
import com.fiction.me.utils.cacheImages
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : FragmentBaseNCMVVM<SearchViewModel, FragmentSearchBinding>() {

    override val viewModel: SearchViewModel by viewModel()
    override val binding: FragmentSearchBinding by viewBinding()
    private lateinit var bookSearchAdapter: SearchListAdapter

    companion object {
        const val NAVIGATE_TO_BOOK_SUMMARY = "NAVIGATE_TO_BOOK_SUMMARY"
        const val ID_OF_BOOK = "ID_OF_BOOK"
        const val BOOK = "BOOK"
        const val CLEAR_RECENT_EVENT = "CLEAR_RECENT_EVENT"
        const val CLEAR_RECENT = "CLEAR_RECENT"
        const val NOT_FOUND_BOOKS_EVENT = "NOT_FOUND_BOOKS_EVENT"
        const val QUERY_BY_TAG_EVENT = "QUERY_BY_TAG_EVENT"
        const val TAG_NAME = "TAG_NAME"
    }

    override fun onView() {
        Log.d("eventProperty", "SearchFragment")
        initSearchAdapter()
        getFragmentResults()
        viewModel.getHistoryList()
    }

    override fun onEach() {
        onEach(viewModel.searchResultsList) { item ->
            binding.progressContainer.isVisible = false
            item?.forEach {
                if (it is SearchBookWithName) context?.let { it1 ->
                    cacheImages(
                        it1,
                        it.imageLink
                    )
                }
            }
            with(binding) {
                bookSearchAdapter.submitList(item)
                if (item.isNullOrEmpty()) {
                    binding.root.hideKeyboard()
                    noResultLayout.isVisible = true
                    searchRv.isGone = true
                    recentSearch.isGone = true
                    clear.isGone = true
                    activity?.supportFragmentManager?.setFragmentResult(
                        NOT_FOUND_BOOKS_EVENT,
                        bundleOf(ID_OF_BOOK to true)
                    )
                } else {
                    if (viewModel.isFoundResult) {
                        noResultLayout.isGone = true
                        searchRv.isVisible = true
                    } else viewModel.isFoundResult = true
                }
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
                sendEvent(item.id)
                activity?.supportFragmentManager?.setFragmentResult(
                    NAVIGATE_TO_BOOK_SUMMARY,
                    bundleOf(BOOK to item.bookInfo, ID_OF_BOOK to item.id)
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
            searchRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
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
                    binding.progressContainer.isVisible = true
                } ?: run {
                    bookSearchAdapter.submitList(emptyList())
                }
            }
        }
    }

    fun sendEvent(id: Long) {
        viewModel.run {
            trackEvents(
                BOOK_SUMMARY_SHOWN, hashMapOf(
                    REFERRER to SEARCH_SCREEN,
                    /*Events.SECTION to Events.CURRENT_READS,*/
                    BOOK_ID_PROPERTY to id
                )
            )
            trackEvents(
                Events.BOOK_CLICKED,
                hashMapOf(BOOK_ID_PROPERTY to id, PLACEMENT to SEARCH_SCREEN)
            )
        }
    }
}
