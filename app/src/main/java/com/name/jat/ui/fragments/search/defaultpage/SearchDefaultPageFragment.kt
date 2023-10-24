package com.name.jat.ui.fragments.search.defaultpage

import android.view.WindowManager
import androidx.core.os.bundleOf
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.databinding.FragmentSearchDefaultPageBinding
import com.name.jat.ui.fragments.search.mainsearch.SearchFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchDefaultPageFragment :
    FragmentBaseNCMVVM<SearchDefaultPageViewModel, FragmentSearchDefaultPageBinding>() {
    override val viewModel: SearchDefaultPageViewModel by viewModel()
    override val binding: FragmentSearchDefaultPageBinding by viewBinding()

    override fun onView() {
        with(binding) {
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            popularTagsView.run {
                setTitle("Popular Tags")
                popularTagsView.setSeeAllText("SEE ALL")
            }
            bookInfoView.run {
                makeSeeAllVisible()
                makeTitleVisible()
            }
        }

        with(viewModel) {
            getSuggestedBooksList()
            getPopularTagsList()
        }
    }

    override fun onEach() {
        onEach(viewModel.bookInfoList) { bookInfoList ->

            bookInfoList?.let {
                binding.bookInfoView.submitList(it)
            }
        }

        onEach(viewModel.popularTagsList) { popularTagList ->
            popularTagList?.let {
                binding.popularTagsView.submitList(it)
            }
        }
    }

    override fun onViewClick() {
        with(binding) {
            popularTagsView.itemClickListener = { _ -> }

            popularTagsView.seeAllClickListener = {
                activity?.supportFragmentManager?.setFragmentResult(
                    NAVIGATE_POPULAR_TAGS,
                    bundleOf(IS_NAVIGATE to true)
                )
            }

            bookInfoView.itemSaveClickListener = { id, isSaved ->
                viewModel.updateBookInfoListAdapter(id, isSaved)
            }

            bookInfoView.onReadMoreClickListener = { id ->
                activity?.supportFragmentManager?.setFragmentResult(
                    SearchFragment.NAVIGATE_TO_BOOK_SUMMARY,
                    bundleOf(SearchFragment.ID_OF_BOOK to id)
                )
            }
        }
    }

    companion object {
        const val NAVIGATE_POPULAR_TAGS = "NAVIGATE_POPULAR_TAGS"
        const val IS_NAVIGATE = "IS_NAVIGATE"
    }
}