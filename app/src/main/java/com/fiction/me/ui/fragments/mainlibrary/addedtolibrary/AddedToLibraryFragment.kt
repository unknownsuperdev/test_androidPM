package com.fiction.me.ui.fragments.mainlibrary.addedtolibrary

import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.fiction.domain.model.OpenedAllBooks
import com.fiction.domain.model.enums.FeedTypes
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentAddedToLibraryBinding
import com.fiction.me.extensions.startHeaderTextAnimation
import com.fiction.me.extensions.toJson
import com.fiction.me.utils.Events.Companion.ADDED_TO_LIBRARY
import com.fiction.me.utils.Events.Companion.ADDED_TO_LIBRARY_SCREEN_SHOWN
import com.fiction.me.utils.Events.Companion.BOOK_CLICKED
import com.fiction.me.utils.Events.Companion.BOOK_ID_PROPERTY
import com.fiction.me.utils.Events.Companion.BOOK_SUMMARY_SHOWN
import com.fiction.me.utils.Events.Companion.OWN_BOOKS
import com.fiction.me.utils.Events.Companion.PLACEMENT
import com.fiction.me.utils.Events.Companion.REFERRER
import com.fiction.me.utils.Events.Companion.SECTION
import com.fiction.me.utils.Events.Companion.SEE_ALL_CLICKED
import com.fiction.me.utils.Events.Companion.USER_LIBRARY
import com.fiction.me.utils.Events.Companion.YOU_MAY_ALSO_LIKE
import com.fiction.me.utils.InternalDeepLink
import com.fiction.me.utils.cacheImages
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddedToLibraryFragment :
    FragmentBaseNCMVVM<AddedToLibraryViewModel, FragmentAddedToLibraryBinding>() {
    override val viewModel: AddedToLibraryViewModel by viewModel()
    override val binding: FragmentAddedToLibraryBinding by viewBinding()

    override fun onView() {
        with(binding) {
            viewModel.run {
                trackEvents(ADDED_TO_LIBRARY_SCREEN_SHOWN)
                getLibraryBooks()
                getSuggestedBooksList()
            }
            emptyStateLayout.apply {
                emptyStateTv.text = getString(R.string.no_added_books)
                emptyStateBodyTv.text = getString(R.string.no_added_books_body)
            }
            customAppBar.run {
                setTitle(resources.getString(R.string.added_to_library))
                setFirstItemDrawable(R.drawable.ic_back)
                makeLastItemVisible()
            }
            suggestedBooksView.run {
                setTitle(resources.getString(R.string.you_may_also_like))
                setVisibilityForLoading(true)
            }
            addedToLibrary.run {
                makeSeeAllAndTitleVisible()
                setVisibilityForLoading(true)
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.libraryBooks) { books ->
            books.isNullOrEmpty().run {
                binding.addedToLibrary.isVisible = !this
                binding.emptyStateLayout.root.isVisible = this
            }
            books?.let { binding.addedToLibrary.submitList(it) }
            binding.addedToLibrary.setVisibilityForLoading(false)
        }
        onEach(viewModel.suggestedBooksList) { books ->
            books?.let {
                with(binding) {
                    it.forEach { context?.let { it1 -> cacheImages(it1, it.image) } }
                    suggestedBooksView.submitList(it)
                    binding.suggestedBooksView.setVisibilityForLoading(false)
                }
            }
        }
        onEach(viewModel.addedOrRemoveBook) { isAdded ->
            with(binding) {
                if (isAdded == true)
                    customSnackBar.startHeaderTextAnimation(R.string.add_to_library)
                else customSnackBar.startHeaderTextAnimation(R.string.remove_from_library)
            }
        }
    }

    override fun onViewClick() {
        with(binding) {
            customAppBar.setOnNavigationClickListener {
                popBackStack()
            }
            suggestedBooksView.onSuggestedBooksItemClick = { id, isSaved ->
                viewModel.updateSuggestedBooksList(id, isSaved)
                if (isSaved)
                    customSnackBar.startHeaderTextAnimation(R.string.add_to_library)
                else customSnackBar.startHeaderTextAnimation(R.string.remove_from_library)
            }
            suggestedBooksView.navigateClickListener = { bookId, title ->
                sendEvent(bookId)
                val book = viewModel.suggestedBooksList.value?.find { it.id == bookId }
                val deepLink = InternalDeepLink.makeCustomDeepLinkToBookSummary(bookId,book?.bookInfo?.toJson() ?: "").toUri()
                navigateFragment(deepLink)
            }
            suggestedBooksView.onClickSeeAll {
                viewModel.trackEvents(
                    SEE_ALL_CLICKED, hashMapOf(
                        PLACEMENT to ADDED_TO_LIBRARY,
                        SECTION to YOU_MAY_ALSO_LIKE
                    )
                )
                val directions =
                    AddedToLibraryFragmentDirections.actionAddedToLibraryFragmentToYouMayAlsoLikeAllBooksFragment(
                        FeedTypes.YOU_MAY_ALSO_LIKE_FROM_LIBRARY
                    )
                navigateFragment(directions)
            }
            addedToLibrary.itemClickListener = { bookId, title ->
                sendEvent(bookId)
                val book = viewModel.libraryBooks.value?.find { it.id == bookId }
                /*val directions =
                    AddedToLibraryFragmentDirections.actionAddedToLibraryFragmentToBookSummaryFragment(
                        bookId,
                        bookInfo = book?.bookInfo
                    )
                navigateFragment(directions)*/
                val deepLink = InternalDeepLink.makeCustomDeepLinkToBookSummary(bookId,book?.bookInfo?.toJson() ?: "").toUri()
                navigateFragment(deepLink)
            }
            addedToLibrary.onClickSeeAll {
                viewModel.trackEvents(
                    SEE_ALL_CLICKED, hashMapOf(
                        PLACEMENT to ADDED_TO_LIBRARY,
                        SECTION to OWN_BOOKS
                    )
                )
                val directions =
                    AddedToLibraryFragmentDirections.actionAddedToLibraryFragmentToAllCurrentReadBooksFragment(
                        OpenedAllBooks.FROM_ADDED_TO_LIBRARY
                    )
                navigateFragment(directions)
            }
            customAppBar.setOnLastItemClickListener {
                val directions =
                    AddedToLibraryFragmentDirections.actionAddedToLibraryFragmentToAddedToLibraryBooksSearchFragment()
                navigateFragment(directions)
            }
        }
    }

    private fun sendEvent(bookId: Long) {
        viewModel.run {
            trackEvents(
                BOOK_SUMMARY_SHOWN, hashMapOf(
                    REFERRER to ADDED_TO_LIBRARY,
                    SECTION to OWN_BOOKS,
                    BOOK_ID_PROPERTY to bookId
                )
            )
            trackEvents(
                BOOK_CLICKED,
                hashMapOf(BOOK_ID_PROPERTY to bookId, PLACEMENT to USER_LIBRARY)
            )
        }
    }
}
