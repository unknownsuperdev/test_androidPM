package com.fiction.me.ui.fragments.onboarding

import androidx.activity.addCallback
import androidx.navigation.NavDirections
import androidx.navigation.fragment.navArgs
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.appbase.viewmodel.BaseViewModel
import com.fiction.me.databinding.FragmentWelcomeBinding
import com.fiction.me.utils.Events.Companion.START_BUTTON_CLICKED
import com.fiction.me.utils.Events.Companion.WELCOME_SCREEN_SHOWN
import org.koin.androidx.viewmodel.ext.android.viewModel

class WelcomeFragment : FragmentBaseNCMVVM<BaseViewModel, FragmentWelcomeBinding>() {
    override val binding: FragmentWelcomeBinding by viewBinding()
    override val viewModel: WelcomeViewModel by viewModel()
    private val args: WelcomeFragmentArgs by navArgs()
    private var directions: NavDirections? = null

    override fun onView() {
        viewModel.trackEvents(WELCOME_SCREEN_SHOWN)
        activity?.onBackPressedDispatcher?.addCallback(this) {
          // activity?.finish()
        }
    }

    override fun onViewClick() {
        binding.startBtn.setOnClickListener {
            when (args.screens[0]) {
                "gender" -> {
                    val nextScreens =
                        args.screens.filterIndexed { index, s -> index > 0 }.toTypedArray()
                    directions =
                        WelcomeFragmentDirections.actionWelcomeFragmentToSelectGenderFragment(
                            nextScreens
                        )
                }
                "reading_time" -> {
                    val nextScreens =
                        args.screens.filterIndexed { index, s -> index > 0 }.toTypedArray()
                    directions =
                        WelcomeFragmentDirections.actionWelcomeFragmentToPickReadingTimeFragment(
                            nextScreens
                        )
                }
                "tags" -> {
                    directions =
                        WelcomeFragmentDirections.actionWelcomeFragmentToChooseFavoriteThemeFragment()
                }
                "for_you" -> {
                    directions =
                        WelcomeFragmentDirections.actionWelcomeFragmentToSelectionBooksFragment()
                }
                else -> {
                    directions =
                        WelcomeFragmentDirections.actionWelcomeFragmentToExploreFragment()
                }
            }
            viewModel.trackEvents(START_BUTTON_CLICKED)
            directions?.let { navigateFragment(it) }
        }
    }
}