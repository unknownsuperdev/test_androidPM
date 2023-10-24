package com.fiction.domain.model.notification

enum class NotificationType(val type: String) {
    READER("reader"),
    BOOK("book"),
    COINSHOP("coinshop"),
    CHALLENGE("challenge"),
    USER_PROFILE("user_profile"),
    AUTH("auth"),
    MAIN("main");


    companion object {

        fun String.toNotificationType(): NotificationType =
            when (this) {
                "reader" -> READER
                "book" -> BOOK
                "coinshop" -> COINSHOP
                "challenge" -> CHALLENGE
                "user_profile" -> USER_PROFILE
                "auth" -> AUTH
                else -> MAIN
            }
    }
}

