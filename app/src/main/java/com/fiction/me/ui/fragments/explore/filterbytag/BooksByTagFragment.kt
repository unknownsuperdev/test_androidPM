package com.fiction.me.ui.fragments.explore.filterbytag

import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentBooksByTagBinding
import com.fiction.me.utils.Events.Companion.BOOKLIST
import com.fiction.me.utils.Events.Companion.BOOK_CLICKED
import com.fiction.me.utils.Events.Companion.BOOK_ID_PROPERTY
import com.fiction.me.utils.Events.Companion.BOOK_LIST_SCREEN
import com.fiction.me.utils.Events.Companion.PLACEMENT
import com.fiction.me.utils.Events.Companion.REFERRER
import com.fiction.me.utils.Events.Companion.SHOW_MORE_CLICKED
import com.fiction.me.utils.cacheImages
import org.koin.androidx.viewmodel.ext.android.viewModel

class BooksByTagFragment :
    FragmentBaseNCMVVM<BooksByTagViewModel, FragmentBooksByTagBinding>() {
    override val viewModel: BooksByTagViewModel by viewModel()
    override val binding: FragmentBooksByTagBinding by viewBinding()
    private val args: BooksByTagFragmentArgs by navArgs()

    override fun onView() {
        with(binding) {
            loadingContainer.isVisible = true
            customAppBar.setFirstItemDrawable(R.drawable.ic_back)
            viewModel.getTagBooksList(args.id)
        }
    }

    override fun onEach() {
        onEach(viewModel.bookList) { books ->
            books?.let {
                it.books.forEach { context?.let { it1 -> cacheImages(it1, it.imageCover) } }
                with(binding) {
                    customAppBar.setTitle(it.title)
                    loadingContainer.isVisible = false
                    booksInfoView.submitList(it.books)
                }
            }
        }
    }

    override fun onViewClick() {
        with(binding) {
            customAppBar.setOnNavigationClickListener {
                popBackStack()
            }

            booksInfoView.itemSaveClickListener = { id, isAddedToLib ->
                viewModel.updateTagBooksListAdapter(id, isAddedToLib)
            }

            booksInfoView.onReadMoreClickListener = { id, title ->
                val book = viewModel.bookList.value?.books?.find { it.id == id }
                sendEvents(id)
                val directions =
                    BooksByTagFragmentDirections.actionBooksByTagFragmentToBookSummaryFragment(
                        id,
                        bookInfo = book?.bookInfo
                    )
                navigateFragment(directions)
            }
        }
    }

    private fun sendEvents(id: Long) {
        viewModel.run {
            trackEvents(BOOK_CLICKED, hashMapOf(BOOK_ID_PROPERTY to id, PLACEMENT to BOOKLIST))
            trackEvents(
                SHOW_MORE_CLICKED,
                hashMapOf(
                    REFERRER to BOOK_LIST_SCREEN,
                    BOOK_ID_PROPERTY to id
                )
            )
        }
    }
}
