package com.name.jat.ui.fragments.explore.geners

import com.name.jat.R
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.databinding.FragmentBooksByGenreBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class BooksByGenresFragment :
    FragmentBaseNCMVVM<BooksByGenresViewModel, FragmentBooksByGenreBinding>() {
    override val viewModel: BooksByGenresViewModel by viewModel()
    override val binding: FragmentBooksByGenreBinding by viewBinding()

    override fun onView() {
        with(binding) {
            customAppBar.setFirstItemDrawable(R.drawable.ic_back)
            viewModel.getSuggestedBooksList()
        }
    }

    override fun onEach() {
        onEach(viewModel.bookInfoList) { books ->
            books?.let {
                with(binding) {
                    customAppBar.setTitle(it.title)
                    booksInfoView.submitList(it.list)
                }
            }
        }
    }

    override fun onViewClick() {
        with(binding) {
            customAppBar.setOnNavigationClickListener {
                popBackStack()
            }

            booksInfoView.itemSaveClickListener = { id, isSaved ->
                viewModel.updateBookInfoListAdapter(id, isSaved)
            }

            booksInfoView.onReadMoreClickListener = { id ->
                val directions =
                    BooksByGenresFragmentDirections.actionBooksByGenresFragmentToBookSummaryFragment(
                        id
                    )
                navigateFragment(directions)
            }
        }
    }
}
