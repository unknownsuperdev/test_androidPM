package com.fiction.me.ui.fragments.mainlibrary.browsinghistory.deletebook

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.fiction.me.appbase.DialogFragmentBaseMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.DeleteBookDialogBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeleteBookDialog(val onDeleteClick: () -> Unit) :
    DialogFragmentBaseMVVM<DeleteBookViewModel, DeleteBookDialogBinding>() {
    override val viewModel: DeleteBookViewModel by viewModel()
    override val binding: DeleteBookDialogBinding by viewBinding()

    override fun onView() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        with(binding) {
            cancel.setOnClickListener {
                dismiss()
            }

            delete.setOnClickListener {
                onDeleteClick()
                dismiss()
            }
        }
    }
}