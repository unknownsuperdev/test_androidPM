package com.fiction.me.utils

object InternalDeepLink {
    const val DOMAIN = "myApp://fiction.me/"

    fun makeCustomDeepLinkToBookSummary(
        bookId: Long,
        bookInfoJson: String,
        isOpeningFromOnBoarding: Boolean = false
    ): String {
        return "${DOMAIN}request?itemId=${bookId}&bookInfoJson=${bookInfoJson}&isOpeningFromOnBoarding=$isOpeningFromOnBoarding&isFromPush=${bookInfoJson.isEmpty()}"
    }

    fun makeCustomDeepLinkForStore(isFromProfile: Boolean): String {
        return "${DOMAIN}request?isFromProfile=$isFromProfile"
    }

    fun makeCustomDeepLinkForReader(chapterId: Long = -1L, bookId: Long, isFromOnBoarding: Boolean = false): String {
        return "${DOMAIN}request?chapterId=${chapterId}&bookId=${bookId}&isFromOnBoarding=${isFromOnBoarding}"
    }

    fun makeCustomDeepLinkForReader(chapterId: Long = -1L, bookId: Long, isFromOnBoarding: Boolean = false, referrer: String): String {
        return "${DOMAIN}request?chapterId=${chapterId}&bookId=${bookId}&isFromOnBoarding=${isFromOnBoarding}&referrer=${referrer}"
    }

    fun makeCustomDeepLinkForVerification(email: String): String {
        return "${DOMAIN}request?email=${email}"
    }
}