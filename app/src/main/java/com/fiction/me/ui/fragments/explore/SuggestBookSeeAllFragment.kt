package com.fiction.me.ui.fragments.explore

import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.adapter.LoadingState
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentSuggestBookSeeAllBinding
import com.fiction.me.extensions.startHeaderTextAnimation
import com.fiction.me.utils.Events.Companion.BOOK_CLICKED
import com.fiction.me.utils.Events.Companion.BOOK_ID_PROPERTY
import com.fiction.me.utils.Events.Companion.EXPLORE
import com.fiction.me.utils.Events.Companion.PLACEMENT
import com.fiction.me.utils.cacheImages
import org.koin.androidx.viewmodel.ext.android.viewModel

class SuggestBookSeeAllFragment :
    FragmentBaseNCMVVM<SuggestBookSeeAllViewModel, FragmentSuggestBookSeeAllBinding>() {

    override val binding: FragmentSuggestBookSeeAllBinding by viewBinding()
    override val viewModel: SuggestBookSeeAllViewModel by viewModel()
    private val args: SuggestBookSeeAllFragmentArgs by navArgs()
    override fun onView() {
        with(binding) {
            customAppBar.setFirstItemDrawable(R.drawable.ic_back)
            customAppBar.setTitle(viewModel.getTitle(args.feedType))
        }
        observeLoadState()
    }

    override fun onEach() {
        onEach(viewModel.printMessageAddedLib) {
            binding.customSnackBar.startHeaderTextAnimation(R.string.added_to_library)
            binding.booksInfoView.updateAdapter()
        }
        onEach(viewModel.printMessageRemoveLib) {
            binding.customSnackBar.startHeaderTextAnimation(R.string.remove_from_library)
            binding.booksInfoView.updateAdapter()
        }
        onEach(viewModel.getAllBooksList(args.feedType, args.id)) {
            binding.booksInfoView.submitData(lifecycle, it)
        }
    }

    override fun onViewClick() {
        with(binding) {
            customAppBar.setOnNavigationClickListener {
                popBackStack()
            }

            booksInfoView.itemSaveClickListener = { books, id, isSaved ->
                viewModel.updateBookItemData(books, id, isSaved)
            }

            booksInfoView.onReadMoreClickListener = { id, title ->
                viewModel.trackEvents(
                    BOOK_CLICKED,
                    hashMapOf(BOOK_ID_PROPERTY to id, PLACEMENT to EXPLORE)
                )
                viewModel.sendEvent(args.feedType, id)
                val book = booksInfoView.getList().find { it.id == id }
                val directions =
                    SuggestBookSeeAllFragmentDirections.actionSuggestBookSeeAllFragmentToBookSummaryFragment(
                        id,
                        bookInfo = book?.bookInfo
                    )
                navigateFragment(directions)
            }
        }
    }

    private fun observeLoadState() {
        binding.booksInfoView.apply {
            observeLoadState(viewLifecycleOwner) { state, exception ->
                when (state) {
                    LoadingState.LOADED -> {
                        with(binding) {
                            booksInfoView.getList().forEach { cacheImages(context, it.imageCover) }
                            loadingContainer.isVisible = false
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
