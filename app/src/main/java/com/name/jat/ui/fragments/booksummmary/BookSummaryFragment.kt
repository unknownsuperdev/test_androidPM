package com.name.jat.ui.fragments.booksummmary

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.name.jat.R
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.databinding.FragmentBookSummaryBinding
import com.name.jat.extensions.startHeaderTextAnimation
import com.name.jat.ui.fragments.chapterlist.ChapterListBottomDialog
import com.name.jat.ui.reader.OpenedFromWhere
import com.name.jat.utils.setResizableText
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookSummaryFragment : FragmentBaseNCMVVM<BookSummaryViewModel, FragmentBookSummaryBinding>() {
    override val binding: FragmentBookSummaryBinding by viewBinding()
    override val viewModel: BookSummaryViewModel by viewModel()

    override fun onView() {

        with(binding) {
            val decryptionText = description.text
            description.setResizableText(decryptionText.toString(), 4, true, collapseText = true)
            views.text = resources.getString(R.string.views, 30)
            likes.text = resources.getString(R.string.likes, 24)
            context?.let {
                Glide.with(it)
                    .load(R.drawable.book)
                    .circleCrop()
                    .into(userImage)
            }
        }
        viewModel.getSuggestedBooksList()
        viewModel.getPopularTagsList()
    }

    override fun onEach() {
        onEach(viewModel.suggestedBooksList) {
            with(binding) {
                it?.title?.let { title -> suggestedBooksRV.setTitle(title) }
                it?.booksList?.let { bookList -> suggestedBooksRV.submitList(bookList) }
            }
        }
        onEach(viewModel.popularTagsList) { tagsModel ->
            tagsModel?.run {
                binding.popularTagsView.setTitle(title)
                binding.popularTagsView.submitList(this.popularTagsList)
            }
        }
        onEach(viewModel.readBook) {
            BookSummaryFragmentDirections.actionBookSummaryFragmentToReaderFragment().run {
                navigateFragment(this)
            }
        }
        onEach(viewModel.addlike) {
            with(binding) {
                customSnackBar.startHeaderTextAnimation(R.string.your_like_was_added)
                favoriteTxt.text = getString(R.string.liked)
                favorite.setImageResource(R.drawable.ic_fill_favorite)
            }
        }
        onEach(viewModel.removeLike) {
            with(binding) {
                customSnackBar.startHeaderTextAnimation(R.string.your_like_was_removed)
                favoriteTxt.text = getString(R.string.like)
                favorite.setImageResource(R.drawable.ic_favorite)
            }
        }

        onEach(viewModel.addBookToLib) {
            with(binding) {
                customSnackBar.startHeaderTextAnimation(R.string.added_to_library)
                addToLibraryText.text = getString(R.string.added)
                addToLibrary.setImageResource(R.drawable.ic_added_to_library)
            }
        }
        onEach(viewModel.removeBookFromLib) {
            with(binding) {
                customSnackBar.startHeaderTextAnimation(R.string.deleted_from_library)
                favoriteTxt.text = getString(R.string.add)
                addToLibrary.setImageResource(R.drawable.ic_add_to_library)
            }
        }
    }

    override fun onViewClick() {
        super.onViewClick()
        with(binding) {
            toolBar.setOnClickListener {
                popBackStack()
            }
            download.setOnClickListener {
                context?.let {
                    download.background =
                        ContextCompat.getDrawable(it, R.drawable.icon_clicked_shape)
                    download.setImageResource(R.drawable.ic_download_clicked)
                    downloadTxt.setTextColor(
                        ContextCompat.getColor(
                            it,
                            R.color.dark_black_500
                        )
                    )

                    CoroutineScope(Dispatchers.IO).launch {
                        delay(1000)

                        withContext(Dispatchers.Main) {
                            download.isEnabled = false
                            download.run {
                                background =
                                    ContextCompat.getDrawable(
                                        it,
                                        R.drawable.icon_shape
                                    )
                                setImageResource(R.drawable.ic_download)
                            }
                            downloadTxt.run {
                                text = context?.resources?.getString(R.string.downloading)
                                setTextColor(ContextCompat.getColor(it, R.color.gray))
                            }
                        }

                        for (i in 0..100) {
                            delay(100)
                            circleProgressBar.progress = i
                        }
                        delay(100)

                        withContext(Dispatchers.Main) {
                            download.setImageResource(R.drawable.ic_downloaded)
                            downloadTxt.text = getString(R.string.downloaded)
                            circleProgressBar.progress = 0
                            customSnackBar.startHeaderTextAnimation(resources.getString(R.string.downloaded_to_library))
                            circleProgressBar.progress = 0
                        }
                    }
                }
            }
            favorite.setOnClickListener {
                viewModel.changeBookLikeState()
            }
            share.setOnClickListener {
                shareBook()
            }
            addToLibrary.setOnClickListener {
                viewModel.addOrRemoveBook()
            }
            popularTagsView.setOnSeeAllClickListener{
                val directions = BookSummaryFragmentDirections.actionBookSummaryFragmentToFilterByPopularTagsFragment()
                navigateFragment(directions)
            }
            chapters.setOnClickListener {
                val btnDialog = ChapterListBottomDialog(OpenedFromWhere.FROM_BOOK_SUMMERY)
                btnDialog.show(childFragmentManager, ChapterListBottomDialog::class.java.simpleName)
            }
            suggestedBooksRV.onSuggestedBooksItemClick = { suggestedBookId, isAddedLibrary ->
                viewModel.updateSuggestedBooksList(suggestedBookId, isAddedLibrary)
            }
            suggestedBooksRV.navigateClickListener = {
                viewModel.getSuggestedBooksList()
            }
            suggestedBooksRV.onSeeAllClickListener = {
                val directions = BookSummaryFragmentDirections.actionBookSummaryFragmentToSuggestBookSeeAllFragment()
                navigateFragment(directions)
            }
            readNow.setOnClickListener {
                context?.let {
                    readNow.setTextColor(ContextCompat.getColor(it,R.color.secondary_purple_dark_600))
                }
                readNow.text = null
                progressCircular.isVisible = true
                viewModel.startReadBook()
            }
        }
    }

    private fun shareBook() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "This is my book link to share.")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}