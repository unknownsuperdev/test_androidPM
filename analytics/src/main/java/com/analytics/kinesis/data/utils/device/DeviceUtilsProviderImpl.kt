package com.analytics.kinesis.data.utils.device

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import com.analytics.kinesis.data.repository.country.CountryApiRepository
import com.core.network.rest.response.model.ApiResponseSuccess
import java.util.*

internal class DeviceUtilsProviderImpl(
    private val context: Context,
    private val countryApiRepository: CountryApiRepository
) : DeviceUtilsProvider {

    private var country: String? = null

    override suspend fun getCountry(): String {
        return try {
            if (!country.isNullOrEmpty()) {
                country ?: "-"
            }else{
                val result = countryApiRepository.getCountryIp()
                if (result is ApiResponseSuccess){
                    country = result.data.country
                    country ?: "-"
                }else{
                    localDetectCountry()
                }
            }
        }catch (e: Throwable) {
            localDetectCountry()
        }
    }

    private fun localDetectCountry() = detectSIMCountry(context)
        ?: detectNetworkCountry(context)
        ?: detectLocaleCountry(context)
        ?: "-"

    override fun getLanguage(): String {
        return Locale.getDefault().language
    }

    override fun getPlatform(): String {
        return "android"
    }

    override fun getOS(): String {
        return Build.VERSION.RELEASE
    }

    @SuppressLint("HardwareIds")
    override fun getDeviceUuid(): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    private fun detectSIMCountry(context: Context): String? {
        try {
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return telephonyManager.simCountryIso
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun detectNetworkCountry(context: Context): String? {
        try {
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return telephonyManager.networkCountryIso
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun detectLocaleCountry(context: Context): String? {
        try {
            val localeCountryISO = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                context.resources.configuration.locales[0].country
            } else {
                context.resources.configuration.locale.country
            }
            return localeCountryISO
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}