package com.fiction.me.ui.fragments.gift

import android.util.Log
import androidx.activity.addCallback
import androidx.navigation.fragment.navArgs
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentWelcomeGiftBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class WelcomeGiftFragment :
    FragmentBaseNCMVVM<WelcomeGiftViewModel, FragmentWelcomeGiftBinding>() {
    override val binding: FragmentWelcomeGiftBinding by viewBinding()
    override val viewModel: WelcomeGiftViewModel by viewModel()
    private val args: WelcomeGiftFragmentArgs by navArgs()

    override fun onView() {
        with(binding) {
            bodyTV.text = args.giftCoinCount
        }
        activity?.onBackPressedDispatcher?.addCallback(this) {
            viewModel.completeGift(args.id, args.coinCount)
        }
    }

    override fun onViewClick() {
        with(binding) {
            closeBtn.setOnClickListener {
                viewModel.completeGift(args.id, args.coinCount)
            }
            claimCoins.setOnClickListener {
                Log.i("claimCoins", "onViewClick: click_claimCoins")
                viewModel.completeGift(args.id, args.coinCount)
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.claimCoins) {
            popBackStack()
        }
    }
}