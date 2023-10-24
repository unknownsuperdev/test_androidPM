package com.fiction.me.ui.fragments.chapterlist

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.fiction.me.R
import com.fiction.me.appbase.BottomSheetFragmentBaseMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.BottomDialogChaptersListBinding
import com.fiction.me.ui.fragments.booksummmary.BookSummaryFragment.Companion.BOOK_ID_FOR_CHAPTER_FROM_BS
import com.fiction.me.ui.fragments.reader.OpenedFromWhere
import com.fiction.me.ui.fragments.reader.ReaderFragment.Companion.ARGUMENT_FOR_OPENING_CHAPTER_DIALOG
import com.fiction.me.ui.fragments.reader.ReaderFragment.Companion.BOOK_ID_FOR_CHAPTERS_FROM_READER
import com.fiction.me.utils.Constants.Companion.OPENED_PART_OF_CHAPTER_RV
import com.fiction.me.utils.Constants.Companion.OPENED_PART_OF_DIALOG
import com.fiction.me.utils.Events.Companion.BOOK_ID_PROPERTY
import com.fiction.me.utils.Events.Companion.BOOK_SUMMARY_SCREEN
import com.fiction.me.utils.Events.Companion.CHAPTERS_LIST_SCREEN_SHOWN
import com.fiction.me.utils.Events.Companion.READING_SCREEN
import com.fiction.me.utils.Events.Companion.REFERRER
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_DRAGGING
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChapterListBottomDialog
    : BottomSheetFragmentBaseMVVM<ChapterViewModel, BottomDialogChaptersListBinding>() {

    override val viewModel: ChapterViewModel by viewModel()
    override val binding: BottomDialogChaptersListBinding by viewBinding()

    var onItemClickListener: (id: Long) -> Unit = { }
    private var chaptersAdapter = ChapterListAdapter { id ->
        onItemClickListener(id)
        viewModel.updateChapter(id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.MyBottomSheetDialogTheme)
    }

    override fun onView() {
        with(binding) {
            when (arguments?.get(ARGUMENT_FOR_OPENING_CHAPTER_DIALOG) as OpenedFromWhere) {
                OpenedFromWhere.FROM_BOOK_SUMMERY -> {
                    closeBtn.visibility = View.GONE
                    chaptersTitle.gravity = Gravity.CENTER
                    arguments?.getLong(BOOK_ID_FOR_CHAPTER_FROM_BS)
                        ?.let { viewModel.getChapters(it) }
                    sendEvent(BOOK_SUMMARY_SCREEN, arguments?.getLong(BOOK_ID_FOR_CHAPTER_FROM_BS) ?: -1)
                }
                OpenedFromWhere.FROM_READER -> {
                    closeBtn.visibility = View.VISIBLE
                    chaptersTitle.gravity = Gravity.START
                    arguments?.getLong(BOOK_ID_FOR_CHAPTERS_FROM_READER)
                        ?.let { viewModel.getChapters(it) }
                    sendEvent(READING_SCREEN, arguments?.getLong(BOOK_ID_FOR_CHAPTERS_FROM_READER) ?: -1)
                }
            }

            rvChapters.apply {
                context?.let {
                    layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
                    adapter = chaptersAdapter
                }
            }
        }
    }
    var isUpdated = false
    var isClickDismiss = false

    override fun onEach() {
        onEach(viewModel.chaptersList) {
            binding.chaptersTitle.text = resources.getString(R.string.chapter).format(it?.size ?: 0)
            chaptersAdapter.submitList(it)
        }
        onEach(viewModel.updatedList) {
            isUpdated = true
            if (isClickDismiss) {
                dismiss()
            }
        }
    }

    fun dismissDialog(){
        isClickDismiss = true
        if (isUpdated) {
            dismiss()
        }
    }

    override fun onViewClick() {
        binding.closeBtn.setOnClickListener {
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val maxHeight = (resources.displayMetrics.heightPixels * OPENED_PART_OF_DIALOG).toInt()
            (dialog as? BottomSheetDialog)?.behavior?.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == STATE_DRAGGING) {
                        binding.container.maxHeight = maxHeight
                        binding.rvChapters.apply {
                            if (measuredHeight > maxHeight)
                                setPadding(
                                    0,
                                    0,
                                    0,
                                    ((resources.displayMetrics.heightPixels + measuredHeight - 2 * maxHeight) * OPENED_PART_OF_CHAPTER_RV).toInt()
                                )
                        }
                    }
                }
            })
        }
        return dialog
    }

    private fun sendEvent(referrer: String, bookId: Long) {
        viewModel.trackEvents(
            CHAPTERS_LIST_SCREEN_SHOWN,
            hashMapOf(
                REFERRER to referrer,
                BOOK_ID_PROPERTY to bookId
            )
        )
    }
}
