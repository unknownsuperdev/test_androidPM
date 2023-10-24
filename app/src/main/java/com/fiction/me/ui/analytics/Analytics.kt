package com.fiction.me.ui.analytics

interface Analytics {

    fun setUserId(userId: String)

    fun trackEvent(eventsType: EventsType = EventsType.AMPLITUDE, eventName: String, params: HashMap<String, Any> = hashMapOf())

    fun setUserProperty(property: Map<String, Any>)

    enum class EventsType{
        AMPLITUDE,
        APPS_FLYER,
        ALL
    }
}