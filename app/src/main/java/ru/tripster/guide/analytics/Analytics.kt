package ru.tripster.guide.analytics

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import ru.tripster.guide.analytics.AnalyticsConstants.APP_OPENED
import ru.tripster.guide.analytics.AnalyticsConstants.CHIPS_NAME
import ru.tripster.guide.analytics.AnalyticsConstants.DEVICE_ID
import ru.tripster.guide.analytics.AnalyticsConstants.DEVICE_MODEL
import ru.tripster.guide.analytics.AnalyticsConstants.EVENT_NUMBER
import ru.tripster.guide.analytics.AnalyticsConstants.EVENT_STATUS
import ru.tripster.guide.analytics.AnalyticsConstants.EXPERIENCE_TYPE
import ru.tripster.guide.analytics.AnalyticsConstants.GUID_ID
import ru.tripster.guide.analytics.AnalyticsConstants.MENU_BUTTON
import ru.tripster.guide.analytics.AnalyticsConstants.ORDER_NUMBER
import ru.tripster.guide.analytics.AnalyticsConstants.ORDER_REJECT_REASON
import ru.tripster.guide.analytics.AnalyticsConstants.ORDER_STATUS
import ru.tripster.guide.analytics.AnalyticsConstants.OS_VERSION
import ru.tripster.guide.analytics.AnalyticsConstants.TOURIST_ID
import ru.tripster.guide.analytics.AnalyticsConstants.USER_EMAIL
import ru.tripster.guide.analytics.AnalyticsConstants.USER_LOGGED_IN
import ru.tripster.guide.analytics.AnalyticsConstants.USER_TAP_CHIPS
import ru.tripster.guide.analytics.AnalyticsConstants.USER_TAP_MENU_BUTTON
import ru.tripster.guide.analytics.AnalyticsConstants.USER_TAP_ORDER_CANCEL_BUTTON
import ru.tripster.guide.extensions.getDeviceName

@SuppressLint("HardwareIds")
internal class AppOpened(context: Context) : AnalyticEvent(
    name = APP_OPENED,
    parameters = mapOf(
        DEVICE_ID to Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ), DEVICE_MODEL to getDeviceName(), OS_VERSION to "Android ${Build.VERSION.RELEASE}"
    )
)

@SuppressLint("HardwareIds")
internal class UserLoggedIn(context: Context, email: String) : AnalyticEvent(
    name = USER_LOGGED_IN,
    parameters = mapOf(
        USER_EMAIL to email,
        DEVICE_ID to Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ), DEVICE_MODEL to getDeviceName(), OS_VERSION to "Android ${Build.VERSION.RELEASE}"
    )
)

@SuppressLint("HardwareIds")
internal class UserTapChips(context: Context, email: String, guidId: Int, chipName: String) :
    AnalyticEvent(
        name = USER_TAP_CHIPS,
        parameters = mapOf(
            GUID_ID to guidId.toString(),
            USER_EMAIL to email,
            DEVICE_ID to Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            ), DEVICE_MODEL to getDeviceName(), OS_VERSION to "Android ${Build.VERSION.RELEASE}",
            CHIPS_NAME to chipName
        )
    )

@SuppressLint("HardwareIds")
internal class UserTapExperienceIndividualOrder(
    name: String,
    context: Context,
    email: String,
    guidId: Int,
    chipName: String,
    experienceType: String,
    orderStatus: String,
    orderNumber: Int,
    menuButton: String
) : AnalyticEvent(
    name = name,
    parameters = mapOf(
        GUID_ID to guidId.toString(),
        USER_EMAIL to email,
        DEVICE_ID to Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ), DEVICE_MODEL to getDeviceName(), OS_VERSION to "Android ${Build.VERSION.RELEASE}",
        CHIPS_NAME to chipName,
        EXPERIENCE_TYPE to experienceType,
        ORDER_STATUS to orderStatus,
        ORDER_NUMBER to orderNumber.toString(),
        MENU_BUTTON to menuButton
    )
)

