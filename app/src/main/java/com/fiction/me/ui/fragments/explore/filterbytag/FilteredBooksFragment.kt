package com.fiction.me.ui.fragments.explore.filterbytag

import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.adapter.LoadingState
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentFilteredBooksBinding
import com.fiction.me.extensions.startHeaderTextAnimation
import com.fiction.me.utils.cacheImages
import org.koin.androidx.viewmodel.ext.android.viewModel

class FilteredBooksFragment :
    FragmentBaseNCMVVM<FilteredBooksViewModel, FragmentFilteredBooksBinding>() {
    override val viewModel: FilteredBooksViewModel by viewModel()
    override val binding: FragmentFilteredBooksBinding by viewBinding()
    private val args: FilteredBooksFragmentArgs by navArgs()

    override fun onView() {
        with(binding) {
            viewModel.setSelectedTagsIdList(args.tags.toList())
            mainToolbar.run {
                setBackBtnIcon(R.drawable.ic_back)
                setBackBtnStartMargin(0)
            }
            selectedTags.changeVisibilityTitle(false)
            selectedTags.itemSelectedClickListener = { tagId, _ ->
                viewModel.updateSelectedItemsList(tagId)
            }
        }
        observeLoadState()
    }

    override fun onEach() {
        onEach(viewModel.selectedPopularTagsList) { tags ->
            tags?.let {
                binding.selectedTags.submitList(it)
            }
        }

        onEach(viewModel.filteredBooksList) { books ->
            binding.booksInfoView.submitData(lifecycle, books)
        }

        onEach(viewModel.emptyState) {
            popBackStack()
        }

        onEach(viewModel.printMessageAddedLib) {
            binding.customSnackBar.startHeaderTextAnimation(R.string.added_to_library)
            binding.booksInfoView.updateAdapter()
        }
        onEach(viewModel.printMessageRemoveLib) {
            binding.customSnackBar.startHeaderTextAnimation(R.string.remove_from_library)
            binding.booksInfoView.updateAdapter()
        }
    }

    override fun onViewClick() {
        with(binding) {
            mainToolbar.setOnBackBtnClickListener {
                popBackStack()
            }

            booksInfoView.itemSaveClickListener = { books, id, isSaved ->
                viewModel.updateBookItemData(books, id, isSaved)
            }

            booksInfoView.onReadMoreClickListener = { id, _ ->
                val book = booksInfoView.getList().find { it.id == id }
                val directions =
                    FilteredBooksFragmentDirections.actionFilteredBooksFragmentToBookSummaryFragment(
                        id,
                        bookInfo = book?.bookInfo
                    )
                navigateFragment(directions)
            }

            backToTags.setOnClickListener {
                popBackStack()
            }
        }
    }

    private fun observeLoadState() {
        binding.booksInfoView.apply {
            observeLoadState(viewLifecycleOwner) { state, exception ->
                when (state) {
                    LoadingState.LOADED -> {
                        with(binding) {
                           loadingContainer.isVisible = false
                            booksInfoView.getList().forEach { cacheImages(context, it.imageCover) }
                            getIsEmptyList().run {
                                noResultLayout.isVisible = this
                            }
                        }
                    }
                    LoadingState.LOADING -> {
                        binding.loadingContainer.isVisible = true
                    }
                    LoadingState.ERROR -> {
                        exception?.run {

                        }
                    }
                }
            }
        }
    }
}
