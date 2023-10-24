package com.fiction.me.appbase.viewmodel

import androidx.lifecycle.ViewModel
import com.fiction.me.ui.analytics.Analytics
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


abstract class BaseViewModel : ViewModel(), KoinComponent {
    protected val analytics: Analytics by inject()

    fun trackEvents(
        event: String,
        params: HashMap<String, Any> = hashMapOf(),
        eventsType: Analytics.EventsType = Analytics.EventsType.AMPLITUDE,
    ) {
        analytics.trackEvent(
            eventsType,
            event,
            params
        )
    }

    fun setUserPropertyEvent(params: Map<String, Any>) {
        analytics.setUserProperty(params)
    }

}