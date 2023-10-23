package ru.tripster.domain.model.statistics

import ru.tripster.entities.responce.response.statistics.*

data class OrderStatisticsData(
    val bookingRate: BookingRateData,
    val canOpenTravelerContacts: Boolean,
    val confirmationRate: ConfirmationRateData,
    val notEnoughOrders: Boolean,
    val ordersRate: OrdersRateData,
    val reactionDelay: ReactionDelayData
) {
    companion object {
        fun from(statistics: OrderStatisticsResponse): OrderStatisticsData =
            with(statistics) {
                OrderStatisticsData(
                    bookingRate = BookingRateData.from(booking_rate),
                    canOpenTravelerContacts = can_open_traveler_contacts ?: false,
                    confirmationRate = ConfirmationRateData.from(confirmation_rate),
                    notEnoughOrders = not_enough_orders ?: false,
                    ordersRate = OrdersRateData.from(orders_rate),
                    reactionDelay = ReactionDelayData.from(reaction_delay)
                )
            }
    }
}

data class ExperienceClickModelIndividual(
    val experienceType: String,
    val orderStatus: String,
    val orderNumber: Int,
)

data class ExperienceClickModelGroup(
    val experienceType: String,
    val eventNumber: Int,
    val eventStatus: String,
)

data class BookingRateData(
    val excellentValue: Double,
    val goodValue: Double,
    val isBad: Boolean,
    val isExcellent: Boolean,
    val isGood: Boolean,
    val isPoor: Boolean,
    val isUndefined: Boolean,
    val poorValue: Double,
    val value: Double
) {
    companion object {
        fun from(booking: BookingRateResponse?): BookingRateData =
            with(booking) {
                BookingRateData(
                    this?.excellent_value ?: 0.0,
                    this?.good_value ?: 0.0,
                    this?.is_bad ?: false,
                    this?.is_excellent ?: false,
                    this?.is_good ?: false,
                    this?.is_poor ?: false,
                    this?.is_undefined ?: false,
                    this?.poor_value ?: 0.0,
                    this?.value ?: 0.0
                )
            }
    }
}

data class ConfirmationRateData(
    val excellentValue: Double,
    val goodValue: Double,
    val isBad: Boolean,
    val isExcellent: Boolean,
    val isGood: Boolean,
    val isPoor: Boolean,
    val isUndefined: Boolean,
    val poorValue: Double,
    val value: Double
) {
    companion object {
        fun from(confirm: ConfirmationRateResponse?): ConfirmationRateData =
            with(confirm) {
                ConfirmationRateData(
                    this?.excellent_value ?: 0.0,
                    this?.good_value ?: 0.0,
                    this?.is_bad ?: false,
                    this?.is_excellent ?: false,
                    this?.is_good ?: false,
                    this?.is_poor ?: false,
                    this?.is_undefined ?: false,
                    this?.poor_value ?: 0.0,
                    this?.value ?: 0.0
                )
            }
    }
}

data class OrdersRateData(
    val excellentValue: Double,
    val goodValue: Double,
    val isBad: Boolean,
    val isExcellent: Boolean,
    val isGood: Boolean,
    val isPoor: Boolean,
    val isUndefined: Boolean,
    val poorValue: Double,
    val value: Double
) {
    companion object {
        fun from(order: OrdersRateResponse?): OrdersRateData =
            with(order) {
                OrdersRateData(
                    this?.excellent_value ?: 0.0,
                    this?.good_value ?: 0.0,
                    this?.is_bad ?: false,
                    this?.is_excellent ?: false,
                    this?.is_good ?: false,
                    this?.is_poor ?: false,
                    this?.is_undefined ?: false,
                    this?.poor_value ?: 0.0,
                    this?.value ?: 0.0
                )
            }
    }
}

data class ReactionDelayData(
    val excellentValue: Int,
    val goodValue: Int,
    val isBad: Boolean,
    val isExcellent: Boolean,
    val isGood: Boolean,
    val isPoor: Boolean,
    val isUndefined: Boolean,
    val poorValue: Int,
    val value: Int
) {
    companion object {
        fun from(reaction: ReactionDelayResponse?): ReactionDelayData =
            with(reaction) {
                ReactionDelayData(
                    this?.excellent_value ?: 0,
                    this?.good_value ?: 0,
                    this?.is_bad ?: false,
                    this?.is_excellent ?: false,
                    this?.is_good ?: false,
                    this?.is_poor ?: false,
                    this?.is_undefined ?: false,
                    this?.poor_value ?: 0,
                    this?.value ?: 0
                )
            }
    }
}

