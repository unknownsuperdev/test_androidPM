package com.name.jat.appbase.utils

import androidx.lifecycle.LifecycleOwner
import com.name.jat.appbase.viewmodel.FlowObserver
import kotlinx.coroutines.flow.Flow

inline fun <reified T> Flow<T>.observeInLifecycle(
    lifecycleOwner: LifecycleOwner
) = FlowObserver(lifecycleOwner, this)