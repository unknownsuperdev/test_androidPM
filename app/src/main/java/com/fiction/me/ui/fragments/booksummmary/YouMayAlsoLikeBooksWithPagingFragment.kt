package com.fiction.me.ui.fragments.booksummmary

import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.adapter.LoadingState
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentYouMayAlsoLikeWithPagingBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class YouMayAlsoLikeBooksWithPagingFragment :
    FragmentBaseNCMVVM<YouMayAlsoLikeBooksWithPagingViewModel, FragmentYouMayAlsoLikeWithPagingBinding>() {

    override val binding: FragmentYouMayAlsoLikeWithPagingBinding by viewBinding()
    override val viewModel: YouMayAlsoLikeBooksWithPagingViewModel by viewModel()
    private val args: YouMayAlsoLikeBooksWithPagingFragmentArgs by navArgs()

    override fun onView() {
        observeLoadState()
        with(binding) {
            customAppBar.setFirstItemDrawable(R.drawable.ic_back)
            customAppBar.setTitle(resources.getString(R.string.you_may_also_like))
        }
    }

    override fun onEach() {
        onEach(viewModel.getAllCurrentReadsBooksList(args.bookId)) {
            binding.booksInfoView.submitData(lifecycle, it)
        }
    }

    override fun onViewClick() {
        with(binding) {
            customAppBar.setOnNavigationClickListener {
                popBackStack()
            }

            booksInfoView.onReadMoreClickListener = { id, title ->
                viewModel.sendEvent(id)
                val book = booksInfoView.getList().find { it.id == id }
                val directions =
                    YouMayAlsoLikeBooksWithPagingFragmentDirections.actionYouMayAlsoLikeBooksWithPagingFragmentToBookSummaryFragment(
                        id,
                        bookInfo = book?.bookInfo
                    )
                navigateFragment(directions)
            }
        }
    }

    private fun observeLoadState() {
        binding.booksInfoView.apply {
            observeLoadState(viewLifecycleOwner) { state, exception ->
                when (state) {
                    LoadingState.LOADED -> {
                        binding.loadingContainer.isVisible = false
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
}
