package com.fiction.me.utils

import android.util.Log
import com.appsflyer.AppsFlyerConversionListener
import com.fiction.me.App
import java.util.*

class MyAppsFlyerConversionListener : AppsFlyerConversionListener {
    var attribution = ""

    override fun onConversionDataSuccess(conversionData: Map<String, Any>) {
        for (attrName in conversionData.keys) Log.d(
            App.LOG_TAG,
            "Conversion attribute: " + attrName + " = " + conversionData[attrName]
        )
        val mediaSource = conversionData["media_source"]
        val campaign = conversionData["campaign"]
        attribution = "media source=$mediaSource, campaign=$campaign"
        val status: String = Objects.requireNonNull(conversionData["af_status"]).toString()
        if (status == "Non-organic") {
            if (Objects.requireNonNull(conversionData["is_first_launch"]).toString() == "true"
            ) {
                Log.d(App.LOG_TAG, "Conversion: First Launch")
            } else {
                Log.d(App.LOG_TAG, "Conversion: Not First Launch")
            }
        } else {
            Log.d(App.LOG_TAG, "Conversion: This is an organic install.")
        }
    }

    override fun onConversionDataFail(errorMessage: String) {
        Log.d(App.LOG_TAG, "error getting conversion data: $errorMessage")
    }

    override fun onAppOpenAttribution(attributionData: Map<String, String>) {
        Log.d(App.LOG_TAG, "onAppOpenAttribution: This is fake call.")
    }

    override fun onAttributionFailure(errorMessage: String) {
        Log.d(App.LOG_TAG, "error onAttributionFailure : $errorMessage")
    }
}