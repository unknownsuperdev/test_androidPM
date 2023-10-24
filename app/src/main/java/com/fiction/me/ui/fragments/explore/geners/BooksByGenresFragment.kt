package com.fiction.me.ui.fragments.explore.geners

import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.adapter.LoadingState
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentBooksByGenreBinding
import com.fiction.me.extensions.startHeaderTextAnimation
import com.fiction.me.utils.Events.Companion.BOOKLIST
import com.fiction.me.utils.Events.Companion.BOOK_CLICKED
import com.fiction.me.utils.Events.Companion.BOOK_ID_PROPERTY
import com.fiction.me.utils.Events.Companion.BOOK_LIST_SCREEN
import com.fiction.me.utils.Events.Companion.PLACEMENT
import com.fiction.me.utils.Events.Companion.REFERRER
import com.fiction.me.utils.Events.Companion.SHOW_MORE_CLICKED
import com.fiction.me.utils.cacheImages
import org.koin.androidx.viewmodel.ext.android.viewModel

class BooksByGenresFragment :
    FragmentBaseNCMVVM<BooksByGenresViewModel, FragmentBooksByGenreBinding>() {
    override val viewModel: BooksByGenresViewModel by viewModel()
    override val binding: FragmentBooksByGenreBinding by viewBinding()
    private val args: BooksByGenresFragmentArgs by navArgs()

    override fun onView() {
        with(binding) {
            customAppBar.setFirstItemDrawable(R.drawable.ic_back)
            observeLoadState()
            customAppBar.setTitle(args.title)
        }
    }

    override fun onEach() {
        onEach(viewModel.getGenreBooksList(args.id)) { books ->
            binding.booksInfoViewPaging.submitData(lifecycle, books)
        }
        onEach(viewModel.addBookToLib) {
            with(binding) {
                customSnackBar.startHeaderTextAnimation(R.string.added_to_library)
            }
        }
        onEach(viewModel.removeBookFromLib) {
            with(binding) {
                customSnackBar.startHeaderTextAnimation(R.string.deleted_from_library)
            }
        }
    }

    override fun onViewClick() {
        with(binding) {
            customAppBar.setOnNavigationClickListener {
                popBackStack()
            }

            booksInfoViewPaging.onReadMoreClickListener = { id, title ->
                sendEvents(id)
                val book = booksInfoViewPaging.getList().find { it.id == id }
                val directions =
                    BooksByGenresFragmentDirections.actionBooksByGenresFragmentToBookSummaryFragment(
                        id,
                        bookInfo = book?.bookInfo
                    )
                navigateFragment(directions)
            }
        }
    }

    private fun sendEvents(id: Long) {
        viewModel.run {
            trackEvents(
                BOOK_CLICKED,
                hashMapOf(BOOK_ID_PROPERTY to id, PLACEMENT to BOOKLIST)
            )
            trackEvents(
                SHOW_MORE_CLICKED,
                hashMapOf(
                    REFERRER to BOOK_LIST_SCREEN,
                    BOOK_ID_PROPERTY to id
                )
            )
        }
    }


    private fun observeLoadState() {
        binding.booksInfoViewPaging.apply {
            observeLoadState(viewLifecycleOwner) { state, exception ->
                when (state) {
                    LoadingState.LOADED -> {
                        binding.booksInfoViewPaging.getList()
                            .forEach { context?.let { it1 -> cacheImages(it1, it.imageCover) } }
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
