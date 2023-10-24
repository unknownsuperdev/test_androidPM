package com.name.jat.ui.fragments.mainlibrary.finished

import com.name.jat.R
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.databinding.FragmentFinishedBooksBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FinishedBooksFragment :
    FragmentBaseNCMVVM<FinishedBooksViewModel, FragmentFinishedBooksBinding>() {
    override val viewModel: FinishedBooksViewModel by viewModel()
    override val binding: FragmentFinishedBooksBinding by viewBinding()

    override fun onView() {
        with(binding) {
            viewModel.getSuggestedBooksList()
            viewModel.getFinishedReadBooksList()
            customAppBar.run {
                setTitle(resources.getString(R.string.finished))
                setFirstItemDrawable(R.drawable.ic_back)
                makeLastItemVisible()
            }
            finishedBooksView.makeSeeAllAndTitleVisible()
        }
    }

    override fun onEach() {
        onEach(viewModel.suggestedBooksList) {
            with(binding) {
                suggestedBooksView.setTitle(it?.title ?: "")
                suggestedBooksView.submitList(it?.booksList ?: listOf())
            }
        }
        onEach(viewModel.finishedReadBooks) { books ->
            with(binding) {
                books?.let { finishedBooksView.submitList(it) }
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
            finishedBooksView.onClickSeeAll {
                navigateFragment(R.id.seeAllFinishedBooksFragment)
            }
        }
    }
}
