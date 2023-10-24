package com.fiction.me.ui.fragments.reader

import com.fiction.me.appbase.BottomSheetFragmentBaseMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.appbase.viewmodel.BaseViewModel
import com.fiction.me.databinding.BottomsheetFragmentMoreBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class BottomSheetFragmentMore :
    BottomSheetFragmentBaseMVVM<BaseViewModel, BottomsheetFragmentMoreBinding>() {

    override val viewModel: BaseViewModel by viewModel()
    override val binding: BottomsheetFragmentMoreBinding by viewBinding()
    private var onClick: (Unit) -> Unit = {}


    override fun onViewClick() {

        with(binding) {
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