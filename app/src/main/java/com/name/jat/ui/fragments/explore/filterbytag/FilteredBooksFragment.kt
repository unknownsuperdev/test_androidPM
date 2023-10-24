package com.name.jat.ui.fragments.explore.filterbytag

import androidx.core.view.isVisible
import com.name.domain.model.PopularTagsDataModel
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.databinding.FragmentFilteredBooksBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FilteredBooksFragment :
    FragmentBaseNCMVVM<FilteredBooksViewModel, FragmentFilteredBooksBinding>() {
    override val viewModel: FilteredBooksViewModel by viewModel()
    override val binding: FragmentFilteredBooksBinding by viewBinding()

    override fun onView() {
        viewModel.getSuggestedBooksList()
        activity?.supportFragmentManager?.setFragmentResultListener(
            FilterByPopularTagsFragment.NAVIGATE_TO_FILTERED_BOOK,
            viewLifecycleOwner
        ) { _, result ->
            viewModel.setSelectedTagsIdList(
                result.getParcelableArrayList(
                    FilterByPopularTagsFragment.SELECTED_TAGS_IDS
                ) ?: arrayListOf()
            )
        }
        with(binding) {
            selectedTags.changeVisibilityTitle(false)
            selectedTags.itemSelectedClickListener = { tagId, isSelected ->
                viewModel.updateSelectedItemsList(tagId, isSelected)
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.selectedPopularTagsList) { tags ->
            tags?.let {
                binding.selectedTags.submitList(it)
            }
        }

        onEach(viewModel.bookInfoList) { books ->
            books?.let {
                with(binding) {
                    booksInfoView.submitList(it.list)
                }
            }
        }

        onEach(viewModel.emptyBookList) {
            makeNoResultVisible()
        }
    }

    override fun onViewClick() {
        with(binding) {
            applyBtn.setOnClickListener {
                val tags = viewModel.getFilteredListById()
                if (tags?.isEmpty() != true) {
                    viewModel.setSelectedTagsIdList(viewModel.getFilteredListById() as ArrayList<PopularTagsDataModel>)
                } else popBackStack()
            }
            mainToolbar.setOnBackBtnClickListener {
                popBackStack()
            }
            booksInfoView.itemSaveClickListener = { id, isSaved ->
                viewModel.updateBookInfoListAdapter(id, isSaved)
            }
            booksInfoView.onReadMoreClickListener = { id ->
                val directions =
                    FilteredBooksFragmentDirections.actionFilteredBooksFragmentToBookSummaryFragment(
                        id
                    )
                navigateFragment(directions)
            }
            noResult.backToTags.setOnClickListener {
                popBackStack()
            }
        }
    }

    private fun makeNoResultVisible() {
        with(binding) {
            noResult.root.isVisible = true
            btnContainer.isVisible = false
        }
    }
}
