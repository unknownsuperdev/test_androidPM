package com.fiction.me.ui.fragments.mainlibrary.finished

import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.fiction.domain.model.enums.FeedTypes
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentFinishedBooksBinding
import com.fiction.me.extensions.startHeaderTextAnimation
import com.fiction.me.extensions.toJson
import com.fiction.me.utils.Events.Companion.BOOK_CLICKED
import com.fiction.me.utils.Events.Companion.BOOK_ID_PROPERTY
import com.fiction.me.utils.Events.Companion.BOOK_SUMMARY_SHOWN
import com.fiction.me.utils.Events.Companion.FINISHED
import com.fiction.me.utils.Events.Companion.FINISHED_SCREEN_SHOWN
import com.fiction.me.utils.Events.Companion.OWN_BOOKS
import com.fiction.me.utils.Events.Companion.PLACEMENT
import com.fiction.me.utils.Events.Companion.REFERRER
import com.fiction.me.utils.Events.Companion.SECTION
import com.fiction.me.utils.Events.Companion.SEE_ALL_CLICKED
import com.fiction.me.utils.Events.Companion.USER_LIBRARY
import com.fiction.me.utils.Events.Companion.YOU_MAY_ALSO_LIKE
import com.fiction.me.utils.InternalDeepLink
import org.koin.androidx.viewmodel.ext.android.viewModel

class FinishedBooksFragment :
    FragmentBaseNCMVVM<FinishedBooksViewModel, FragmentFinishedBooksBinding>() {
    override val viewModel: FinishedBooksViewModel by viewModel()
    override val binding: FragmentFinishedBooksBinding by viewBinding()

    override fun onView() {
        with(binding) {
            viewModel.run {
                getFinishedBooks()
                getSuggestedBooksList()
                trackEvents(FINISHED_SCREEN_SHOWN)
            }
            customAppBar.run {
                setTitle(resources.getString(R.string.finished))
                setFirstItemDrawable(R.drawable.ic_back)
                makeLastItemVisible()
            }
            suggestedBooksView.setTitle(resources.getString(R.string.you_may_also_like))
            finishedBooksView.makeSeeAllAndTitleVisible()
            emptyStateLayout.apply {
                emptyStateTv.text = getString(R.string.no_finished_books)
                emptyStateBodyTv.text = getString(R.string.no_finished_books_body)
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.suggestedBooksList) {
            with(binding) {
                suggestedBooksView.submitList(it ?: listOf())
            }
        }
        onEach(viewModel.finishedBooks) { books ->
            books.isNullOrEmpty().run {
                binding.finishedBooksView.isVisible = !this
                binding.emptyStateLayout.root.isVisible = this
            }
            books?.let { binding.finishedBooksView.submitList(it) }
        }
        onEach(viewModel.addedOrRemoveBook) { isSaved ->
            with(binding) {
                if (isSaved == true)
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
            customAppBar.setOnLastItemClickListener {
                val directions =
                    FinishedBooksFragmentDirections.actionFinishedFragmentToFinishedBooksSearchFragment()
                navigateFragment(directions)
            }

            suggestedBooksView.onSuggestedBooksItemClick = { id, isSaved ->
                viewModel.updateSuggestedBooksList(id, isSaved)
            }
            suggestedBooksView.navigateClickListener = { bookId, title ->
                viewModel.run {
                    trackEvents(
                        BOOK_SUMMARY_SHOWN, hashMapOf(
                            REFERRER to FINISHED, SECTION to YOU_MAY_ALSO_LIKE,
                            BOOK_ID_PROPERTY to bookId
                        )
                    )
                    trackEvents(
                        BOOK_CLICKED,
                        hashMapOf(PLACEMENT to USER_LIBRARY, BOOK_ID_PROPERTY to bookId)

                    )
                }
                val book = viewModel.suggestedBooksList.value?.find { it.id == bookId }
                /*val directions =
                    FinishedBooksFragmentDirections.actionFinishedFragmentToBookSummaryFragment(
                        bookId,
                        bookInfo = book?.bookInfo
                    )
                navigateFragment(directions)*/
                val deepLink = InternalDeepLink.makeCustomDeepLinkToBookSummary(
                    bookId,
                    book?.bookInfo?.toJson() ?: ""
                ).toUri()
                navigateFragment(deepLink)
            }
            suggestedBooksView.onClickSeeAll {
                viewModel.trackEvents(
                    SEE_ALL_CLICKED, hashMapOf(
                        PLACEMENT to FINISHED,
                        SECTION to YOU_MAY_ALSO_LIKE
                    )
                )
                val directions =
                    FinishedBooksFragmentDirections.actionFinishedFragmentToYouMayAlsoLikeAllBooksFragment(
                        FeedTypes.YOU_MAY_ALSO_LIKE_FROM_FINISHED
                    )
                navigateFragment(directions)
            }

            finishedBooksView.onClickSeeAll {
                viewModel.trackEvents(
                    SEE_ALL_CLICKED, hashMapOf(
                        PLACEMENT to FINISHED,
                        SECTION to OWN_BOOKS
                    )
                )
                navigateFragment(R.id.seeAllFinishedBooksFragment)
            }
            finishedBooksView.itemClickListener = { bookId, title ->
                sendEvent(bookId)
                val book = viewModel.finishedBooks.value?.find { it.id == bookId }
                /* val directions =
                     FinishedBooksFragmentDirections.actionFinishedFragmentToBookSummaryFragment(
                         bookId,
                         bookInfo = book?.bookInfo
                     )
                 navigateFragment(directions)*/
                val deepLink = InternalDeepLink.makeCustomDeepLinkToBookSummary(
                    bookId,
                    book?.bookInfo?.toJson() ?: ""
                ).toUri()
                navigateFragment(deepLink)
            }
        }
    }

    private fun sendEvent(bookId: Long) {
        viewModel.run {
            viewModel.trackEvents(
                BOOK_SUMMARY_SHOWN,
                hashMapOf(REFERRER to FINISHED, BOOK_ID_PROPERTY to bookId, SECTION to OWN_BOOKS)
            )
            trackEvents(
                BOOK_CLICKED,
                hashMapOf(PLACEMENT to USER_LIBRARY, BOOK_ID_PROPERTY to bookId)
            )
        }
    }
}
