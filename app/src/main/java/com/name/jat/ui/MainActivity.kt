package com.name.jat.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.name.jat.R
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewBinding: ActivityMainBinding by viewBinding()
    private val viewModel: MainViewModel by viewModel()
    lateinit var navController: NavController
    private lateinit var navOptions: NavOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)

        setContentView(viewBinding.root)
        supportActionBar?.hide()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        viewBinding.bottomNav.itemIconTintList = null
        navController = navHostFragment.navController
        navOptions = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setPopUpTo(R.id.exploreFragment, false)
            .build()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.bookSummaryFragment -> viewBinding.bottomNav.visibility = View.VISIBLE
                R.id.reportFragment -> viewBinding.bottomNav.visibility = View.GONE
                R.id.readerFragment -> viewBinding.bottomNav.visibility = View.GONE
                R.id.editProfileFragment -> viewBinding.bottomNav.visibility = View.GONE
                R.id.profileFragment -> viewBinding.bottomNav.visibility = View.VISIBLE
            }
        }

        viewBinding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item_explore -> navController.navigate(R.id.exploreFragment)
                R.id.item_library -> navController.navigate(R.id.mainLibraryFragment)
                R.id.item_profile -> navController.navigate(R.id.profileFragment)
            }
            return@setOnItemSelectedListener true
        }
    }
}