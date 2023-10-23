package ru.tripster.guide.ui.fragments.chat

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.tripster.guide.R
import ru.tripster.guide.appbase.bottomSheetDialog.BottomSheetDialogFragmentBaseMVVM
import ru.tripster.guide.appbase.utils.viewBinding
import ru.tripster.guide.appbase.viewmodel.BaseViewModel
import ru.tripster.guide.databinding.BottomSheetSendContactsBinding

class WarningSendContactsBottomSheet :
    BottomSheetDialogFragmentBaseMVVM<BaseViewModel, BottomSheetSendContactsBinding>() {
    override val viewModel: BaseViewModel by viewModel()
    override val binding: BottomSheetSendContactsBinding by viewBinding()

    override fun onView() {
        dialog?.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val coordinatorLayout = binding.container
            val bottomSheetInternal = d.findViewById<View>(R.id.bottomSheet) as ConstraintLayout
            val bottomSheetBehavior = BottomSheetBehavior.from<View>(bottomSheetInternal)
            BottomSheetBehavior.from(coordinatorLayout.parent as View).peekHeight =
                bottomSheetInternal.height
            bottomSheetBehavior.peekHeight = bottomSheetInternal.height
            coordinatorLayout.parent.requestLayout()
        }
    }

    override fun onViewClick() {
        with(binding) {
            close.setOnClickListener { dismiss() }
            editMessage.setOnClickListener { dismiss() }
            sendMessage.setOnClickListener {
                setFragmentResult(
                    SEND_MESSAGE_BUNDLE,
                    bundleOf(
                        SEND_MESSAGE_REQUEST_KEY to true,
                    )
                )

                dismiss()
            }

        }
    }


    companion object {
        const val SEND_MESSAGE_BUNDLE = "SEND_MESSAGE_BUNDLE"
        const val SEND_MESSAGE_REQUEST_KEY = "SEND_MESSAGE_REQUEST_KEY"
    }


}