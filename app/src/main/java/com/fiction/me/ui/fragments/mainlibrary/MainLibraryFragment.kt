package com.fiction.me.ui.fragments.mainlibrary

import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.fiction.domain.model.OpenedAllBooks
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentMainLibraryBinding
import com.fiction.me.extensions.startHeaderTextAnimation
import com.fiction.me.extensions.toJson
import com.fiction.me.ui.fragments.mainlibrary.bottomdialog.ContextMenuActionBottomDialog.Companion.BOOK_ID
import com.fiction.me.ui.fragments.mainlibrary.bottomdialog.ContextMenuActionBottomDialog.Companion.DELETE
import com.fiction.me.ui.fragments.mainlibrary.bottomdialog.ContextMenuActionBottomDialog.Companion.FINISH
import com.fiction.me.utils.Events.Companion.BOOK_CLICKED
import com.fiction.me.utils.Events.Companion.BOOK_ID_PROPERTY
import com.fiction.me.utils.Events.Companion.CURRENT_READS
import com.fiction.me.utils.Events.Companion.LIBRARY
import com.fiction.me.utils.Events.Companion.LIBRARY_SCREEN
import com.fiction.me.utils.Events.Companion.LIBRARY_SCREEN_SHOWN
import com.fiction.me.utils.Events.Companion.REFERRER
import com.fiction.me.utils.Events.Companion.SECTION
import com.fiction.me.utils.Events.Companion.USER_LIBRARY
import com.fiction.me.utils.InternalDeepLink
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainLibraryFragment : FragmentBaseNCMVVM<MainLibraryViewModel, FragmentMainLibraryBinding>() {
    override val viewModel: MainLibraryViewModel by viewModel()
    override val binding: FragmentMainLibraryBinding by viewBinding()

    override fun onView() {


        viewModel.trackEvents(LIBRARY_SCREEN_SHOWN)
        getFragmentResult()
        viewModel.getCurrentReadBooks()
        with(binding) {
            emptyStateLayout.apply {
                emptyStateTv.text = getString(R.string.no_reads)
                emptyStateBodyTv.text = getString(R.string.no_reads_body)
            }
            customAppBar.run {
                setTitle(resources.getString(R.string.library))
                makeLastItemVisible()
            }
            currentReadBookView.setTitle(resources.getString(R.string.current_reads))
            currentReadBookView.makeSeeAllAndTitleVisible()
        }
    }

    override fun onEach() {
        with(binding) {
            onEach(viewModel.currentReadBooks) { currentReadBooks ->
                currentReadBooks?.let {
                    currentReadBookView.isVisible = !currentReadBooks.isNullOrEmpty()
                    emptyStateLayout.root.isVisible = currentReadBooks.isNullOrEmpty()
                    currentReadBookView.submitList(it)
                }
            }

            onEach(viewModel.afterDeleteCurrentReadBook) {
                customSnackBar.startHeaderTextAnimation(viewModel.message)
            }
        }
    }

    override fun onViewClick() {
        with(binding) {
            with(currentReadBookView) {
                onClickSeeAll {
                    viewModel.sendEventSeeAllClick()
                    val directions =
                        MainLibraryFragmentDirections.actionMainLibraryFragmentToAllCurrentReadBooksFragment(
                            OpenedAllBooks.FROM_MAIN_LIBRARY
                        )
                    navigateFragment(directions)
                }
                /*itemLongClickListener = { id ->
                    val directions =
                        MainLibraryFragmentDirections.actionMainLibraryToContextMenuBottomDialog(
                            id
                        )
                    navigateFragment(directions)
                }*/
                itemClickListener = { bookId, title ->
                    sendEvent(bookId)
                    val book = viewModel.currentReadBooks.value?.find { it.id == bookId }
                    val deepLink = InternalDeepLink.makeCustomDeepLinkToBookSummary(
                        bookId,
                        book?.bookInfo?.toJson() ?: ""
                    ).toUri()
                    navigateFragment(deepLink)
                }
            }
            customAppBar.setOnLastItemClickListener {
                navigateFragment(R.id.librarySearchFragment)
            }
            finished.setOnClickListener {
                navigateFragment(R.id.finishedFragment)
            }
            browsingHistory.setOnClickListener {
                navigateFragment(R.id.browsingHistoryFragment)
            }
            addedToLibrary.setOnClickListener {
                navigateFragment(R.id.addedToLibraryFragment)
            }
        }
    }

    private fun getFragmentResult() {
        activity?.supportFragmentManager?.setFragmentResultListener(
            FINISH,
            viewLifecycleOwner
        ) { _, result ->
            with(viewModel) {
                deleteBookFromLib(result.getLong(BOOK_ID))
                viewModel.message =
                    resources.getString(R.string._novel_was_moved_to_Finished, "Day and night")
            }
        }

        activity?.supportFragmentManager?.setFragmentResultListener(
            DELETE,
            viewLifecycleOwner
        ) { _, result ->
            with(viewModel) {
                deleteBookFromLib(result.getLong(BOOK_ID))
                viewModel.message = resources.getString(R.string._was_deleted, "Day and night")
            }
        }
    }

    private fun sendEvent(bookId: Long) {
        viewModel.run {
            trackEvents(
                LIBRARY, hashMapOf(
                    REFERRER to LIBRARY_SCREEN, SECTION to CURRENT_READS,
                    BOOK_ID_PROPERTY to bookId
                )
            )
            trackEvents(
                BOOK_CLICKED,
                hashMapOf(REFERRER to USER_LIBRARY, BOOK_ID_PROPERTY to bookId)
            )
        }
    }

    override fun onDestroy() {
        //viewModel.clearCache()
        super.onDestroy()
    }
}