@SuppressLint("HardwareIds")
internal class UserTapExperienceIndividual(
    name: String,
    context: Context,
    email: String,
    guidId: Int,
    experienceType: String,
    orderStatus: String,
    orderNumber: Int,
    menuButton: String,
    touristId: Int
) : AnalyticEvent(
    name = name,
    parameters = mapOf(
        GUID_ID to guidId.toString(),
        USER_EMAIL to email,
        DEVICE_ID to Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ), DEVICE_MODEL to getDeviceName(), OS_VERSION to "Android ${Build.VERSION.RELEASE}",
        EXPERIENCE_TYPE to experienceType,
        ORDER_STATUS to orderStatus,
        ORDER_NUMBER to orderNumber.toString(),
        MENU_BUTTON to menuButton,
        TOURIST_ID to touristId.toString()
    )
)

@SuppressLint("HardwareIds")
internal class CalendarDateExperienceIndividual(
    name: String,
    context: Context,
    email: String,
    guidId: Int,
    experienceType: String,
    orderStatus: String,
    orderNumber: Int,
    menuButton: String
) : AnalyticEvent(
    name = name,
    parameters = mapOf(
        GUID_ID to guidId.toString(),
        USER_EMAIL to email,
        DEVICE_ID to Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ), DEVICE_MODEL to getDeviceName(), OS_VERSION to "Android ${Build.VERSION.RELEASE}",
        EXPERIENCE_TYPE to experienceType,
        ORDER_STATUS to orderStatus,
        ORDER_NUMBER to orderNumber.toString(),
        MENU_BUTTON to menuButton
    )
)
@SuppressLint("HardwareIds")
internal class UserTapExperienceGroupTourOrder(
    name: String,
    context: Context,
    email: String,
    guidId: Int,
    chipName: String,
    experienceType: String,
    eventStatus: String,
    eventNumber: Int,
    menuButton: String
) : AnalyticEvent(
    name = name,
    parameters = mapOf(
        GUID_ID to guidId.toString(),
        USER_EMAIL to email,
        DEVICE_ID to Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ), DEVICE_MODEL to getDeviceName(), OS_VERSION to "Android ${Build.VERSION.RELEASE}",
        CHIPS_NAME to chipName,
        EXPERIENCE_TYPE to experienceType,
        EVENT_STATUS to eventStatus,
        EVENT_NUMBER to eventNumber.toString(),
        MENU_BUTTON to menuButton
    )
)

@SuppressLint("HardwareIds")
internal class UserTapExperienceGroupTour(
    name: String,
    context: Context,
    email: String,
    guidId: Int,
    experienceType: String,
    eventStatus: String,
    eventNumber: Int,
    menuButton: String,
    touristIds: String
) : AnalyticEvent(
    name = name,
    parameters = mapOf(
        GUID_ID to guidId.toString(),
        USER_EMAIL to email,
        DEVICE_ID to Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ), DEVICE_MODEL to getDeviceName(), OS_VERSION to "Android ${Build.VERSION.RELEASE}",
        EXPERIENCE_TYPE to experienceType,
        EVENT_STATUS to eventStatus,
        EVENT_NUMBER to eventNumber.toString(),
        MENU_BUTTON to menuButton,
        TOURIST_ID to touristIds
    )
)

@SuppressLint("HardwareIds")
internal class UserTapExperienceGroupTourWithOrderNumber(
    name: String,
    context: Context,
    email: String,
    guidId: Int,
    experienceType: String,
    eventStatus: String,
    eventNumber: Int,
    orderNumber: Int,
    menuButton: String,
    touristIds: String
) : AnalyticEvent(
    name = name,
    parameters = mapOf(
        GUID_ID to guidId.toString(),
        USER_EMAIL to email,
        DEVICE_ID to Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ), DEVICE_MODEL to getDeviceName(), OS_VERSION to "Android ${Build.VERSION.RELEASE}",
        EXPERIENCE_TYPE to experienceType,
        EVENT_STATUS to eventStatus,
        ORDER_NUMBER to orderNumber.toString(),
        EVENT_NUMBER to eventNumber.toString(),
        MENU_BUTTON to menuButton,
        TOURIST_ID to touristIds
    )
)

