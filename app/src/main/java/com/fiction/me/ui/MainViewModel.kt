package com.fiction.me.ui

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.core.utils.Uuid
import com.fiction.domain.analytic.AnalyticApple
import com.fiction.domain.analytic.AnalyticFacebook
import com.fiction.domain.interactors.*
import com.fiction.domain.utils.Constants.Companion.DISABLED_ADVERTISING_ID
import com.fiction.me.BuildConfig
import com.fiction.me.appbase.viewmodel.BaseViewModel
import com.fiction.me.ui.fragments.dataProvider.AdIdDataSource
import com.fiction.me.utils.AnalyticAppsflyer
import com.fiction.me.utils.Constants.Companion.ANDROID
import com.fiction.me.utils.Constants.Companion.APPLE
import com.fiction.me.utils.Constants.Companion.FACEBOOK
import com.fiction.me.utils.Constants.Companion.GOOGLE
import com.fiction.me.utils.Constants.Companion.TIKTOK
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainViewModel(
    private val setPushTokenUseCase: SetPushTokenUseCase,
    private val getPushTokenFromLocalUseCase: GetPushTokenFromLocalUseCase,
    private val setPushNotIdToLocalDbUseCase: SetPushNotIdToLocalDbUseCase,
    private val setAppleDataUseCase: SetAppleDataUseCase,
    private val setFacebookDataUseCase: SetFacebookDataUseCase,
    private val setGoogleDataUseCase: SetGoogleDataUseCase,
    private val setTiktokDataUseCase: SetTiktokDataUseCase,
    private val uuid: Uuid,
    private val getIsLogged: GetIsLoggedStatusUseCase,
    private val adInfo: AdIdDataSource,
    private val appsFlyerUseCase: SetAppsflyerDataUseCase,
    private val setAndroidDataUseCase: SetAndroidDataUseCase,
    private val getProfileInfoUseCase: GetProfileInfoUseCase,
    private val getAvailableTariffsUseCase: GetAvailableTariffsUseCase
) : BaseViewModel() {
    var currentDestinationId: Int? = null
    var isMakeFbCall = true
    var isClearBackStack = false

    fun getFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.i("FirebaseMessagingToken", "onCreate: $token")
                setPushToken(token)
                return@OnCompleteListener
            }
        })
    }

    fun setAppsFlyerData() {
        viewModelScope.launch {
            AnalyticAppsflyer.run {
                appsFlyerUseCase(
                    appsflyerId,
                    idfa,
                    uniqueId
                )
            }
        }
    }

    fun setAndroidData() {
        CoroutineScope(Dispatchers.IO).launch {
            val advertiserId = adInfo.getAdvertisingId().first ?: return@launch
            when(val result = setAndroidDataUseCase(advertiserId)){
                is ActionResult.Success -> {
                    Log.i("setAndroidData", "setAndroidDataResponse: ${result.result}")
                }
                is ActionResult.Error -> {}
            }
        }
    }

    fun setFbData(fbAnalyticData: AnalyticFacebook, webInfo: JSONObject? = null) {
        CoroutineScope(Dispatchers.IO).launch {
            val (advertiserId, advertiserTrackingEnabled) = adInfo.getAdvertisingId()
            if (advertiserId != DISABLED_ADVERTISING_ID) {
                val fbInfo = fbAnalyticData.copy(
                    attribution = AnalyticAppsflyer.idfa,
                    advertiserId = advertiserId,
                    applicationTrackingEnabled = advertiserTrackingEnabled,
                    advertiserTrackingEnabled = advertiserTrackingEnabled
                )

                when (val result = setFacebookDataUseCase(fbInfo, webInfo)) {
                    is ActionResult.Success -> {
                        Log.i("setAndroidData", "SUCCESS: setAndroidDataResponse: ${result.result}")
                    }
                    is ActionResult.Error -> {}
                }
            }
        }
    }

    private fun setPushToken(pushToken: String) {
        viewModelScope.launch {
            getPushTokenFromLocalUseCase().collectLatest {
                if (it != pushToken)
                    setPushTokenUseCase(pushToken)
            }
        }
    }

    fun makeAnalyticCall(
        analyticType: String,
        webInfo: JSONObject,
        analyticFcbInfo: AnalyticFacebook? = null
    ) {
        viewModelScope.launch {
            when (analyticType) {
                GOOGLE -> setGoogleDataUseCase(webInfo)
                FACEBOOK -> analyticFcbInfo?.let {
                    setFbData(analyticFcbInfo, webInfo)
                }
                TIKTOK -> setTiktokDataUseCase(webInfo)
                APPLE -> {
                    val isLogged = getIsLogged()
                    setAppleDataUseCase(
                        AnalyticApple(
                            BuildConfig.VERSION_NAME,
                            authorizationStatus = if (isLogged == true) "1" else "0",
                            udid = uuid.getUuid(),
                            os = ANDROID
                        )
                    )
                }
            }
        }
    }

    fun updateBalance(){
        viewModelScope.launch {
            getProfileInfoUseCase(true)
            getAvailableTariffsUseCase(isMakeCallAnyway = true)
        }
    }

    fun storePushIdInLocale(pushId: Int) {
        viewModelScope.launch {
            setPushNotIdToLocalDbUseCase(pushId)
        }
    }
}


