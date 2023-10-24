package com.name.jat.ui.fragments.mainlibrary.finished

import com.name.jat.R
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.databinding.FragmentAllReadBooksBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SeeAllFinishedBooksFragment :
    FragmentBaseNCMVVM<SeeAllFinishedBooksViewModel, FragmentAllReadBooksBinding>() {
    override val viewModel: SeeAllFinishedBooksViewModel by viewModel()
    override val binding: FragmentAllReadBooksBinding by viewBinding()

    override fun onView() {
        with(binding) {
            viewModel.getAllFinishedBookList()
            customAppBar.run {
                setTitle(resources.getString(R.string.finished))
                setFirstItemDrawable(R.drawable.ic_back)
                makeLastItemVisible()
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.allFinishedBookList) { allFinishedBooks ->
            allFinishedBooks?.let {
                binding.allCurrentReadBooksView.submitList(it)
            }
        }
    }

    override fun onViewClick() {
        with(binding) {
            allCurrentReadBooksView.onItemClickListener = { id ->
                val directions =
                    SeeAllFinishedBooksFragmentDirections.actionSeeAllFinishedBooksFragmentToBookSummaryFragment(
                        id
                    )
                navigateFragment(directions)
            }
            customAppBar.setOnNavigationClickListener {
                popBackStack()
            }
        }
    }
}