package com.name.jat.ui.fragments.mainlibrary.browsinghistory

import com.name.jat.R
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.databinding.FragmentBrowsingHistoryBinding
import com.name.jat.extensions.startHeaderTextAnimation
import com.name.jat.ui.fragments.mainlibrary.bottomdialog.ContextMenuActionBottomDialog
import com.name.jat.ui.fragments.mainlibrary.bottomdialog.ContextMenuHistoryBottomDialog.Companion.ADD_TO_LIBRARY
import com.name.jat.ui.fragments.mainlibrary.bottomdialog.ContextMenuHistoryBottomDialog.Companion.DELETE
import com.name.jat.ui.fragments.mainlibrary.bottomdialog.ContextMenuHistoryBottomDialog.Companion.DOWNLOAD
import org.koin.androidx.viewmodel.ext.android.viewModel

class BrowsingHistoryFragment :
    FragmentBaseNCMVVM<BrowsingHistoryViewModel, FragmentBrowsingHistoryBinding>() {
    override val viewModel: BrowsingHistoryViewModel by viewModel()
    override val binding: FragmentBrowsingHistoryBinding by viewBinding()

    override fun onView() {
        viewModel.getHistoryBooksList()
        getFragmentResult()
        with(binding) {
            customAppBar.run {
                setTitle(resources.getString(R.string.browsing_history))
                setFirstItemDrawable(R.drawable.ic_back)
                makeLastItemVisible()
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.historyBookInfoList) { booksList ->
            booksList?.let {
                binding.booksInfoWithSwipe.submitList(booksList)
            }
        }
        onEach(viewModel.afterDeleteCurrentReadBook) {
            binding.customSnackBar.startHeaderTextAnimation(viewModel.message)
        }
    }

    override fun onViewClick() {
        with(binding) {

            booksInfoWithSwipe.run {
                itemSaveClickListener = { id, isSaved ->
                    viewModel.updateBookInfoList(id, isSaved)
                }

                onReadMoreClickListener = { id ->
                    val directions =
                        BrowsingHistoryFragmentDirections.actionBrowsingHistoryFragmentToBookSummaryFragment(
                            id
                        )
                    navigateFragment(directions)
                }

                itemLongClickListener = { id ->
                    val directions =
                        BrowsingHistoryFragmentDirections.actionBrowsingHistoryFragmentToContextMenuHistoryBottomDialog(
                            id
                        )
                    navigateFragment(directions)
                }

                onDownloadClickListener = { id ->
                    val message = resources.getString(
                        R.string._was_downloaded_and_added_to_library,
                        "Honeymoon "
                    )
                    viewModel.message = message
                    viewModel.deleteBookFromLib(id)
                }

                onDeleteClickListener = { id ->
                    val clearDialog =
                        DeleteBookDialog(onDeleteClick = {
                            viewModel.deleteBookFromLib(id)
                        })
                    val message = resources.getString(R.string._was_deleted, "Honeymoon")
                    viewModel.message  = message
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

    private fun getFragmentResult() {

        activity?.supportFragmentManager?.setFragmentResultListener(
            ADD_TO_LIBRARY,
            viewLifecycleOwner
        ) { _, result ->
            with(viewModel) {
                deleteBookFromLib(result.getLong(ContextMenuActionBottomDialog.BOOK_ID))
                viewModel.message = resources.getString(R.string.added_to_library)
            }
        }

        activity?.supportFragmentManager?.setFragmentResultListener(
            DOWNLOAD,
            viewLifecycleOwner
        ) { _, result ->
            with(viewModel) {
                deleteBookFromLib(result.getLong(ContextMenuActionBottomDialog.BOOK_ID))
                viewModel.message = resources.getString(R.string.downloaded_and_added_to_library)
            }
        }

        activity?.supportFragmentManager?.setFragmentResultListener(
            DELETE,
            viewLifecycleOwner
        ) { _, result ->
            with(viewModel) {
                deleteBookFromLib(result.getLong(ContextMenuActionBottomDialog.BOOK_ID))
                viewModel.message = resources.getString(R.string.deleted_from_history)
            }
        }
    }

}