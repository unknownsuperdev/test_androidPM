package com.name.jat.ui.fragments.search

import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.name.jat.R
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.databinding.FragmentSearchMainBinding
import com.name.jat.extensions.debounce
import com.name.jat.extensions.hideKeyboard
import com.name.jat.extensions.onTextChanged
import com.name.jat.ui.fragments.search.defaultpage.SearchDefaultPageFragment
import com.name.jat.ui.fragments.search.mainsearch.SearchFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchMainFragment : FragmentBaseNCMVVM<SearchMainViewModel, FragmentSearchMainBinding>() {

    override val viewModel: SearchMainViewModel by viewModel()
    override val binding: FragmentSearchMainBinding by viewBinding()
    private lateinit var navController: NavController

    override fun onView() {
        val onSearchTextChange: (String) -> Unit = debounce(
            viewLifecycleOwner.lifecycleScope,
            ::getSearchResults
        )
        with(binding) {
            edSearch.run {
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus && text?.length == 0) {
                        val item = navController.backStack.find {
                            it.destination.id == R.id.searchMainFragment
                        }
                        if (item == null)
                            navController.navigate(R.id.searchMainFragment)
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
            navigateFragment(
                SearchMainFragmentDirections.actionLibraryFragmentToBookSummaryFragment(
                    result.getLong(SearchFragment.ID_OF_BOOK)
                )
            )
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
            SearchFragment.NOT_FOUND_BOOKS_EVENT,
            viewLifecycleOwner
        ) { _, result ->
            with(binding) {
                btnCancel.isGone = true
                icClear.isGone = true
                edSearch.clearFocus()
            }
        }
        activity?.supportFragmentManager?.setFragmentResultListener(
            SearchFragment.QUERY_BY_TAG_EVENT,
            viewLifecycleOwner
        ) { _, result ->
            val tag = result.getString(SearchFragment.TAG_NAME)
            with(binding) {
                edSearch.setText(tag)
                edSearch.setSelection(edSearch.length());
            }
        }

        activity?.supportFragmentManager?.setFragmentResultListener(
            SearchDefaultPageFragment.NAVIGATE_POPULAR_TAGS,
            viewLifecycleOwner
        ) { _, result ->
            with(binding) {
                val directions =
                    SearchMainFragmentDirections.actionLibraryFragmentToFilterByPopularTagsFragment()
                navigateFragment(directions)
            }
        }
    }

    private fun onSearchText(text: String?) {
        activity?.supportFragmentManager?.setFragmentResult(
            WORD_OF_SEARCH,
            bundleOf(WORD to text)
        )
    }

    companion object {
        const val WORD_OF_SEARCH = "WORD_OF_SEARCH"
        const val WORD = "WORD"
    }
}
