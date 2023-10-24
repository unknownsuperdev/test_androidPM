package com.name.jat.ui.fragments.onboarding

import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.appbase.viewmodel.BaseViewModel
import com.name.jat.databinding.FragmentWelcomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class WelcomeFragment : FragmentBaseNCMVVM<BaseViewModel, FragmentWelcomeBinding>() {
    override val binding: FragmentWelcomeBinding by viewBinding()
    override val viewModel: BaseViewModel by viewModel()

    override fun onViewClick() {
        binding.startBtn.setOnClickListener {
            val directions = WelcomeFragmentDirections.actionWelcomeFragmentToSelectGenderFragment()
            navigateFragment(directions)
        }
    }

}