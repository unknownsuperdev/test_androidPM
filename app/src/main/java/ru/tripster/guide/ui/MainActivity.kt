package ru.tripster.guide.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import ru.tripster.guide.R
import ru.tripster.guide.appbase.utils.viewBinding
import ru.tripster.guide.databinding.ActivityMainBinding
import ru.tripster.guide.enums.DeepLinkPrefix
import ru.tripster.guide.extensions.setStatusBarColor


class MainActivity : AppCompatActivity() {

    private val viewBinding: ActivityMainBinding by viewBinding()

    lateinit var navController: NavController

    companion object {
        const val DEEP_LINK_ORDER_ID = "DEEP_LINK_ORDER_ID_KEY"
        const val DEEP_LINK_PREFIX = "DEEP_LINK_PREFIX_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window?.apply {
//            decorView.systemUiVisibility =
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
//
//            var flags: Int = decorView.systemUiVisibility
//            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//            decorView.systemUiVisibility = flags
//        }

        setContentView(viewBinding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val appLinkAction: String? = intent?.action
        val appLinkData: Uri? = intent?.data

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment

        navController = navHostFragment.navController
//        viewBinding.bottomNav.setupWithNavController(navController)

        if (appLinkAction == Intent.ACTION_VIEW && appLinkData != null) {
            val bundle = Bundle()

            when {
                appLinkData.toString()
                    .contains(DeepLinkPrefix.DELETE_ACCOUNT.prefix) -> bundle.putString(
                    DEEP_LINK_PREFIX,
                    DeepLinkPrefix.DELETE_ACCOUNT.prefix
                )

                else -> {
                    val link = appLinkData.toString().substringBefore("/?")

                    if (link.takeLastWhile { it > '/' }.toIntOrNull() != null) {
                        bundle.putInt(DEEP_LINK_ORDER_ID, link.takeLastWhile { it > '/' }.toInt())
                        bundle.putString(DEEP_LINK_PREFIX, DeepLinkPrefix.EXPERIENCE_ORDER.prefix)
                    }

                }
            }

//            navController.navigate(R.id.splashScreenFragment, bundle)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//
//                R.id.loginFragment -> bottomNavBarVisibility(false)
//                R.id.splashScreenFragment -> bottomNavBarVisibility(false)
//                R.id.orderCancelFragment -> bottomNavBarVisibility(false)
//                R.id.deleteAccountFragment -> bottomNavBarVisibility(false)
//                R.id.editPriceBottomSheet -> bottomNavBarVisibility(false)
//                R.id.chooseTimeBottomSheet -> bottomNavBarVisibility(false)
//                R.id.calendarFragment -> bottomNavBarVisibility(false)
//                R.id.chatFragment -> bottomNavBarVisibility(false)
//                R.id.confirmOrderFragment -> bottomNavBarVisibility(false)
//                R.id.sendContactsBottomSheet -> bottomNavBarVisibility(false)
//                R.id.menuDebugFragment -> bottomNavBarVisibility(false)
//                R.id.calendarCloseTimeFragment -> bottomNavBarVisibility(false)
//                else -> bottomNavBarVisibility(true)
//            }

            when (destination.id) {

                R.id.editPriceBottomSheet -> {
                    if (navController.previousBackStackEntry?.destination?.id == R.id.orderDetailsGroupTourFragment)
                        transparentStatusBar()
                }

                else -> {
                    setMargins(0)
                    this.setStatusBarColor(ContextCompat.getColor(this, android.R.color.white))
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    private fun setMargins(top: Int) {
        val layoutParams = viewBinding.navHost.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.setMargins(0, top, 0, 0)
        viewBinding.navHost.layoutParams = layoutParams
    }

    fun transparentStatusBar() {
        setMargins(0)
        setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent))
        window?.decorView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
}