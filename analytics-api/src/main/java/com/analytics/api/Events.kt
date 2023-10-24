package com.analytics.api

object Events {
    const val USER_PROPERTIES__APP_VERSION = "user_properties__app_version"
    const val USER_PROPERTIES__START_APP_VERSION = "user_properties__start_app_version"
    const val USER_PROPERTIES__DEVICE_UUID = "user_properties__device_uuid"
    const val USER_PROPERTIES__USER_UUID = "user_properties__user_uuid"

    const val USER_PROPERTY__COHORT_DAY = "user_properties__cohort_day"
    const val USER_PROPERTY__COHORT_WEEK = "user_properties__cohort_week"
    const val USER_PROPERTY__COHORT_MONTH = "user_properties__cohort_month"
    const val USER_PROPERTY__COHORT_YEAR = "user_properties__cohort_year"
    const val USER_PROPERTY__USER_GENDER = "user_properties__user_gender"
    const val USER_PROPERTY__FAVOURITE_READING_TIME = "user_properties__favourite_reading_time"
    const val USER_PROPERTY__FAVORITE_THEMES = "user_properties__favorite_themes"
    const val USER_PROPERTY__STATUS = "user_properties__status"
    const val USER_PROPERTY__CHAPTERS_AUTO_UNLOCK = "user_properties__chapters_auto_unlock"
    const val USER_PROPERTY__READING_MODE = "user_properties__reading_mode"
    const val USER_PROPERTY__POPULAR_TAGS_FILTER = "user_properties__popular_tags_filter"
    const val USER_PROPERTY__THEME_MODE = "user_properties__theme_mode"
    const val USER_PROPERTY__USER_COIN_BALANCE = "user_properties__user_coin_balance"
    const val USER_PROPERTY__USER_COINS_SPEND = "user_properties__user_coins_spend" // server
    const val USER_PROPERTY__USER_DOLLARS_SPEND_CUMULATIVE = "user_properties__user_dollars_spend_cumulative" // server
    const val USER_PROPERTY__TOP_UP_AMOUNT = "user_properties__top_up_amount" // server
    const val USER_PROPERTY__BOOKS_AMOUNT = "user_properties__books_amount" // server
    const val USER_PROPERTY__USER_TYPE = "user_properties__user_type"

    // user property value
    const val USER_PROPERTY_STATUS_NOT_REGISTERED = "not_registered"
    const val USER_PROPERTY__THEME_MODE_DARK = "dark"
    const val USER_PROPERTY__USER_TYPE_FREE = "free"
    const val USER_PROPERTY__USER_TYPE_PREMIUM = "premium"

    const val EVENT_PROPERTIES = "event_properties__"
}