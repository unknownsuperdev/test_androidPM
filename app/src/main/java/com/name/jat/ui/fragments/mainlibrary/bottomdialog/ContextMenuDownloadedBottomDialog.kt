package com.name.jat.ui.fragments.mainlibrary.bottomdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.name.jat.R
import com.name.jat.databinding.BottomDialogContextMenuDownloadedBinding
import com.name.jat.extensions.shareBookWithIntent

class ContextMenuDownloadedBottomDialog : BottomSheetDialogFragment() {

    private lateinit var binding: BottomDialogContextMenuDownloadedBinding
    private val viewModel: ContextMenuDownloadedViewModel by viewModels()
    private val args: ContextMenuDownloadedBottomDialogArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomDialogContextMenuDownloadedBinding.inflate(inflater, container, false)
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
            finished.setOnClickListener {
                setFragmentResultForFinished()
            }
            removeDownloads.setOnClickListener {
                setFragmentResultForRemoveDownloads()
            }
            delete.setOnClickListener {
                setFragmentResultForDelete()
            }
        }
    }

    private fun setFragmentResultForRemoveDownloads() {
        activity?.supportFragmentManager?.setFragmentResult(
            DOWNLOAD_REMOVE,
            bundleOf(BOOK_ID to args.id, TYPE to args.type)
        )
        dismiss()
    }

    private fun setFragmentResultForFinished() {
        activity?.supportFragmentManager?.setFragmentResult(
            FINISH,
            bundleOf(BOOK_ID to args.id, TYPE to args.type)
        )
        dismiss()
    }

    private fun setFragmentResultForDelete() {
        activity?.supportFragmentManager?.setFragmentResult(
            DELETE,
            bundleOf(BOOK_ID to args.id, TYPE to args.type)
        )
        dismiss()
    }

    companion object {
        const val DOWNLOAD_REMOVE = "DOWNLOAD_REMOVE"
        const val FINISH = "FINISH"
        const val DELETE = "DELETE"
        const val BOOK_ID = "FINISHED_BOOK_ID"
        const val TYPE = "TYPE"
    }
}
