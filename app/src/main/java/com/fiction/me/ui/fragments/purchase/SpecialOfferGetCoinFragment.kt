package com.fiction.me.ui.fragments.purchase

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentSpecialOfferGetCoinBinding
import com.fiction.me.utils.Constants.Companion.TIMER_DURATION
import com.fiction.me.ui.fragments.explore.GetMoreCoinsDialogFragment
import com.fiction.me.ui.fragments.purchase.maindialog.MainDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SpecialOfferGetCoinFragment :
    FragmentBaseNCMVVM<SpecialOfferGetCoinViewModel, FragmentSpecialOfferGetCoinBinding>() {

    override val viewModel: SpecialOfferGetCoinViewModel by viewModel()
    override val binding: FragmentSpecialOfferGetCoinBinding by viewBinding()

    override fun onView() {
        with(binding) {
            viewModel.ticker = TIMER_DURATION
            val priceCount = 94.99
            price.text = context?.resources?.getString(R.string.price)?.format(priceCount)
        }
    }

    override fun onEach() {
        with(binding) {
            onEach(viewModel.timer) {
                circleProgressBar.progress = TIMER_DURATION - it
                if (it > 9)
                    timer.text = context?.resources?.getString(R.string.timer)?.format(it)
                else
                    timer.text = context?.resources?.getString(R.string.timer_count)?.format(it)
                if (it == 0) {
                    circleProgressBar.visibility = View.INVISIBLE
                    freeCoins.isVisible = false
                }
            }

            onEach(viewModel.getCoinProgress) {
                progressContainer.isVisible = false
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
    }

    override fun onViewClick() {
        with(binding) {
            getCoins.setOnClickListener {
                progressContainer.isVisible = true
                viewModel.isStartProgress = true
            }
            closeBtn.setOnClickListener {
                popBackStack()
            }
        }
    }
}