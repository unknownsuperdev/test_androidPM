package com.fiction.me.ui.fragments.explore.notenoughcoins

import com.fiction.me.appbase.DialogFragmentBaseMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.DialogFragmentNotEnoughCoinsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotEnoughCoinsDialogFragment :
    DialogFragmentBaseMVVM<NotEnoughCoinsViewModel, DialogFragmentNotEnoughCoinsBinding>() {
    override val viewModel: NotEnoughCoinsViewModel by viewModel()
    override val binding: DialogFragmentNotEnoughCoinsBinding by viewBinding()

    override fun onView() {
        viewModel.getBalance()

    }

    override fun onEach() {
        onEach(viewModel.userBalance) {
            with(binding) {
                coins.text = it.toString()
            }
        }
    }

    override fun onViewClick() {
        with(binding) {
            closeBtn.setOnClickListener {
                dismiss()
            }
        }
    }
}