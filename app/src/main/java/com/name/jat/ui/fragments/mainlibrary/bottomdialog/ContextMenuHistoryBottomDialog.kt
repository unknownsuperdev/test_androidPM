package com.name.jat.ui.fragments.mainlibrary.bottomdialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.name.jat.R
import com.name.jat.databinding.BottomDialogContextMenuHistoryBinding

class ContextMenuHistoryBottomDialog : BottomSheetDialogFragment() {

    private lateinit var binding: BottomDialogContextMenuHistoryBinding
    private val viewModel: ContextMenuHistoryViewModel by viewModels()
    private val args: ContextMenuHistoryBottomDialogArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomDialogContextMenuHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
    }

    private fun onClick() {

        with(binding) {
            addToLibrary.setOnClickListener {
                setFragmentResultForAddToLibrary()
            }
            download.setOnClickListener {
                setFragmentResultForDownload()
            }
            share.setOnClickListener {
                shareBook()
                dismiss()
            }
            delete.setOnClickListener {
                setFragmentResultForDelete()
            }
        }
    }

    private fun setFragmentResultForDownload() {
        activity?.supportFragmentManager?.setFragmentResult(
            DOWNLOAD,
            bundleOf(BOOK_ID to args.id)
        )
        dismiss()
    }

    private fun setFragmentResultForAddToLibrary() {
        activity?.supportFragmentManager?.setFragmentResult(
            ADD_TO_LIBRARY,
            bundleOf(BOOK_ID to args.id)
        )
        dismiss()
    }

    private fun setFragmentResultForDelete() {
        activity?.supportFragmentManager?.setFragmentResult(
            DELETE,
            bundleOf(BOOK_ID to args.id)
        )
        dismiss()
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

    companion object {
        const val DOWNLOAD = "DOWNLOAD"
        const val ADD_TO_LIBRARY = "ADD_TO_LIBRARY"
        const val DELETE = "DELETE"
        const val BOOK_ID = "FINISHED_BOOK_ID"
    }
}
