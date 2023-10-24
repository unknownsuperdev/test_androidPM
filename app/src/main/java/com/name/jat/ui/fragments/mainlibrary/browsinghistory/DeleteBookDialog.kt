package com.name.jat.ui.fragments.mainlibrary.browsinghistory

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.name.jat.databinding.DeleteBookDialogBinding

class DeleteBookDialog(val onDeleteClick: () -> Unit) : DialogFragment() {

    private lateinit var binding: DeleteBookDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DeleteBookDialogBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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