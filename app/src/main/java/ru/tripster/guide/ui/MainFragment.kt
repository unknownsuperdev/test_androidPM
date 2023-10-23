package ru.tripster.guide.ui

import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.tripster.guide.R
import ru.tripster.guide.appbase.FragmentBaseNCMVVM
import ru.tripster.guide.appbase.utils.viewBinding
import ru.tripster.guide.databinding.FragmentMainBinding

class MainFragment : FragmentBaseNCMVVM<MainFragmentViewModel, FragmentMainBinding>() {

    override val viewModel: MainFragmentViewModel by viewModel()
    override val binding: FragmentMainBinding by viewBinding()
    lateinit var navControllerMain: NavController
    lateinit var navOptions: NavOptions

    override fun onView() {
        val navHostTabs =
            childFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navControllerMain = navHostTabs.navController
        getFragmentResult()

        navOptions = navOptions {
            launchSingleTop = true
            restoreState = true
            popUpTo(navControllerMain.graph.findStartDestination().id) {
                saveState = true
            }
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_orders -> {
                    activity?.supportFragmentManager?.setFragmentResult(
                        AMPLITUDE_KEY, bundleOf(
                            AMPLITUDE_BUNDLE to 0
                        )
                    )
                    navControllerMain.navigate(R.id.ordersContainerFragment, null, navOptions)
                    true
                }
                R.id.nav_calendar -> {
                    activity?.supportFragmentManager?.setFragmentResult(
                        AMPLITUDE_KEY, bundleOf(
                            AMPLITUDE_BUNDLE to 1
                        )
                    )
                    navControllerMain.navigate(R.id.calendarContainerFragment, null, navOptions)
                    true
                }
                R.id.nav_profile -> {
                    activity?.supportFragmentManager?.setFragmentResult(
                        AMPLITUDE_KEY, bundleOf(
                            AMPLITUDE_BUNDLE to 2
                        )
                    )
                    navControllerMain.navigate(R.id.profileContainerFragment, null, navOptions)
                    true
                }
                else -> false
            }
        }
    }

    override fun onStop() {
        super.onStop()
        activity?.supportFragmentManager?.clearFragmentResult(BOTTOM_NAV_BAR_VISIBILITY_KEY)
        activity?.supportFragmentManager?.clearFragmentResult(BADGE_KEY)
        activity?.supportFragmentManager?.clearFragmentResult(NAVIGATE_KEY)
    }

    private fun getFragmentResult() {

        activity?.supportFragmentManager?.setFragmentResultListener(
            BOTTOM_NAV_BAR_VISIBILITY_KEY,
            viewLifecycleOwner
        ) { _, result ->
            bottomNavBarVisibility(result.getBoolean(BOTTOM_NAV_BAR_VISIBILITY_BUNDLE))
        }

        activity?.supportFragmentManager?.setFragmentResultListener(
            BADGE_KEY,
            viewLifecycleOwner
        ) { _, result ->
            setBadge(result.getInt(BADGE_BUNDLE))
        }

        activity?.supportFragmentManager?.setFragmentResultListener(
            NAVIGATE_KEY,
            viewLifecycleOwner
        ) { _, result ->
            navControllerMain.navigate(R.id.ordersContainerFragment, null, navOptions)
            binding.bottomNav.menu.getItem(0).isChecked = true

            result.getString(NAVIGATE_BUNDLE)?.let { navControllerMain.navigate(it) }
        }

        activity?.supportFragmentManager?.setFragmentResultListener(
            BADGE_KEY,
            viewLifecycleOwner
        ) { _, result ->
            setBadge(result.getInt(BADGE_BUNDLE))
        }

        activity?.supportFragmentManager?.setFragmentResultListener(
            PUT_Y_KEY,
            viewLifecycleOwner
        ) { _, _ ->
            activity?.supportFragmentManager?.setFragmentResult(
                GET_Y_KEY, bundleOf(GET_Y_BUNDLE to binding.bottomNav.top)
            )
        }

    }

    companion object {
        const val BOTTOM_NAV_BAR_VISIBILITY_KEY = "Bottom Nav Bar Visibility Key"
        const val BOTTOM_NAV_BAR_VISIBILITY_BUNDLE = "Bottom Nav Bar Visibility Bundle"

        const val BADGE_KEY = "Badge key"
        const val BADGE_BUNDLE = "Badge bundle"

        const val NAVIGATE_KEY = "Navigate key"
        const val NAVIGATE_BUNDLE = "Navigate bundle"

        const val PUT_Y_KEY = "Put Y Key"

        const val GET_Y_KEY = "Get Y Key"
        const val GET_Y_BUNDLE = "Get Y Bundle"

        const val AMPLITUDE_KEY = "Amplitude Key"
        const val AMPLITUDE_BUNDLE = "Amplitude Bundle"
    }

    fun bottomNavBarVisibility(isVisible: Boolean) {
        binding.bottomNav.isVisible = isVisible
    }

    private fun setBadge(badgeNumber: Int) {
        val badge = binding.bottomNav.getOrCreateBadge(R.id.nav_orders)
        context?.let {
            badge.backgroundColor = ContextCompat.getColor(it, R.color.tomat)
            badge.horizontalOffset = 10
            badge.verticalOffset = 5
            badge.isVisible = badgeNumber > 0
            badge.number = badgeNumber
        }
    }

}