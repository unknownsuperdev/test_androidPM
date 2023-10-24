package com.fiction.me.ui.fragments.explore.filterbytag

import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.analytics.api.Events.USER_PROPERTY__POPULAR_TAGS_FILTER
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentFilterByPopularTagsBinding
import com.fiction.me.extensions.dpToPx
import com.fiction.me.utils.Events.Companion.PLACEMENT
import com.fiction.me.utils.Events.Companion.POPULAR_TAGS_SCREEN
import com.fiction.me.utils.Events.Companion.TAG_CLICKED
import com.fiction.me.utils.Events.Companion.TAG_NAME
import org.koin.androidx.viewmodel.ext.android.viewModel

class FilterByPopularTagsFragment :
    FragmentBaseNCMVVM<FilterByPopularTagsViewModel, FragmentFilterByPopularTagsBinding>() {
    override val viewModel: FilterByPopularTagsViewModel by viewModel()
    override val binding: FragmentFilterByPopularTagsBinding by viewBinding()

    private var allTagsAdapter =
        AllPopularTagsListAdapter {title, tagListItemId, tagId, isSelected ->
            viewModel.updatePopularTagState(tagListItemId, tagId, isSelected)
            if (isSelected) {
                viewModel.trackEvents(
                    TAG_CLICKED,
                    hashMapOf(TAG_NAME to title, PLACEMENT to POPULAR_TAGS_SCREEN)
                )
                viewModel.setUserPropertyEvent(mapOf(USER_PROPERTY__POPULAR_TAGS_FILTER to title))
            }

        }

    override fun onView() {
        with(binding) {
            mainToolbar.run {
                setTitleText(resources.getString(R.string.popular_tags))
                setBackBtnIcon(R.drawable.ic_back)
            }
            tagsListFilterRV.apply {
                context?.let {
                    layoutManager =
                        LinearLayoutManager(it)
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
                viewModel.apply {
                    clearSelectedTags()
                    selectedTagsList.clear()
                }
            }

            mainToolbar.setOnBackBtnClickListener {
                popBackStack()
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.popularTagsList) {
            allTagsAdapter.submitList(it)
            changeButtonState()
            if (viewModel.isCleared) {
                val action =
                    FilterByPopularTagsFragmentDirections.actionFilterByPopularTagsFragmentToFilteredBooksFragment(
                        viewModel.selectedTagsList.toTypedArray()
                    )
                viewModel.isCleared = false
                navigateFragment(action)
            }
        }
    }

    private fun changeButtonState() {
        viewModel.getIsThereSelectedTags().also { isThereAreSelectedItems ->
            binding.apply.isVisible = isThereAreSelectedItems
            changePaddingTagsRv(isThereAreSelectedItems)
        }
    }

    private fun changePaddingTagsRv(isListNotEmpty: Boolean) {
        context?.let {
            if (isListNotEmpty) {
                binding.tagsListFilterRV.setPadding(0, 0, 0, 80.dpToPx(it))
            } else binding.tagsListFilterRV.setPadding(0, 0, 0, 24.dpToPx(it))
        }
    }
}
