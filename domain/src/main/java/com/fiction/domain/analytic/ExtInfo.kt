package com.fiction.domain.analytic

import com.google.gson.annotations.SerializedName

data class ExtInfo(
    @SerializedName("app_pkg_name")
    val appPkgName: String?,
    @SerializedName("avl_storage_size")
    val avlStorageSize: String?,
    @SerializedName("carrier_name")
    val carrierName: String?,
    @SerializedName("cpu_cores")
    val cpuCores: String?,
    @SerializedName("dev_model_name")
    val devModelName: String?,
    @SerializedName("dev_timezone")
    val devTimezone: String?,
    @SerializedName("dev_timezone_abv")
    val devTimezoneAbv: String?,
    @SerializedName("ext_info_ver")
    val extInfoVer: String?,
    @SerializedName("ext_storage_size")
    val extStorageSize: String?,
    @SerializedName("locale")
    val locale: String?,
    @SerializedName("os_ver")
    val osVer: String?,
    @SerializedName("pkg_info_ver_name")
    val pkgInfoVerName: String?,
    @SerializedName("pkg_ver_code")
    val pkgVerCode: String?,
    @SerializedName("screen_density")
    val screenDensity: String?,
    @SerializedName("screen_height")
    val screenHeight: String?,
    @SerializedName("screen_width")
    val screenWidth: String?
)
