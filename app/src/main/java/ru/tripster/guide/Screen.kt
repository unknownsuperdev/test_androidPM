package ru.tripster.guide

const val INDIVIDUAL_ORDERS = "order_details_individual_fragment"
const val GROUP_TOUR_ORDERS = "order_details_group_tour_fragment"
const val CHAT = "chat_fragment"
const val CONFIRM_OR_EDIT = "confirm_or_edit_fragment"
const val LOGIN_FRAGMENT = "login_fragment"
const val CHOOSE_TIME_BOTTOM_SHEET = "choose_time_bottom_sheet"

const val ORDER_ID = "orderId"
const val DEEP_LINK_ORDER_ID = "deepLinkOrderId"
const val ORDER = "order"
const val EVENT_ID = "eventId"
const val STATISTIC_DATA = "statisticData"
const val TYPE = "type"
const val IS_NAVIGATED_DATE_ORDER = "isNavigatedDateOrder"
const val TYPE_OF_NAVIGATION = "typeOfNavigation"
const val GUIDE_LAST_VISIT_DATE = "guideLastVisitDate"
const val EXPERIENCE_TITLE = "experienceTitle"
const val IS_CONFIRM = "isConfirm"
const val USER_INFO = "userInfo"
const val IS_SAVED_USER = "isSavedUser"
const val TIME = "time"
const val IS_FROM_CLOSE_TIME = "isFromCloseTime"
const val LIST_START_TIME = "listStartTime"
const val START_TIME = "startTime"
const val END_TIME = "endTime"

sealed class Screen {

    object Login : Screen() {
        fun show(
            deepLinkOrderId: Int,
            userInfo: String,
            isSavedUser: Boolean
        ): String {
            return "$LOGIN_FRAGMENT/$DEEP_LINK_ORDER_ID=${deepLinkOrderId}/$USER_INFO=${userInfo}/$IS_SAVED_USER=${isSavedUser}"
        }
    }

    object ChooseTime : Screen() {
        fun show(
            time: String,
            isFromCloseTime: Boolean,
            listStartTime: String,
            startTime: String,
            endTime: String
        ): String {
            return "$CHOOSE_TIME_BOTTOM_SHEET/$TIME=${time}/$IS_FROM_CLOSE_TIME=${isFromCloseTime}/$LIST_START_TIME=${listStartTime}/$START_TIME=${startTime}/$END_TIME=${endTime}"
        }
    }

    object IndividualOrders : Screen() {
        fun show(
            orderId: Int,
            eventId: Int,
            type: String,
            isNavigatedDateOrder: Boolean,
            statisticData: String
        ): String {
            return "$INDIVIDUAL_ORDERS/$ORDER_ID=${orderId}/$EVENT_ID=${eventId}/$TYPE=${type}/$IS_NAVIGATED_DATE_ORDER=${isNavigatedDateOrder}/$STATISTIC_DATA=${statisticData}"
        }
    }

    object GroupTourOrders : Screen() {
        fun show(
            orderId: Int,
            isNavigatedDateOrder: Boolean,
            statisticData: String,
            guideLastVisitDate: String
        ): String {
            return "$GROUP_TOUR_ORDERS/$ORDER_ID=${orderId}/$IS_NAVIGATED_DATE_ORDER=${isNavigatedDateOrder}/$STATISTIC_DATA=${statisticData}/$GUIDE_LAST_VISIT_DATE=${guideLastVisitDate}"
        }
    }

    object Chat : Screen() {
        fun show(order: String, typeOfNavigation: String, statisticData: String): String {
            return "$CHAT/$ORDER=${order}/$TYPE_OF_NAVIGATION=${typeOfNavigation}/$STATISTIC_DATA=${statisticData}"
        }
    }

    object ConfirmOrEdit : Screen() {
        fun show(orderId: Int, type: String, experienceTitle: String, isConfirm: Boolean): String {
            return "$CONFIRM_OR_EDIT/$ORDER_ID=${orderId}/$TYPE=${type}/$EXPERIENCE_TITLE=${experienceTitle}/$IS_CONFIRM=${isConfirm}"
        }
    }


}