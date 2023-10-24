package com.name.jat.ui.fragments.mainlibrary.downloaded

import com.name.jat.R
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.databinding.FragmentDownloadedBooksBinding
import com.name.jat.extensions.startHeaderTextAnimation
import com.name.jat.ui.fragments.mainlibrary.bottomdialog.ContextMenuDownloadedBottomDialog.Companion.BOOK_ID
import com.name.jat.ui.fragments.mainlibrary.bottomdialog.ContextMenuDownloadedBottomDialog.Companion.DELETE
import com.name.jat.ui.fragments.mainlibrary.bottomdialog.ContextMenuDownloadedBottomDialog.Companion.DOWNLOAD_REMOVE
import com.name.jat.ui.fragments.mainlibrary.bottomdialog.ContextMenuDownloadedBottomDialog.Companion.FINISH
import com.name.jat.ui.fragments.mainlibrary.bottomdialog.ContextMenuDownloadedBottomDialog.Companion.TYPE
import org.koin.androidx.viewmodel.ext.android.viewModel

class DownloadedBooksFragment :
    FragmentBaseNCMVVM<DownloadedBooksViewModel, FragmentDownloadedBooksBinding>() {
    override val viewModel: DownloadedBooksViewModel by viewModel()
    override val binding: FragmentDownloadedBooksBinding by viewBinding()

    override fun onView() {
        with(binding) {
            viewModel.getCurrentReadBooksList()
            viewModel.getNextInLineBooksList()
            getFragmentResult()
            customAppBar.run {
                setTitle(resources.getString(com.name.jat.R.string.downloaded))
                setFirstItemDrawable(R.drawable.ic_back)
                makeLastItemVisible()
            }
            currentReadBookView.setTitle(resources.getString(R.string.current_reads))
            currentReadBookView.makeSeeAllAndTitleVisible()
            nextInLineBooksView.setTitle(resources.getString(R.string.next_in_line))
        }
    }

    override fun onEach() {
        onEach(viewModel.currentReadBooks) { currentReadBooks ->
            currentReadBooks?.let {
                binding.currentReadBookView.submitList(it)
            }
        }
        onEach(viewModel.nextInLineBooks) { nextInLineBooks ->
            nextInLineBooks?.let {
                binding.nextInLineBooksView.submitList(it)
            }
        }
        onEach(viewModel.afterDeleteCurrentReadBook) {
            binding.customSnackBar.startHeaderTextAnimation(viewModel.message)
        }
    }

    override fun onViewClick() {
        with(binding) {
            customAppBar.setOnNavigationClickListener {
                popBackStack()
            }
            currentReadBookView.onClickSeeAll {
                navigateFragment(R.id.allCurrentReadBooksFragment)
            }
            customAppBar.setOnLastItemClickListener {
                navigateFragment(R.id.downloadedBooksSearchFragment)
            }
            currentReadBookView.itemLongClickListener = { id ->
                val directions =
                    DownloadedBooksFragmentDirections.actionDownloadedBooksFragmentToContextMenuDownloadedBottomDialog(
                        id, CURRENT_READ
                    )
                navigateFragment(directions)
            }
            nextInLineBooksView.itemLongClickListener = { id ->
                val directions =
                    DownloadedBooksFragmentDirections.actionDownloadedBooksFragmentToContextMenuDownloadedBottomDialog(
                        id, NEXT_IN_LINE
                    )
                navigateFragment(directions)
            }
        }
    }

    private fun getFragmentResult() {
        activity?.supportFragmentManager?.setFragmentResultListener(
            DOWNLOAD_REMOVE,
            viewLifecycleOwner
        ) { _, result ->
            with(viewModel) {
                if (result.getString(TYPE).equals(CURRENT_READ))
                    deleteBookFromDownloaded(result.getLong(BOOK_ID))
                else deleteBookFromNextInLine(result.getLong(BOOK_ID))
                viewModel.message = resources.getString(R.string.remove_downloaded)
            }
        }

        activity?.supportFragmentManager?.setFragmentResultListener(
            FINISH,
            viewLifecycleOwner
        ) { _, result ->
            with(viewModel) {
                if (result.getString(TYPE).equals(CURRENT_READ))
                    deleteBookFromDownloaded(result.getLong(BOOK_ID))
                else deleteBookFromNextInLine(result.getLong(BOOK_ID))
                viewModel.message =
                    resources.getString(R.string._novel_was_moved_to_Finished, "Day and night")
            }
        }

        activity?.supportFragmentManager?.setFragmentResultListener(
            DELETE,
            viewLifecycleOwner
        ) { _, result ->
            with(viewModel) {
                if (result.getString(TYPE).equals(CURRENT_READ))
                    deleteBookFromDownloaded(result.getLong(BOOK_ID))
                else deleteBookFromNextInLine(result.getLong(BOOK_ID))
                viewModel.message = resources.getString(R.string._was_deleted, "Day and night")
            }
        }
    }

    companion object {
        const val CURRENT_READ = "CurrentRead"
        const val NEXT_IN_LINE = "NextInLine"
    }
}
