package com.name.jat.ui.fragments.mainlibrary.addedtolibrary

import com.name.jat.R
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.databinding.FragmentAddedToLibraryBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddedToLibraryFragment :
    FragmentBaseNCMVVM<AddedToLibraryViewModel, FragmentAddedToLibraryBinding>() {
    override val viewModel: AddedToLibraryViewModel by viewModel()
    override val binding: FragmentAddedToLibraryBinding by viewBinding()

    override fun onView() {
        with(binding) {
            viewModel.run {
                getSuggestedBooksList()
                getAddedToLibraryBooksList()
            }
            customAppBar.run {
                setTitle(resources.getString(R.string.added_to_library))
                setFirstItemDrawable(R.drawable.ic_back)
                makeLastItemVisible()
            }
            addedToLibrary.makeSeeAllAndTitleVisible()
        }
    }

    override fun onEach() {
        onEach(viewModel.addedToLibraryBooks) { books ->
            with(binding) {
                books?.let { addedToLibrary.submitList(it) }
            }
        }
        onEach(viewModel.suggestedBooksList) {
            with(binding) {
                suggestedBooksView.setTitle(it?.title ?: "")
                suggestedBooksView.submitList(it?.booksList ?: listOf())
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
            }
        }
    }

}
