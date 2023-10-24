package com.fiction.me.ui.fragments.mainlibrary.addedtolibrary

import android.view.WindowManager
import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.adapter.LoadingState
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentLibrarySearchBinding
import com.fiction.me.extensions.debounce
import com.fiction.me.extensions.hideKeyboard
import com.fiction.me.extensions.onTextChanged
import com.fiction.me.extensions.toJson
import com.fiction.me.utils.Events.Companion.ADDED_TO_LIBRARY
import com.fiction.me.utils.Events.Companion.BOOK_CLICKED
import com.fiction.me.utils.Events.Companion.BOOK_ID_PROPERTY
import com.fiction.me.utils.Events.Companion.CURRENT_READS
import com.fiction.me.utils.Events.Companion.PLACEMENT
import com.fiction.me.utils.Events.Companion.REFERRER
import com.fiction.me.utils.Events.Companion.SEARCH
import com.fiction.me.utils.Events.Companion.SEARCH_SCREEN_SHOWN
import com.fiction.me.utils.Events.Companion.SECTION
import com.fiction.me.utils.InternalDeepLink
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddedToLibraryBooksSearchFragment :
    FragmentBaseNCMVVM<AddedToLibraryBooksSearchViewModel, FragmentLibrarySearchBinding>() {

    override val viewModel: AddedToLibraryBooksSearchViewModel by viewModel()
    override val binding: FragmentLibrarySearchBinding by viewBinding()
    var startMode: Int? = null
    override fun onView() {
        val onSearchTextChange: (String) -> Unit = debounce(
            viewLifecycleOwner.lifecycleScope,
            ::getSearchResult
        )
        viewModel.trackEvents(
            SEARCH_SCREEN_SHOWN,
            hashMapOf(REFERRER to ADDED_TO_LIBRARY)
        )
        observeLoadState()
        with(binding) {
            toolBar.run {
                setTitleText(resources.getString(R.string.added_to_library))
                setBackBtnIcon(R.drawable.ic_back)
            }
            startMode = activity?.window?.attributes?.softInputMode
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            edSearch.run {
                edSearch.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus && text?.length!! > 0) {
                        btnCancel.isVisible = true
                        icClear.isVisible = true
                    }
                    if (hasFocus && text?.length == 0) {
                        btnCancel.isVisible = true
                    }
                }
                edSearch.onTextChanged(onSearchTextChange)
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.pagingData) { books ->
            books?.let {
                binding.currentReadBooks.submitData(lifecycle, it)
            }
        }
    }

    override fun onViewClick() {
        with(binding) {
            btnCancel.setOnClickListener {
                edSearch.clearFocus()
                it.hideKeyboard()
                edSearch.text = null
                currentReadBooks.isGone = true
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
                currentReadBooks.isGone = true
                viewModel.clearSearchResult()
            }
            currentReadBooks.itemClickListener = { id, title ->
                sendEvent(id)
                //viewModel.setViewInBook(id)
                val book = currentReadBooks.getList().find { it.id == id }
                /* val directions =
                     AddedToLibraryBooksSearchFragmentDirections.actionAddedToLibraryBooksSearchFragmentToBookSummaryFragment(
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

    private fun observeLoadState() {
        binding.currentReadBooks.apply {
            observeLoadState(viewLifecycleOwner) { state, exception ->
                when (state) {
                    LoadingState.LOADED -> {
                        with(binding) {
                            getIsEmptyList().also {
                                if (it) {
                                    currentReadBooks.isGone = true
                                    noResultLayout.isVisible = true
                                } else {
                                    noResultLayout.isGone = true
                                    currentReadBooks.isVisible = true
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

    private fun sendEvent(id: Long) {
        viewModel.run {
            trackEvents(
                ADDED_TO_LIBRARY, hashMapOf(
                    REFERRER to ADDED_TO_LIBRARY, SECTION to CURRENT_READS,
                    BOOK_ID_PROPERTY to id
                )
            )
            trackEvents(
                BOOK_CLICKED,
                hashMapOf(PLACEMENT to SEARCH, REFERRER to ADDED_TO_LIBRARY, BOOK_ID_PROPERTY to id)
            )
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        startMode?.let { activity?.window?.setSoftInputMode(it) }
    }
}
