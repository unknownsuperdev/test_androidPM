package com.fiction.me.ui.fragments.purchase

import android.os.Bundle
import androidx.core.view.isVisible
import com.fiction.me.R
import com.fiction.me.appbase.BottomSheetFragmentBaseMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.BottomDialogGetCoinForNotPayerBinding
import com.fiction.me.ui.fragments.explore.GetMoreCoinsDialogFragment
import com.fiction.me.ui.fragments.purchase.maindialog.MainDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class GetCoinForNotPayerUserBottomDialog :
    BottomSheetFragmentBaseMVVM<GetCoinForNotPayerUserViewModel, BottomDialogGetCoinForNotPayerBinding>() {
    override val viewModel: GetCoinForNotPayerUserViewModel by viewModel()
    override val binding: BottomDialogGetCoinForNotPayerBinding by viewBinding()

    override fun getTheme() = R.style.CustomTransparentBottomSheetDialogTheme

    override fun onEach() {
        onEach(viewModel.getCoinProgress) {
            binding.progressContainer.isVisible = false
            val mainDialogFragment = MainDialogFragment()
            val args = Bundle()
            args.putString(
                GetMoreCoinsDialogFragment.TITLE,
                resources.getString(R.string.error)
            )
            args.putString(
                GetMoreCoinsDialogFragment.DESCRIPTION,
                resources.getString(R.string.error_content)
            )
            mainDialogFragment.arguments = args
            mainDialogFragment.show(
                childFragmentManager,
                MainDialogFragment::class.java.simpleName
            )
        }

    }

    override fun onViewClick() {
        with(binding) {
            closeBtn.setOnClickListener {
                dismiss()
            }
            getCoins.setOnClickListener {
                viewModel.isStartProgress = true
                progressContainer.isVisible = true
            }
        }
    }
}
