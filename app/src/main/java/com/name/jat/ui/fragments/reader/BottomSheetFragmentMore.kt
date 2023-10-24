package com.name.jat.ui.fragments.reader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.name.jat.databinding.BottomsheetFragmentMoreBinding
import com.name.jat.extensions.shareBookWithIntent


class BottomSheetFragmentMore : BottomSheetDialogFragment() {

    private lateinit var binding: BottomsheetFragmentMoreBinding
    private var onClick: (Unit) -> Unit = {}


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetFragmentMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            share.setOnClickListener {
                activity?.shareBookWithIntent("This is my text to send.")
                dismiss()
            }
            report.setOnClickListener {
                onClick.invoke(Unit)
                dismiss()
            }
        }
    }
    fun setReportClickListener(onClick: (Unit) -> Unit) {
        this.onClick = onClick
    }
}