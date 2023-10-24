package com.fiction.me.ui.fragments.mainlibrary

import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.fiction.domain.model.OpenedAllBooks
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.adapter.LoadingState
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentAllReadBooksBinding
import com.fiction.me.extensions.toJson
import com.fiction.me.utils.InternalDeepLink
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllCurrentReadBooksFragment :
    FragmentBaseNCMVVM<AllCurrentReadBooksViewModel, FragmentAllReadBooksBinding>() {
    override val viewModel: AllCurrentReadBooksViewModel by viewModel()
    override val binding: FragmentAllReadBooksBinding by viewBinding()
    private val args: AllCurrentReadBooksFragmentArgs by navArgs()

    override fun onView() {
        with(binding) {
            observeLoadState()
            customAppBar.run {
                when (args.openedAllBooks) {
                    OpenedAllBooks.FROM_ADDED_TO_LIBRARY -> {
                        setTitle(resources.getString(R.string.added_to_library))
                    }
                    else -> {
                        setTitle(resources.getString(R.string.current_reads))
                    }
                }
                setFirstItemDrawable(R.drawable.ic_back)
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.getAllCurrentReadsBooksList(args.openedAllBooks)) {
            binding.allCurrentReadBooksView.submitData(lifecycle, it)
        }
    }

    override fun onViewClick() {
        with(binding) {
            allCurrentReadBooksView.onItemClickListener = { id, title ->
                val book = allCurrentReadBooksView.getList().find { it.id == id }
                /*  val directions =
                      AllCurrentReadBooksFragmentDirections.actionAllCurrentReadBooksFragmentToBookSummaryFragment(
                          id,
                          bookInfo = book?.bookInfo
                      )
                  navigateFragment(directions)*/
                val deepLink = InternalDeepLink.makeCustomDeepLinkToBookSummary(
                    id,
                    book?.bookInfo?.toJson() ?: ""
                ).toUri()
                navigateFragment(deepLink)
            }
            customAppBar.setOnNavigationClickListener {
                popBackStack()
            }
        }
    }

    private fun observeLoadState() {
        binding.allCurrentReadBooksView.apply {
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