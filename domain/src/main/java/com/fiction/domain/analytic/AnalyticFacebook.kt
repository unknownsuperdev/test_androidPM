package com.fiction.domain.analytic

import com.google.gson.annotations.SerializedName

data class AnalyticFacebook(
    @SerializedName("advertiser_id")
    val advertiserId: String?,
    @SerializedName("advertiser_tracking_enabled")
    val advertiserTrackingEnabled: String?,
    @SerializedName("application_tracking_enabled")
    val applicationTrackingEnabled: String?,
    @SerializedName("attribution")
    val attribution: String?,
    @SerializedName("bundle_short_version")
    val bundleShortVersion: String?,
    @SerializedName("bundle_version")
    val bundleVersion: String?,
    @SerializedName("extinfo")
    val extinfo: ExtInfo?,
    /*@SerializedName("uuid")
    val uuid: String?*/
)
