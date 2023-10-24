package com.name.jat.ui.fragments.chapterlist

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_DRAGGING
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.name.jat.R
import com.name.jat.databinding.BottomDialogChaptersListBinding
import com.name.jat.ui.reader.OpenedFromWhere
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class ChapterListBottomDialog(
    private val openedFromWhere: OpenedFromWhere
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomDialogChaptersListBinding
    private val viewModel: ChapterViewModel by viewModels()
    private var chaptersAdapter = ChapterListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.MyBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomDialogChaptersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {

            when (openedFromWhere) {
                OpenedFromWhere.FROM_BOOK_SUMMERY -> {
                    closeBtn.visibility = View.GONE
                    chaptersTitle.gravity = Gravity.CENTER
                }
                OpenedFromWhere.FROM_READER -> {
                    closeBtn.visibility = View.VISIBLE
                    chaptersTitle.gravity = Gravity.START and Gravity.CENTER_VERTICAL
                }
            }

            rvChapters.apply {
                context?.let {
                    layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
                    setHasFixedSize(true)
                    adapter = chaptersAdapter
                }
            }

            closeBtn.setOnClickListener {
                dismiss()
            }

        }
        viewModel.getChapterListData()

        lifecycleScope.launch {
            viewModel.chaptersList.collect {
                chaptersAdapter.submitList(it?.chapterList)
            }
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val maxHeight = (resources.displayMetrics.heightPixels * 0.9).toInt()
            (dialog as? BottomSheetDialog)?.behavior?.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == STATE_DRAGGING) {
                        binding.container.maxHeight = maxHeight
                    }
                }
            })
        }
        return dialog
    }

}
