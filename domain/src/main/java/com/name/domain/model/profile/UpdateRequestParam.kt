package com.name.domain.model.profile

import okhttp3.RequestBody

data class UpdateRequestParam(
    val name: String?,
    val avatar: RequestBody?,
    val autoUnlockPaid: Boolean?,
    val readingMode: Boolean?
)