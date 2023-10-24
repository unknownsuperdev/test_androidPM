package com.analytics.kinesis

internal object Constant {
    internal object EventPropertyDefault {
        const val EVENT_PROPERTIES__COUNTRY = "event_properties__country"
        const val EVENT_PROPERTIES__LANGUAGE = "event_properties__language"
        const val EVENT_PROPERTIES__PLATFORM = "event_properties__platform"
        const val EVENT_PROPERTIES__OS = "event_properties__OS"
        const val EVENT_PROPERTIES_DEVICE_UUID = "event_properties__device_uuid"
        const val EVENT_PROPERTIES_USER_UUID = "event_properties__user_uuid"
        const val EVENT_PROPERTIES_SESSION_ID = "event_properties__session_id"
        const val EVENT_PROPERTIES__TIMESTAMP = "event_properties__timestamp"
        const val EVENT_PROPERTIES__EVENT_NAME = "event_properties__event_name"
        const val USER_PROPERTIES_SETUP = "user_properties_setup"
    }
    internal object DI {
        const val COUNTRY_QUALIFIER = "country"
    }
    internal object Network {
        const val COUNTRY_FIND_DOMAIN = "http://ip-api.com/"
    }
    internal object Session {
        const val DURATION_NEW_SESSION = 1800000L // half hour
    }
}