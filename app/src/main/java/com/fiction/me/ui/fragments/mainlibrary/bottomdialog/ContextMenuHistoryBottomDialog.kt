package com.fiction.me.ui.fragments.mainlibrary.bottomdialog

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.fragment.navArgs
import com.fiction.me.R
import com.fiction.me.appbase.BottomSheetFragmentBaseMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.BottomDialogContextMenuHistoryBinding
import com.fiction.me.utils.Events.Companion.BROWSING_HISTORY
import com.fiction.me.utils.Events.Companion.PLACEMENT
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContextMenuHistoryBottomDialog :
    BottomSheetFragmentBaseMVVM<ContextMenuHistoryViewModel, BottomDialogContextMenuHistoryBinding>() {

    override val binding: BottomDialogContextMenuHistoryBinding by viewBinding()
    override val viewModel: ContextMenuHistoryViewModel by viewModel()
    private val args: ContextMenuHistoryBottomDialogArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onViewClick() {

        with(binding) {
            addToLibrary.setOnClickListener {
                setFragmentResultForAddToLibrary()
            }
            delete.setOnClickListener {
                setFragmentResultForDelete()
            }
        }
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

//    private fun sendEvent(eventName: String) {
//        val bookName = binding.title
//        viewModel.trackEvents(
//            eventName,
//            hashMapOf(
//                PLACEMENT to BROWSING_HISTORY,
//                BOOK_NAME to bookName
//            )
//        )
//    }


    companion object {
        const val ADD_TO_LIBRARY = "ADD_TO_LIBRARY"
        const val DELETE = "DELETE"
        const val BOOK_ID = "FINISHED_BOOK_ID"
    }
}
