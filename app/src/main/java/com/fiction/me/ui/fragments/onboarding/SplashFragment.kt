package com.fiction.me.ui.fragments.onboarding

import android.os.Bundle
import android.util.Log
import androidx.core.view.WindowCompat
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentSplashBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : FragmentBaseNCMVVM<SplashViewModel, FragmentSplashBinding>() {
    override val binding: FragmentSplashBinding by viewBinding()
    override val viewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, true)
    }

}