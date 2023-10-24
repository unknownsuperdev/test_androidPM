package com.fiction.me.ui.fragments.retention

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.fiction.me.R
import com.fiction.me.appbase.BottomSheetFragmentBaseMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.BottomDialogRetentionInReaderBinding
import com.fiction.me.ui.fragments.reader.ReaderFragment
import com.fiction.me.utils.Events.Companion.BOOK_ID_PROPERTY
import com.fiction.me.utils.Events.Companion.PLACEMENT
import com.fiction.me.utils.Events.Companion.READER
import com.fiction.me.utils.Events.Companion.RETENTION_SCREEN_CLOSED
import com.fiction.me.utils.Events.Companion.RETENTION_SCREEN_SHOWN
import com.fiction.me.utils.Events.Companion.RETENTION_SCREEN_START_READING_BUTTON_CLICKED
import com.fiction.me.utils.cacheImages
import org.koin.androidx.viewmodel.ext.android.viewModel

class RetentionInReaderBottomDialog :
    BottomSheetFragmentBaseMVVM<RetentionInReaderViewModel, BottomDialogRetentionInReaderBinding>() {

    override val viewModel: RetentionInReaderViewModel by viewModel()
    override val binding: BottomDialogRetentionInReaderBinding by viewBinding()

    private val retentionAdapter = RetentionAdapter { bookId, title ->
        viewModel.updateBookSelectionState(bookId, title)
    }
    var dismissListener: (isGoBack: Boolean) -> Unit = {}
    var startReadListener: (id: Long) -> Unit = {}
    var isGoBack = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogThemeCustomBehavior)
    }

    override fun onView() {
        viewModel.trackEvents(
            RETENTION_SCREEN_SHOWN,
            hashMapOf(
                PLACEMENT to READER,
                BOOK_ID_PROPERTY to (arguments?.getLong(BOOK_ID) ?: -1L)
            )
        )
        binding.retentionBooksRV.apply {
            context?.let {
                layoutManager = LinearLayoutManager(it, LinearLayoutManager.HORIZONTAL, false)
                adapter = retentionAdapter
            }
        }
        val bookId = arguments?.getLong(BOOK_ID) ?: -1L
        viewModel.getRetentionScreenBooks(bookId)
    }

    override fun onViewClick() {
        with(binding) {
            closeBtn.setOnClickListener {
                dismiss()
            }
            startReading.setOnClickListener {
                viewModel.trackEvents(
                    RETENTION_SCREEN_START_READING_BUTTON_CLICKED,
                    hashMapOf(BOOK_ID_PROPERTY to viewModel.readingBookId)
                )
                viewModel.readingBookId.let {
                    startReadListener(it)
                    isGoBack = false
                    dismiss()
                }
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.retentionScreenBooks) { retentionBooks ->
            retentionAdapter.submitList(retentionBooks)
            retentionBooks?.forEach {
                context?.let { it1 -> cacheImages(it1, it.image) }
                if (viewModel.isVisibleReadButton)
                    binding.startReading.isVisible = true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.trackEvents(RETENTION_SCREEN_CLOSED)
        dismissListener(isGoBack)
    }

    companion object {
        const val BOOK_ID = "bookId"
        const val BOOK_NAME = "bookName"

        fun newInstance(bookId: Long, bookName: String) = RetentionInReaderBottomDialog().apply {
            arguments = Bundle().apply {
                putLong(BOOK_ID, bookId)
                putString(BOOK_NAME, bookName)
            }
        }
    }
}
