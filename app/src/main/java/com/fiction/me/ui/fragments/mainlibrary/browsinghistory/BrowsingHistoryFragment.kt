package com.fiction.me.ui.fragments.mainlibrary.browsinghistory

import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.adapter.LoadingState
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentBrowsingHistoryBinding
import com.fiction.me.extensions.startHeaderTextAnimation
import com.fiction.me.extensions.toJson
import com.fiction.me.ui.fragments.mainlibrary.bottomdialog.ContextMenuHistoryBottomDialog.Companion.ADD_TO_LIBRARY
import com.fiction.me.ui.fragments.mainlibrary.bottomdialog.ContextMenuHistoryBottomDialog.Companion.DELETE
import com.fiction.me.ui.fragments.mainlibrary.browsinghistory.deletebook.DeleteBookDialog
import com.fiction.me.utils.Events
import com.fiction.me.utils.Events.Companion.BOOK_CLICKED
import com.fiction.me.utils.Events.Companion.BOOK_ID_PROPERTY
import com.fiction.me.utils.Events.Companion.BOOK_SUMMARY_SHOWN
import com.fiction.me.utils.Events.Companion.BROWSING_HISTORY_SCREEN
import com.fiction.me.utils.Events.Companion.BROWSING_HISTORY_SCREEN_SHOWN
import com.fiction.me.utils.Events.Companion.REFERRER
import com.fiction.me.utils.InternalDeepLink
import org.koin.androidx.viewmodel.ext.android.viewModel

class BrowsingHistoryFragment :
    FragmentBaseNCMVVM<BrowsingHistoryViewModel, FragmentBrowsingHistoryBinding>() {
    override val viewModel: BrowsingHistoryViewModel by viewModel()
    override val binding: FragmentBrowsingHistoryBinding by viewBinding()

    override fun onView() {
        viewModel.trackEvents(BROWSING_HISTORY_SCREEN_SHOWN)
        getFragmentResult()
        observeLoadState()
        with(binding) {
            emptyStateLayout.apply {
                emptyStateTv.text = getString(R.string.no_browsing_history)
                emptyStateBodyTv.text = getString(R.string.no_browsing_history_body)
            }
            customAppBar.run {
                setTitle(resources.getString(R.string.browsing_history))
                setFirstItemDrawable(R.drawable.ic_back)
                makeLastItemVisible()
            }
        }
    }

    override fun onEach() {
        with(binding) {
            onEach(viewModel.pagingData) { booksList ->
                booksInfoWithSwipe.submitData(lifecycle, booksList)
            }
            onEach(viewModel.afterDeleteCurrentReadBook) {
                customSnackBar.startHeaderTextAnimation(viewModel.message)
            }
        }

        onEach(viewModel.addBookToLib) {
            binding.run {
                customSnackBar.startHeaderTextAnimation(R.string.added_to_library)
                booksInfoWithSwipe.updateAdapter()
            }
        }

        onEach(viewModel.removeBookFromLib) {
            binding.run {
                customSnackBar.startHeaderTextAnimation(R.string.deleted_from_library)
                booksInfoWithSwipe.updateAdapter()
            }
        }
    }

    override fun onViewClick() {
        with(binding) {

            booksInfoWithSwipe.run {
                itemSaveClickListener = { books, id, isSaved ->
                    viewModel.addOrRemoveBook(books, id, isSaved)
                }

                onReadMoreClickListener = { id, title ->
                    sendEvent(id)
                    val book = booksInfoWithSwipe.getList().find { it.id == id }
                    /* val directions =
                         BrowsingHistoryFragmentDirections.actionBrowsingHistoryFragmentToBookSummaryFragment(
                             id,
                             bookInfo = book?.bookInfo
                         )
                     navigateFragment(directions)*/
                    val deepLink = InternalDeepLink.makeCustomDeepLinkToBookSummary(id,book?.bookInfo?.toJson() ?: "").toUri()
                    navigateFragment(deepLink)
                }

               /* itemLongClickListener = { id ->
                    val directions =
                        BrowsingHistoryFragmentDirections.actionBrowsingHistoryFragmentToContextMenuHistoryBottomDialog(
                            id
                        )
                    navigateFragment(directions)
                }*/

                onDeleteClickListener = { id ->
                    val clearDialog =
                        DeleteBookDialog(onDeleteClick = {
                            //       viewModel.deleteBookFromLib(id)
                        })
                    val message = resources.getString(R.string._was_deleted, "Honeymoon")
                    viewModel.message = message
                    clearDialog.show(
                        childFragmentManager,
                        context?.resources?.getString(R.string.clear_dialog)
                    )
                }
            }

            customAppBar.run {
                setOnNavigationClickListener {
                    popBackStack()
                }
                setOnLastItemClickListener {
                    navigateFragment(R.id.browsingHistorySearchFragment)
                }
            }
        }
    }

    private fun observeLoadState() {
        binding.booksInfoWithSwipe.apply {
            observeLoadState(viewLifecycleOwner) { state, exception ->
                when (state) {
                    LoadingState.LOADED -> {
                        with(binding) {
                            loadingContainer.isVisible = false
                            getIsEmptyList().also {
                                booksInfoWithSwipe.isVisible = !it
                                emptyStateLayout.root.isVisible = it
                            }
                        }
                    }
                    LoadingState.LOADING -> {
                        binding.loadingContainer.isVisible = true
                    }
                    LoadingState.ERROR -> {
                        binding.loadingContainer.isVisible = false
                        exception?.run {}
                    }
                }
            }
        }
    }

    private fun getFragmentResult() {

        activity?.supportFragmentManager?.setFragmentResultListener(
            ADD_TO_LIBRARY,
            viewLifecycleOwner
        ) { _, result ->
            with(viewModel) {
                //   deleteBookFromLib(result.getLong(ContextMenuActionBottomDialog.BOOK_ID))
                viewModel.message = resources.getString(R.string.added_to_library)
            }
        }

        activity?.supportFragmentManager?.setFragmentResultListener(
            DELETE,
            viewLifecycleOwner
        ) { _, result ->
            with(viewModel) {
                //   deleteBookFromLib(result.getLong(ContextMenuActionBottomDialog.BOOK_ID))
                viewModel.message = resources.getString(R.string.deleted_from_history)
            }
        }
    }

    private fun sendEvent(id: Long) {
        viewModel.run {
            trackEvents(
                BOOK_SUMMARY_SHOWN, hashMapOf(
                    REFERRER to BROWSING_HISTORY_SCREEN, /*SECTION to Events.CURRENT_READS,*/
                    BOOK_ID_PROPERTY to id
                )
            )
            trackEvents(
                BOOK_CLICKED,
                hashMapOf(REFERRER to Events.USER_LIBRARY, BOOK_ID_PROPERTY to id)
            )
        }
    }
}