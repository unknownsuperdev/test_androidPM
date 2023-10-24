package com.fiction.me.ui.fragments.dataProvider

import android.content.Context
import android.content.Intent
import android.util.Log
import com.appsflyer.AppsFlyerLib
import com.appsflyer.deeplink.DeepLink
import com.appsflyer.deeplink.DeepLinkListener
import com.appsflyer.deeplink.DeepLinkResult
import com.fiction.me.App
import com.fiction.me.ui.MainActivity
import com.fiction.me.utils.AnalyticAppsflyer
import com.fiction.me.utils.Constants
import com.fiction.me.utils.Constants.Companion.EMPTY
import com.fiction.me.utils.MyAppsFlyerConversionListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppsFlyerImpl(
    private val appsFlyerLib: AppsFlyerLib,
    private val conversionListener: MyAppsFlyerConversionListener,
    private val context: Context
) : IAppsFlyer {

    override fun init() {
        appsFlyerLib.subscribeForDeepLink(DeepLinkListener { deepLinkResult ->
            when (deepLinkResult.status) {
                DeepLinkResult.Status.FOUND -> Log.d(App.LOG_TAG, "Deep link found")
                DeepLinkResult.Status.NOT_FOUND -> {
                    Log.d(App.LOG_TAG, "Deep link not found")
                    return@DeepLinkListener
                }
                else -> {
                    Log.d(App.LOG_TAG, "Error getting Deep Link data: ${deepLinkResult.error}")
                    return@DeepLinkListener
                }
            }
            val deepLinkObj = deepLinkResult.deepLink
            goToActivity(deepLinkObj)
        })
        appsFlyerLib.init(Constants.AF_DEV_KEY, conversionListener, context)
        appsFlyerLib.start(context)
        setAppsFlyerData(context, appsFlyerLib)
    }

    private fun setAppsFlyerData(context: Context, appsFlyerLib: AppsFlyerLib) {
        CoroutineScope(Dispatchers.IO).launch {
            val appsflyerId: String = appsFlyerLib.getAppsFlyerUID(context) ?: EMPTY
            var idfa: String = EMPTY
            for (i in 0..5) {
                if (idfa == EMPTY)
                    idfa = appsFlyerLib.getAttributionId(context) ?: EMPTY
                else break
            }
            val uniqueId: String = appsFlyerLib.getAttributionId(context) ?: EMPTY
            AnalyticAppsflyer.appsflyerId = appsflyerId
            AnalyticAppsflyer.idfa = idfa
            AnalyticAppsflyer.uniqueId = uniqueId
        }
    }

    private fun goToActivity(dlData: DeepLink?) {
        try {
            val intent = Intent(context, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                action = Intent.ACTION_MAIN
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            if (dlData != null && dlData.clickEvent.has("deep_link_sub1")) {
                val deepLinkSub1 = dlData.getStringValue("deep_link_sub1")
                AnalyticAppsflyer.webInfo = deepLinkSub1
                intent.putExtra(App.DL_ATTRS, deepLinkSub1)
            }
        } catch (e: ClassNotFoundException) {
            Log.d(App.LOG_TAG, "Deep linking failed")
            e.printStackTrace()
        }
    }
}