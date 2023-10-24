package com.name.jat.ui.fragments.mainlibrary

import android.view.WindowManager
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.name.jat.R
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.databinding.FragmentLibrarySearchBinding
import com.name.jat.extensions.debounce
import com.name.jat.extensions.hideKeyboard
import com.name.jat.extensions.onTextChanged
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibrarySearchFragment :
    FragmentBaseNCMVVM<LibrarySearchViewModel, FragmentLibrarySearchBinding>() {

    override val viewModel: LibrarySearchViewModel by viewModel()
    override val binding: FragmentLibrarySearchBinding by viewBinding()

    override fun onView() {
        val onSearchTextChange: (String) -> Unit = debounce(
            viewLifecycleOwner.lifecycleScope,
            ::getSearchResult
        )
        with(binding) {
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            toolBar.setTitleText(resources.getString(R.string.library))
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
        onEach(viewModel.searchResult) {
            with(binding) {
                if (it.isNullOrEmpty()) {
                    currentReadBooks.isGone = true
                    noResultLayout.isVisible = true
                } else {
                    noResultLayout.isGone = true
                    currentReadBooks.isVisible = true
                    currentReadBooks.submitList(it)
                }
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
                edSearch.text = null
                currentReadBooks.isGone = true
                viewModel.clearSearchResult()
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
}
