package com.name.jat.ui.fragments.explore

import com.name.jat.R
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.databinding.FragmentSuggestBookSeeAllBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SuggestBookSeeAllFragment :
    FragmentBaseNCMVVM<SuggestBookSeeAllViewModel, FragmentSuggestBookSeeAllBinding>() {

    override val binding: FragmentSuggestBookSeeAllBinding by viewBinding()
    override val viewModel: SuggestBookSeeAllViewModel by viewModel()
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
                    SuggestBookSeeAllFragmentDirections.actionSuggestBookSeeAllFragmentToBookSummaryFragment(
                        id
                    )
                navigateFragment(directions)
            }
        }
    }
}
