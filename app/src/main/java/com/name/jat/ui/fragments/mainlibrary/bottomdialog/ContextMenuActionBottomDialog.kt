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
import com.name.jat.extensions.shareBookWithIntent


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
                activity?.shareBookWithIntent("This is my book link to share.")
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

    companion object {
        const val DOWNLOAD = "DOWNLOAD"
        const val FINISH = "FINISH"
        const val DELETE = "DELETE"
        const val BOOK_ID = "FINISHED_BOOK_ID"
    }
}
