package ru.tripster.guide.appbase

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.viewbinding.ViewBinding
import ru.tripster.guide.R
import ru.tripster.guide.appbase.viewmodel.BaseViewModel

abstract class FragmentBaseNCMVVM<ViewModel : BaseViewModel, ViewBind : ViewBinding> :
    FragmentBaseMVVM<ViewModel, ViewBind>() {

    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    protected fun removeResultListeners(vararg key: String) {
        key.forEach {
            activity?.supportFragmentManager?.clearFragmentResultListener(it)
        }
    }

    protected fun popBackStack() {
        navController.popBackStack()
    }

    protected fun navigateFragment(destinationId: Int, arg: Bundle? = null) {
        navController.navigate(destinationId, arg)
    }

    protected fun navigateFragment(destinations: NavDirections, navOptions: NavOptions? = null) {
        navController.navigate(destinations, navOptions = navOptions)
    }

    protected fun navigateFragmentDeepLink(uri: String, navOptions: NavOptions? = null) {
        navController.navigate(uri, navOptions = navOptions)
    }
    protected fun goToFirstFragment() {
        navController.popBackStack(R.id.loginFragment, false)
    }

}