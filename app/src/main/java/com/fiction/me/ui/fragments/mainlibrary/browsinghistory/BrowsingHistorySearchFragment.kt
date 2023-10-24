package com.fiction.me.ui.fragments.mainlibrary.browsinghistory

import android.view.WindowManager
import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.adapter.LoadingState
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentBrowsingHistorySearchBinding
import com.fiction.me.extensions.*
import com.fiction.me.utils.Events.Companion.BOOK_CLICKED
import com.fiction.me.utils.Events.Companion.BOOK_ID_PROPERTY
import com.fiction.me.utils.Events.Companion.BOOK_SUMMARY_SHOWN
import com.fiction.me.utils.Events.Companion.BROWSING_HISTORY
import com.fiction.me.utils.Events.Companion.PLACEMENT
import com.fiction.me.utils.Events.Companion.REFERRER
import com.fiction.me.utils.Events.Companion.SEARCH_SCREEN
import com.fiction.me.utils.Events.Companion.SEARCH_SCREEN_SHOWN
import com.fiction.me.utils.Events.Companion.USER_LIBRARY
import com.fiction.me.utils.InternalDeepLink
import org.koin.androidx.viewmodel.ext.android.viewModel

class BrowsingHistorySearchFragment :
    FragmentBaseNCMVVM<BrowsingHistorySearchViewModel, FragmentBrowsingHistorySearchBinding>() {

    override val viewModel: BrowsingHistorySearchViewModel by viewModel()
    override val binding: FragmentBrowsingHistorySearchBinding by viewBinding()
    var startMode: Int? = null
    override fun onView() {
        viewModel.trackEvents(
            SEARCH_SCREEN_SHOWN,
            hashMapOf(REFERRER to BROWSING_HISTORY)
        )
        val onSearchTextChange: (String) -> Unit = debounce(
            viewLifecycleOwner.lifecycleScope,
            ::getSearchResult
        )
        observeLoadState()
        with(binding) {
            toolBar.run {
                setTitleText(resources.getString(R.string.browsing_history))
                setBackBtnIcon(R.drawable.ic_back)
            }
            startMode = activity?.window?.attributes?.softInputMode
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            edSearch.run {
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus && text?.length == 0) {
                        btnCancel.isVisible = true
                    }
                    if (hasFocus && text?.length!! > 0) {
                        btnCancel.isVisible = true
                        icClear.isVisible = true
                    }
                }
                onTextChanged(onSearchTextChange)
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.pagingData) { books ->
            books?.let {
                binding.booksView.submitData(lifecycle, it)
            }
        }

        onEach(viewModel.addBookToLib) {
            with(binding) {
                customSnackBar.startHeaderTextAnimation(R.string.added_to_library)
                booksView.updateAdapter()
            }
        }

        onEach(viewModel.removeBookFromLib) {
            with(binding) {
                customSnackBar.startHeaderTextAnimation(R.string.deleted_from_library)
                booksView.updateAdapter()
            }
        }
    }

    override fun onViewClick() {
        with(binding) {
            btnCancel.setOnClickListener {
                edSearch.clearFocus()
                it.hideKeyboard()
                edSearch.text = null
                booksView.isGone = true
                it.isGone = true
                icClear.isGone = true
                viewModel.clearSearchResult()
                popBackStack()
            }

            toolBar.setOnBackBtnClickListener {
                toolBar.hideKeyboard()
                popBackStack()
            }

            icClear.setOnClickListener {
                icClear.isVisible = false
                edSearch.text = null
                booksView.isGone = true
                viewModel.clearSearchResult()
            }

            booksView.itemSaveClickListener = { books, id, isSaved ->
                viewModel.addOrRemoveBook(books, id, isSaved)
            }

            booksView.onReadMoreClickListener = { id, title ->
                sendEvent(id)
                //viewModel.setViewInBook(id)
                val book = booksView.getList().find { it.id == id }
               /* val directions =
                    BrowsingHistorySearchFragmentDirections.actionBrowsingHistorySearchFragmentToBookSummaryFragment(
                        id,
                        bookInfo = book?.bookInfo
                    )
                navigateFragment(directions)*/
                val deepLink = InternalDeepLink.makeCustomDeepLinkToBookSummary(id,book?.bookInfo?.toJson() ?: "").toUri()
                navigateFragment(deepLink)
            }
        }
    }

    private fun getSearchResult(text: String) {
        if (text.isNotEmpty() && text.isNotBlank()) {
            viewModel.getSearchResult(text)
            binding.icClear.isVisible = true
        } else {
            binding.icClear.isVisible = false
        }
    }

    private fun sendEvent(id: Long) {
        viewModel.run {
            trackEvents(
                BOOK_SUMMARY_SHOWN, hashMapOf(
                    REFERRER to SEARCH_SCREEN, /*SECTION to ,*/
                    BOOK_ID_PROPERTY to id
                )
            )
            trackEvents(
                BOOK_CLICKED,
                hashMapOf(BOOK_ID_PROPERTY to id, PLACEMENT to USER_LIBRARY)
            )
        }
    }

    private fun observeLoadState() {
        binding.booksView.apply {
            observeLoadState(viewLifecycleOwner) { state, exception ->
                when (state) {
                    LoadingState.LOADED -> {
                        with(binding) {
                            getIsEmptyList().also {
                                if (it) {
                                    booksView.isGone = true
                                    noResultLayout.isVisible = true
                                } else {
                                    noResultLayout.isGone = true
                                    booksView.isVisible = true
                                }
                            }
                        }
                    }
                    LoadingState.LOADING -> {

                    }
                    LoadingState.ERROR -> {
                        exception?.run {

                        }
                    }
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        startMode?.let { activity?.window?.setSoftInputMode(it) }
    }
}
