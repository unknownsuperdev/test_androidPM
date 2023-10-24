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
import com.name.jat.databinding.BottomDialogContextMenuActionBinding

class ContextMenuActionBottomDialog : BottomSheetDialogFragment() {

    private lateinit var binding: BottomDialogContextMenuActionBinding
    private val viewModel: ContextMenuActionViewModel by viewModels()
    private val args: ContextMenuActionBottomDialogArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomDialogContextMenuActionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
    }

    private fun onClick() {

        with(binding) {
            share.setOnClickListener {
                shareBook()
                dismiss()
            }
            download.setOnClickListener {
                setFragmentResultForDownload()
            }
            delete.setOnClickListener {
                setFragmentResultForDelete()
            }
            finished.setOnClickListener {
                setFragmentResultForFinished()
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

    private fun setFragmentResultForFinished() {
        activity?.supportFragmentManager?.setFragmentResult(
            FINISH,
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
        const val FINISH = "FINISH"
        const val DELETE = "DELETE"
        const val BOOK_ID = "FINISHED_BOOK_ID"
    }
}
