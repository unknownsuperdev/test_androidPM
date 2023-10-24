package com.name.jat.ui.fragments.mainlibrary

import com.name.jat.R
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.databinding.FragmentMainLibraryBinding
import com.name.jat.extensions.startHeaderTextAnimation
import com.name.jat.ui.fragments.mainlibrary.bottomdialog.ContextMenuActionBottomDialog.Companion.BOOK_ID
import com.name.jat.ui.fragments.mainlibrary.bottomdialog.ContextMenuActionBottomDialog.Companion.DELETE
import com.name.jat.ui.fragments.mainlibrary.bottomdialog.ContextMenuActionBottomDialog.Companion.DOWNLOAD
import com.name.jat.ui.fragments.mainlibrary.bottomdialog.ContextMenuActionBottomDialog.Companion.FINISH
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainLibraryFragment : FragmentBaseNCMVVM<MainLibraryViewModel, FragmentMainLibraryBinding>() {
    override val viewModel: MainLibraryViewModel by viewModel()
    override val binding: FragmentMainLibraryBinding by viewBinding()

    override fun onView() {
        getFragmentResult()
        with(binding) {
            viewModel.getCurrentReadBooksList()
            customAppBar.run {
                setTitle(resources.getString(R.string.library))
                makeLastItemVisible()
            }
            currentReadBookView.setTitle(resources.getString(R.string.current_reads))
            currentReadBookView.makeSeeAllAndTitleVisible()
        }
    }

    override fun onEach() {
        onEach(viewModel.currentReadBooks) { currentReadBooks ->
            currentReadBooks?.let {
                binding.currentReadBookView.submitList(it)
            }
        }
        onEach(viewModel.afterDeleteCurrentReadBook) {
            binding.customSnackBar.startHeaderTextAnimation(viewModel.message)
        }
    }

    override fun onViewClick() {
        with(binding) {
            with(currentReadBookView) {
                onClickSeeAll {
                    navigateFragment(R.id.allCurrentReadBooksFragment)
                }
                itemLongClickListener = { id ->
                    val directions =
                        MainLibraryFragmentDirections.actionMainLibraryToContextMenuBottomDialog(
                            id
                        )
                    navigateFragment(directions)
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
            downloaded.setOnClickListener {
                navigateFragment(R.id.downloadedBooksFragment)
            }
            addedToLibrary.setOnClickListener {
                navigateFragment(R.id.addedToLibraryFragment)
            }
        }
    }

    private fun getFragmentResult() {
        activity?.supportFragmentManager?.setFragmentResultListener(
            DOWNLOAD,
            viewLifecycleOwner
        ) { _, result ->
            with(viewModel) {
                deleteBookFromLib(result.getLong(BOOK_ID))
                viewModel.message = resources.getString(R.string._was_downloaded, "Day and night")
            }
        }

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
}