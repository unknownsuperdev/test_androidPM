package com.name.jat.ui.fragments.explore.filterbytag

import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.name.jat.R
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.databinding.FragmentFilterByPopularTagsBinding
import com.name.jat.extensions.dpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel

class FilterByPopularTagsFragment :
    FragmentBaseNCMVVM<FilterByPopularTagsViewModel, FragmentFilterByPopularTagsBinding>() {
    override val viewModel: FilterByPopularTagsViewModel by viewModel()
    override val binding: FragmentFilterByPopularTagsBinding by viewBinding()

    companion object {
        val NAVIGATE_TO_FILTERED_BOOK = "navigate to filtered book"
        val SELECTED_TAGS_IDS = "selected tags ids"
    }

    private var allTagsAdapter =
        AllPopularTagsListAdapter { tagListItemId, tagId, isSelected ->
            viewModel.updatePopularTagState(tagListItemId, tagId, isSelected)
        }

    override fun onView() {
        with(binding) {
            mainToolbar.setTitleText(resources.getString(R.string.popular_tags))
            tagsListFilterRV.apply {
                context?.let {
                    layoutManager =
                        LinearLayoutManager(it)
                    setHasFixedSize(true)
                    adapter = allTagsAdapter
                }

                itemAnimator = null
            }
        }
        viewModel.getPopularTagsList()
    }

    override fun onViewClick() {
        with(binding) {
            apply.setOnClickListener {
                activity?.supportFragmentManager?.setFragmentResult(
                    NAVIGATE_TO_FILTERED_BOOK,
                    bundleOf(SELECTED_TAGS_IDS to viewModel.getSelectedTagsList())
                )
                navigateFragment(R.id.filteredBooksFragment)
            }

            mainToolbar.setOnBackBtnClickListener {
                popBackStack()
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.popularTagsList) {
            val isListNotEmpty = viewModel.getSelectedTagsList().isNotEmpty()
            binding.apply.isVisible = isListNotEmpty
            changePaddingTagsRv(isListNotEmpty)
            allTagsAdapter.submitList(it)
        }
    }

    private fun changePaddingTagsRv(isListNotEmpty: Boolean) {
        context?.let {
            if (isListNotEmpty) {
                binding.tagsListFilterRV.setPadding(0, 0, 0, 80.dpToPx(it))
            } else binding.tagsListFilterRV.setPadding(0, 0, 0, 24.dpToPx(it))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearSelectedTagsList()
    }
}
