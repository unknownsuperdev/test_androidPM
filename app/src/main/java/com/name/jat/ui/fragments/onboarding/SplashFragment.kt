package com.name.jat.ui.fragments.onboarding

import androidx.navigation.NavDirections
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.databinding.FragmentSplashBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : FragmentBaseNCMVVM<SplashViewModel, FragmentSplashBinding>() {
    override val binding: FragmentSplashBinding by viewBinding()
    override val viewModel: SplashViewModel by viewModel()
    private var directions: NavDirections? = null
    override fun onView() {
        viewModel.run {
            startTimer()
            getIsOpenedWelcomeScreens()
        }
    }

    override fun onEach() {
        onEach(viewModel.durationEnded) {
            directions?.let {
                navigateFragment(it)
            }
        }
        onEach(viewModel.isOpenedWelcomeScreens) { isOpened ->
            isOpened?.let {
                if (it) {
                    directions = SplashFragmentDirections.actionSplashFragmentToExploreFragment()
                } else directions = SplashFragmentDirections.actionSplashFragmentToWelcomeFragment()
            }

        }
    }
}