package com.fiction.me.ui.fragments.explore

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.fiction.me.R
import com.fiction.me.appbase.DialogFragmentBaseMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.DialogFragmentGetMoreCoinsBinding
import com.fiction.me.ui.fragments.purchase.maindialog.MainDialogFragment
import com.fiction.me.utils.Constants
import org.koin.androidx.viewmodel.ext.android.viewModel

class GetMoreCoinsDialogFragment :
    DialogFragmentBaseMVVM<GetMoreCoinsViewModel, DialogFragmentGetMoreCoinsBinding>() {
    override val binding: DialogFragmentGetMoreCoinsBinding by viewBinding()
    override val viewModel: GetMoreCoinsViewModel by viewModel()

    override fun onView() {
        with(binding) {
            viewModel.ticker = Constants.TIMER_DURATION
            val priceCount = 94.99
            price.text = context?.resources?.getString(R.string.price)?.format(priceCount)
        }
    }

    override fun onEach() {
        onEach(viewModel.timer) {
            with(binding) {
                progressBar.progress = Constants.TIMER_DURATION - it
                if (it > 9)
                    timer.text = context?.resources?.getString(R.string.timer)?.format(it)
                else
                    timer.text = context?.resources?.getString(R.string.timer_count)?.format(it)
                if (it == 0) {
                    progressBar.visibility = View.INVISIBLE
                    freeCoins.isVisible = false
                }
            }
        }
    }

    override fun onViewClick() {
        with(binding) {
            getCoins.setOnClickListener {
                val mainDialogFragment = MainDialogFragment()
                val args = Bundle()
                args.putString(TITLE, resources.getString(R.string.error))
                args.putString(DESCRIPTION, resources.getString(R.string.error_content))
                mainDialogFragment.arguments = args
                mainDialogFragment.show(
                    childFragmentManager,
                    MainDialogFragment::class.java.simpleName
                )
            }
            closeBtn.setOnClickListener {
                dismiss()
            }
        }
    }

    companion object {
        const val TITLE = "TITLE"
        const val DESCRIPTION = "DESCRIPTION"
    }
}