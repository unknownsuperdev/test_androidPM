package com.name.jat.ui.fragments.mainlibrary

import com.name.jat.R
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.databinding.FragmentAllReadBooksBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllCurrentReadBooksFragment :
    FragmentBaseNCMVVM<AllCurrentReadBooksVIewModel, FragmentAllReadBooksBinding>() {
    override val viewModel: AllCurrentReadBooksVIewModel by viewModel()
    override val binding: FragmentAllReadBooksBinding by viewBinding()

    override fun onView() {
        with(binding) {
            viewModel.getAllCurrentReadsBooksList()
            customAppBar.run {
                setTitle(resources.getString(com.name.jat.R.string.current_reads))
                setFirstItemDrawable(R.drawable.ic_back)
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.allCurrentReadsBookList) { allCurrentReadBooks ->
            allCurrentReadBooks?.let {
                binding.allCurrentReadBooksView.submitList(it)
            }
        }
    }

    override fun onViewClick() {
        with(binding) {
            allCurrentReadBooksView.onItemClickListener = { id ->
                val directions =
                    AllCurrentReadBooksFragmentDirections.actionAllCurrentReadBooksFragmentToBookSummaryFragment(
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