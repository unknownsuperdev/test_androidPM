package com.core.network.rest

object ApplicationErrors {
    val UNKNOWN = -1
    val TIMEOUT = 1000
    val UNKNOW_HOST = 1001
    val NETWORK_ERROR = 1024
    val UNAVAILABLE_SERVERS = 20001
    val AUTHORIZATION_TOKEN_EXPIRED = 1023
}

object HttpErrors {
    val UNAUTHORIZED = 401
    val EXPIRED = 418
    val NOT_AUTH = 403
    val OTP = 423
}