package com.core.network.rest.header

interface Header {
    suspend fun getHeaders(): Map<String, String>
}