@SuppressLint("HardwareIds")
internal class CalendarDateExperienceGroupTour(
    name: String,
    context: Context,
    email: String,
    guidId: Int,
    experienceType: String,
    eventStatus: String,
    eventNumber: Int,
    menuButton: String
) : AnalyticEvent(
    name = name,
    parameters = mapOf(
        GUID_ID to guidId.toString(),
        USER_EMAIL to email,
        DEVICE_ID to Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ), DEVICE_MODEL to getDeviceName(), OS_VERSION to "Android ${Build.VERSION.RELEASE}",
        EXPERIENCE_TYPE to experienceType,
        EVENT_STATUS to eventStatus,
        EVENT_NUMBER to eventNumber.toString(),
        MENU_BUTTON to menuButton
    )
)
@SuppressLint("HardwareIds")
internal class UserTapConfirmButton(
    name: String,
    context: Context,
    email: String,
    guidId: Int,
    experienceType: String,
    orderStatus: String,
    orderNumber: Int,
    touristId: Int
) : AnalyticEvent(
    name = name,
    parameters = mapOf(
        GUID_ID to guidId.toString(),
        USER_EMAIL to email,
        DEVICE_ID to Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ), DEVICE_MODEL to getDeviceName(), OS_VERSION to "Android ${Build.VERSION.RELEASE}",
        EXPERIENCE_TYPE to experienceType,
        ORDER_STATUS to orderStatus,
        ORDER_NUMBER to orderNumber.toString(),
        TOURIST_ID to touristId.toString()
    )
)

@SuppressLint("HardwareIds")
internal class UserTapCancelButton(
    context: Context,
    email: String,
    guidId: Int,
    experienceType: String,
    orderStatus: String,
    orderNumber: Int,
    touristId: Int,
    orderRejectReason: String
) : AnalyticEvent(
    name = USER_TAP_ORDER_CANCEL_BUTTON,
    parameters = mapOf(
        GUID_ID to guidId.toString(),
        USER_EMAIL to email,
        DEVICE_ID to Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ),
        DEVICE_MODEL to getDeviceName(), OS_VERSION to "Android ${Build.VERSION.RELEASE}",
        EXPERIENCE_TYPE to experienceType,
        ORDER_STATUS to orderStatus,
        ORDER_NUMBER to orderNumber.toString(),
        ORDER_REJECT_REASON to orderRejectReason,
        TOURIST_ID to touristId.toString()
    )
)

@SuppressLint("HardwareIds")
internal class UserOrderGroupTourDetails(
    name: String,
    context: Context,
    email: String,
    guidId: Int,
    experienceType: String,
    orderStatus: String,
    orderNumber: Int,
    menuButton: String,
    touristId: Int
) : AnalyticEvent(
    name = name,
    parameters = mapOf(
        GUID_ID to guidId.toString(),
        USER_EMAIL to email,
        DEVICE_ID to Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ),
        DEVICE_MODEL to getDeviceName(), OS_VERSION to "Android ${Build.VERSION.RELEASE}",
        EXPERIENCE_TYPE to experienceType,
        ORDER_STATUS to orderStatus,
        ORDER_NUMBER to orderNumber.toString(),
        MENU_BUTTON to menuButton,
        TOURIST_ID to touristId.toString()
    )
)

@SuppressLint("HardwareIds")
internal class MenuItemClicked(
    context: Context,
    name: String,
    email: String,
    guidId: Int,
    menuButton: String,
) : AnalyticEvent(
    name = name,
    parameters = mapOf(
        GUID_ID to guidId.toString(),
        USER_EMAIL to email,
        DEVICE_ID to Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ),
        DEVICE_MODEL to getDeviceName(), OS_VERSION to "Android ${Build.VERSION.RELEASE}",
        MENU_BUTTON to menuButton
    )
)

@SuppressLint("HardwareIds")
internal class CalendarOrderClicked(
    context: Context,
    name: String,
    email: String,
    guidId: Int
) : AnalyticEvent(
    name = name,
    parameters = mapOf(
        GUID_ID to guidId.toString(),
        USER_EMAIL to email,
        DEVICE_ID to Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ),
        DEVICE_MODEL to getDeviceName(),
        OS_VERSION to "Android ${Build.VERSION.RELEASE}"
    )
)

@SuppressLint("HardwareIds")
internal class ApiKeyChanged(context: Context) : AnalyticEvent(
    name = APP_OPENED,
    parameters = mapOf(
        DEVICE_ID to Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ), DEVICE_MODEL to getDeviceName(), OS_VERSION to "Android ${Build.VERSION.RELEASE}"
    )
)