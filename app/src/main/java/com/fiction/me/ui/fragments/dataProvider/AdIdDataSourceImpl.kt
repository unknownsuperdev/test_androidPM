package com.fiction.me.ui.fragments.dataProvider

import android.content.Context
import com.fiction.domain.utils.Constants.Companion.DISABLED_ADVERTISING_ID
import com.google.android.gms.ads.identifier.AdvertisingIdClient

class AdIdDataSourceImpl(private val context: Context) : AdIdDataSource {

    override fun getAdvertisingId(): Pair<String?, String> {
        return try {
            val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context)
            val advertisingId = adInfo.id
            val advertiserTrackingEnabled = if (adInfo.isLimitAdTrackingEnabled) "0" else "1"
            Pair(advertisingId ?: "", advertiserTrackingEnabled)
        } catch (e: Exception) {
            e.printStackTrace()
            Pair(DISABLED_ADVERTISING_ID, "0")
        }
    }
}