package com.fiction.me.ui.fragments.search

import android.util.Log
import android.view.WindowManager
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.fiction.domain.model.BookInfo
import com.fiction.domain.model.enums.FeedTypes
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentSearchMainBinding
import com.fiction.me.extensions.*
import com.fiction.me.ui.fragments.search.defaultpage.SearchDefaultPageFragment.Companion.FROM_DEFAULT_PAGE
import com.fiction.me.ui.fragments.search.defaultpage.SearchDefaultPageFragment.Companion.FROM_DEFAULT_PAGE_OPEN_TAG
import com.fiction.me.ui.fragments.search.defaultpage.SearchDefaultPageFragment.Companion.NAVIGATE_MOST_POPULAR
import com.fiction.me.ui.fragments.search.defaultpage.SearchDefaultPageFragment.Companion.NAVIGATE_POPULAR_TAGS
import com.fiction.me.ui.fragments.search.defaultpage.SearchDefaultPageFragment.Companion.POPULAR_TAGS
import com.fiction.me.ui.fragments.search.defaultpage.SearchDefaultPageFragment.Companion.POP_UP_BOOK_STATE
import com.fiction.me.ui.fragments.search.mainsearch.SearchFragment
import com.fiction.me.ui.fragments.search.mainsearch.SearchFragment.Companion.NOT_FOUND_BOOKS_EVENT
import com.fiction.me.ui.fragments.search.mainsearch.SearchFragment.Companion.QUERY_BY_TAG_EVENT
import com.fiction.me.ui.fragments.search.mainsearch.SearchFragment.Companion.TAG_NAME
import com.fiction.me.utils.Events.Companion.EXPLORE
import com.fiction.me.utils.Events.Companion.PLACEMENT
import com.fiction.me.utils.Events.Companion.REFERRER
import com.fiction.me.utils.Events.Companion.SEARCH_SCREEN_SHOWN
import com.fiction.me.utils.InternalDeepLink
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchMainFragment : FragmentBaseNCMVVM<SearchMainViewModel, FragmentSearchMainBinding>() {

    override val viewModel: SearchMainViewModel by viewModel()
    override val binding: FragmentSearchMainBinding by viewBinding()
    private lateinit var navController: NavController
    var startMode: Int? = null
    override fun onView() {
        startMode = activity?.window?.attributes?.softInputMode
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        viewModel.trackEvents(SEARCH_SCREEN_SHOWN, hashMapOf(REFERRER to EXPLORE))
        val onSearchTextChange: (String) -> Unit = debounce(
            viewLifecycleOwner.lifecycleScope,
            ::getSearchResults
        )
        with(binding) {
            edSearch.run {
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus && text?.length == 0) {
                        val item = navController.backQueue.find {
                            it.destination.id == R.id.searchFragment
                        }
                        if (item == null)
                            navController.navigate(R.id.searchFragment)
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
        activity?.let {
            navController = Navigation.findNavController(it, R.id.nav_search)
        }
        getFragmentResults()
    }

    override fun onViewClick() {
        with(binding) {
            btnCancel.setOnClickListener {
                edSearch.clearFocus()
                it.hideKeyboard()
                edSearch.text = null
                onSearchText(null)
                it.isGone = true
                icClear.isGone = true
                navController.popBackStack()
            }
            back.setOnClickListener {
                it.hideKeyboard()
                popBackStack()
            }
            icClear.setOnClickListener {
                icClear.isVisible = false
                edSearch.text = null
                onSearchText(null)
            }
        }
    }

    private fun getSearchResults(text: String) {
        if (text.isNotEmpty() && text.isNotBlank()) {
            onSearchText(text)
            binding.icClear.isVisible = true
        } else {
            binding.icClear.isVisible = false
        }
    }

    private fun getFragmentResults() {
        activity?.supportFragmentManager?.setFragmentResultListener(
            SearchFragment.NAVIGATE_TO_BOOK_SUMMARY,
            viewLifecycleOwner
        ) { _, result ->
            val book = result.getParcelable<BookInfo>(SearchFragment.BOOK)
            val bookId = result.getLong(SearchFragment.ID_OF_BOOK)
           // viewModel.setViewInBook(bookId)
       /*     navigateFragment(
                SearchMainFragmentDirections.actionLibraryFragmentToBookSummaryFragment(
                    bookId,
                    bookInfo = book
                )
            )*/
            val deepLink = InternalDeepLink.makeCustomDeepLinkToBookSummary(bookId,book?.toJson() ?: "").toUri()
            navigateFragment(deepLink)
        }

        activity?.supportFragmentManager?.setFragmentResultListener(
            SearchFragment.CLEAR_RECENT_EVENT,
            viewLifecycleOwner
        ) { _, _ ->
            with(binding) {
                btnCancel.isGone = true
                icClear.isGone = true
                edSearch.clearFocus()
            }
        }

        activity?.supportFragmentManager?.setFragmentResultListener(
            NOT_FOUND_BOOKS_EVENT,
            viewLifecycleOwner
        ) { _, result ->
            with(binding) {
                btnCancel.isGone = true
                icClear.isGone = true
                edSearch.clearFocus()
            }
        }

        activity?.supportFragmentManager?.setFragmentResultListener(
            QUERY_BY_TAG_EVENT,
            viewLifecycleOwner
        ) { _, result ->
            val tag = result.getString(TAG_NAME)
            with(binding) {
                edSearch.setText(tag)
                edSearch.setSelection(edSearch.length())
            }
        }

        activity?.supportFragmentManager?.setFragmentResultListener(
            NAVIGATE_POPULAR_TAGS,
            viewLifecycleOwner
        ) { _, result ->
            val directions =
                SearchMainFragmentDirections.actionLibraryFragmentToFilterByPopularTagsFragment()
            navigateFragment(directions)
        }

        activity?.supportFragmentManager?.setFragmentResultListener(
            NAVIGATE_MOST_POPULAR,
            viewLifecycleOwner
        ) { _, result ->
            val directions =
                SearchMainFragmentDirections.actionSearchMainFragmentToSuggestBookSeeAllFragment(
                    FeedTypes.MOST_POPULAR
                )
            navigateFragment(directions)
        }

        activity?.supportFragmentManager?.setFragmentResultListener(
            FROM_DEFAULT_PAGE,
            viewLifecycleOwner
        ) { _, result ->
            val isAddedToLib = result.getBoolean(POP_UP_BOOK_STATE)
            val message =
                if (isAddedToLib) resources.getString(R.string.added_to_library) else resources.getString(
                    R.string.remove_from_library
                )
            binding.customSnackBar.startHeaderTextAnimation(message)
        }

        activity?.supportFragmentManager?.setFragmentResultListener(
            FROM_DEFAULT_PAGE_OPEN_TAG,
            viewLifecycleOwner
        ) { _, result ->
            val tagId = result.getLong(POPULAR_TAGS)
            val directions =
                SearchMainFragmentDirections.actionSearchMainFragmentToBooksByTagFragment(tagId)
            navigateFragment(directions)
        }
    }


    private fun onSearchText(text: String?) {
        activity?.supportFragmentManager?.setFragmentResult(
            WORD_OF_SEARCH,
            bundleOf(WORD to text)
        )
    }
    override fun onDestroyView() {
        super.onDestroyView()
        startMode?.let { activity?.window?.setSoftInputMode(it) }
    }
    companion object {
        const val WORD_OF_SEARCH = "WORD_OF_SEARCH"
        const val WORD = "WORD"
    }
}
