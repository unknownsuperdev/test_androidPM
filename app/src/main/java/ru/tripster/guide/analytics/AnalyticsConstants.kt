package ru.tripster.guide.analytics

object AnalyticsConstants {
    const val API_KEY_AMPLITUDE_DEV = "426f52178a774413e92fa27f6619ddc6"
    const val API_KEY_AMPLITUDE_PROD = "ce5efe7fac9ff2b9ca1d8bed9b58dba4"

    //actions
    const val APP_OPENED = "app_opened"
    const val USER_LOGGED_IN = "authorization:user_logged_in.tab"
    const val USER_TAP_CHIPS = "orders:chips.tap"

    const val USER_TAP_EXPERIENCE_NAME = "orders:experience_name.tap"
    const val CALENDAR_DAY_EXPERIENCE_NAME = "calendar_day:experience_name.tap"

    const val GROUP_EXP_TRAVELLER_NAME = "group_exp:order_traveller_name.tap"

    const val ORDERS_TAP_CHAT_ICON = "orders:chat_icon.tap"
    const val PRIVATE_EXP_CHAT_ICON = "private_exp:chat_icon.tap"
    const val GROUP_EXP_CHAT_ICON = "group_exp:chat_icon.tap"
    const val GROUP_ORDER_CHAT_ICON = "group_order:chat_icon.tap"
    const val CALENDAR_DAY_CHAT_ICON = "calendar_day:chat_icon.tap"

    const val ORDERS_TAP_CHAT_PREVIEW = "orders:chat_preview.tap"
    const val PRIVATE_EXP_CHAT_PREVIEW = "private_exp:chat_preview.tap"
    const val GROUP_EXP_CHAT_PREVIEW = "group_exp:chat_preview.tap"
    const val GROUP_ORDER_CHAT_PREVIEW = "group_order:chat_preview.tap"

    const val ORDERS_TAP_CALL_ICON = "orders:call.tap"
    const val PRIVATE_EXP_CALL_ICON = "private_exp:call.tap"
    const val GROUP_EXP_CALL_ICON = "group_exp:chat_icon.tap"
    const val GROUP_ORDER_CALL_ICON = "group_order:call.tap"
    const val CALENDAR_DAY_CALL_ICON = "calendar_day:call.tap"

    const val ORDERS_CONFIRM_BUTTON = "orders:order_confirm.tap"
    const val PRIVATE_EXP_CONFIRM_BUTTON = "private_exp:order_confirm.tap"
    const val GROUP_EXP_CONFIRM_BUTTON = "group_exp:order_confirm.tap"

    const val USER_TAP_ORDER_CONFIRM_SUBMIT_BUTTON = "order_confirm:order_confirm_submit.tap"

    const val USER_TAP_ORDER_CHANGE_SUBMIT_BUTTON = "order_change:order_change_submit.tap"

    const val PRIVATE_EXP_ORDER_CHANGE_BUTTON = "private_exp:order_change.tap"

    const val PRIVATE_EXP_CONTACT_SUPPORT = "private_exp:contact_support.tap"
    const val GROUP_EXP_CONTACT_SUPPORT = "group_exp:contact_support.tap"
    const val GROUP_ORDER_CONTACT_SUPPORT = "group_order:contact_support.tap"

    const val USER_TAP_MENU_BUTTON = "user_tap_menu_button.tap"
    const val PRIVATE_EXP_MENU_BUTTON = "private_exp:menu.tap"
    const val ORDERS_MENU_BUTTON = "orders:menu.tap"
    const val CALENDAR_MENU_BUTTON = "calendar:menu.tap"
    const val PROFILE_MENU_BUTTON = "profile:menu.tap"
    const val GROUP_EXP_MENU_BUTTON = "group_exp:menu.tap"
    const val GROUP_ORDER_MENU_BUTTON = "group_order:menu.tap"
    const val CALENDAR_DAY_MENU_BUTTON = "calendar_day:menu.tap"

    const val PRIVATE_EXP_ORDER_CANCEL = "private_exp:order_cancel.tap"
    const val GROUP_ORDER_ORDER_CANCEL = "group_order:order_cancel.tap"

    const val USER_TAP_ORDER_CANCEL_BUTTON = "order_cancel:order_cancel_submit.tap"

    const val PRIVATE_EXP_COMMUNICATE_TIPS = "private_exp:communicate_tips.tap"

    const val PRIVATE_EXP_SHOW_TRAVELLER_CONTACT = "private_exp:show_traveller_contact.tap"

    const val PRIVATE_EXP_GET_CONTACT_TIP = "private_exp:get_contact_tip.tap"
    const val GROUP_ORDER_GET_CONTACT_TIP = "group_order:get_contact_tip.tap"

    const val PRIVATE_EXP_BACK_ICON = "private_exp:back.tap"
    const val GROUP_EXP_BACK_ICON = "group_exp:back.tap"
    const val GROUP_ORDER_BACK_ICON = "group_order:back.tap"
    const val CALENDAR_DAY_BACK_ICON = "calendar_day:back.tap"

    const val GROUP_EXP_REGISTRATION_CLOSE_BUTTON = "group_exp:registration_close.tap"

    const val GROUP_EXP_EVENT_EDIT = "group_exp:event_edit.tap"

    const val CALENDAR_EXPERIENCE_FILTER = "calendar:experience_filter.tap"
    const val CALENDAR_DAY_EXPERIENCE_FILTER = "calendar_day:experience_filter.tap"

    const val CALENDAR_CLOSE_TIME = "calendar:close_time.tap"
    const val CALENDAR_DAY_CLOSE_TIME = "calendar_day:close_time.tap"

    const val CALENDAR_DAY_TAP = "calendar:day.tap"

    const val CALENDAR_DAY_DELETE = "calendar_day:delete.tap"

    const val CLOSE_TIME_TAP = "close_time:close_time_submit.tap"

    //parameters
    const val DEVICE_ID = "Device ID"
    const val DEVICE_MODEL = "Device Model"
    const val OS_VERSION = "OS Version"
    const val USER_EMAIL = "User Email"
    const val CHIPS_NAME = "Chips name"
    const val GUID_ID = "ID_guide"
    const val TOURIST_ID = "ID_traveler"
    const val EXPERIENCE_TYPE = "Experience type"
    const val ORDER_STATUS = "Order status"
    const val ORDER_NUMBER = "Order number"
    const val EVENT_NUMBER = "Event number"
    const val EVENT_STATUS = "Event status"
    const val MENU_BUTTON = "Menu button"
    const val ORDER_REJECT_REASON = "Order Reject Reason"
}