package com.fiction.me.ui.fragments.mainlibrary.bottomdialog

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.fragment.navArgs
import com.fiction.me.R
import com.fiction.me.appbase.BottomSheetFragmentBaseMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.BottomDialogContextMenuActionBinding
import com.fiction.me.utils.Events.Companion.LIBRARY_CONTEXT_MENU
import com.fiction.me.utils.Events.Companion.PLACEMENT
import org.koin.androidx.viewmodel.ext.android.viewModel


class ContextMenuActionBottomDialog :
    BottomSheetFragmentBaseMVVM<ContextMenuActionViewModel, BottomDialogContextMenuActionBinding>() {
    override val binding: BottomDialogContextMenuActionBinding by viewBinding()
    override val viewModel: ContextMenuActionViewModel by viewModel()

    private val args: ContextMenuActionBottomDialogArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onViewClick() {

        with(binding) {
            delete.setOnClickListener {
                setFragmentResultForDelete()
            }
            finished.setOnClickListener {
                setFragmentResultForFinished()
            }
        }
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

//    private fun sendEvent(eventName: String) {
//        val bookName = binding.title
//        viewModel.trackEvents(
//            eventName,
//            hashMapOf(
//                PLACEMENT to LIBRARY_CONTEXT_MENU,
//                BOOK_NAME to bookName
//            )
//        )
//    }


    companion object {
        const val FINISH = "FINISH"
        const val DELETE = "DELETE"
        const val BOOK_ID = "FINISHED_BOOK_ID"
    }
}
