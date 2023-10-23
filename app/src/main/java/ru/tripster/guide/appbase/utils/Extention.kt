package ru.tripster.guide.appbase.utils

import android.app.Activity
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.Flow
import ru.tripster.guide.appbase.viewmodel.FlowObserver
import ru.tripster.guide.ui.MainFragment

inline fun <reified T> Flow<T>.observeInLifecycle(
    lifecycleOwner: LifecycleOwner
) = FlowObserver(lifecycleOwner, this)

fun FragmentActivity.bottomNavBarVisibility(visibility: Boolean) {
    this.supportFragmentManager.setFragmentResult(
        MainFragment.BOTTOM_NAV_BAR_VISIBILITY_KEY, bundleOf(MainFragment.BOTTOM_NAV_BAR_VISIBILITY_BUNDLE to visibility)
    )
}
