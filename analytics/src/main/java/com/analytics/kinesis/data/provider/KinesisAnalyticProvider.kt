package com.analytics.kinesis.data.provider

import android.content.Context
import android.util.Log
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.internal.StaticCredentialsProvider
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserStateDetails
import com.amazonaws.mobileconnectors.kinesis.kinesisrecorder.KinesisRecorder
import com.amazonaws.mobileconnectors.kinesis.kinesisrecorder.KinesisRecorderConfig
import com.amazonaws.regions.Regions
import com.analytics.api.AnalyticProvider
import com.analytics.kinesis.Constant.EventPropertyDefault.EVENT_PROPERTIES_DEVICE_UUID
import com.analytics.kinesis.Constant.EventPropertyDefault.EVENT_PROPERTIES_SESSION_ID
import com.analytics.kinesis.Constant.EventPropertyDefault.EVENT_PROPERTIES_USER_UUID
import com.analytics.kinesis.Constant.EventPropertyDefault.EVENT_PROPERTIES__EVENT_NAME
import com.analytics.kinesis.Constant.EventPropertyDefault.EVENT_PROPERTIES__LANGUAGE
import com.analytics.kinesis.Constant.EventPropertyDefault.EVENT_PROPERTIES__OS
import com.analytics.kinesis.Constant.EventPropertyDefault.EVENT_PROPERTIES__PLATFORM
import com.analytics.kinesis.Constant.EventPropertyDefault.EVENT_PROPERTIES__TIMESTAMP
import com.analytics.kinesis.Constant.EventPropertyDefault.USER_PROPERTIES_SETUP
import com.analytics.kinesis.data.utils.device.DeviceUtilsProvider
import org.json.JSONObject
import org.koin.core.component.KoinComponent
import java.lang.Exception
import java.util.*

class KinesisAnalyticProvider(
    private val context: Context,
    private val streamName: String,
    private val durationNewSession: Long,
    private val deviceUtilsProvider: DeviceUtilsProvider,
    private val kinesisDirectory: String = "kinesisDirectory",
) : AnalyticProvider, KoinComponent {

    private var lastEventTime: Long = 0
    private var sessionId: String = ""
    private var userId: String? = null
    private var deviceId: String = deviceUtilsProvider.getDeviceUuid()
    private var recorder: KinesisRecorder? = null

    init {
        setup()
    }

    private fun setup() {
        AWSMobileClient.getInstance().initialize(context.applicationContext, object : Callback<UserStateDetails> {
            override fun onResult(result: UserStateDetails?) {
                recorder = KinesisRecorder(
                    context.getDir(kinesisDirectory, 0),
                    Regions.US_EAST_2,
                    AWSMobileClient.getInstance(),
                    KinesisRecorderConfig().withPartitionKey(streamName)
                )
            }

            override fun onError(e: Exception?) {
            }
        })
    }

    private fun checkRefreshSessionId(): String {
        val time = getTimestamp()
        if ((time - lastEventTime) > durationNewSession) {
            sessionId = UUID.randomUUID().toString()
        }
        lastEventTime = time
        return sessionId
    }

    override fun setUserId(userId: String) {
        this.userId = userId
    }

    override fun setDeviceId(deviceId: String) {
        this.deviceId = deviceId
    }

    override suspend fun logEvent(event: String, eventProperty: Map<String, Any>) {
        val plusEventName = eventProperty.toMutableMap()
        plusEventName[EVENT_PROPERTIES__EVENT_NAME] = event
        sendEvent(plusEventName)
    }

    override suspend fun setUserProperty(userProperty: Map<String, Any>) {
        val plusEventName = userProperty.toMutableMap()
        plusEventName[EVENT_PROPERTIES__EVENT_NAME] = USER_PROPERTIES_SETUP
        sendEvent(plusEventName)
    }

    private suspend fun sendEvent(map: Map<String, Any>) {
        checkRefreshSessionId()
        val eventProperty = collectEventPropertyDefault()
        eventProperty.putAll(map)
        Log.d("eventProperty", "${JSONObject(eventProperty.toMap())}")
        recorder?.saveRecord(
            "${JSONObject(eventProperty.toMap())}",
            streamName
        )
        recorder?.submitAllRecords()
    }

    private fun getTimestamp() = System.currentTimeMillis()

    private suspend fun collectEventPropertyDefault(): MutableMap<String, Any> {
        return mutableMapOf(
            EVENT_PROPERTIES__LANGUAGE to deviceUtilsProvider.getLanguage(),
            EVENT_PROPERTIES__PLATFORM to deviceUtilsProvider.getPlatform(),
            EVENT_PROPERTIES__OS to deviceUtilsProvider.getOS(),
            EVENT_PROPERTIES_DEVICE_UUID to deviceId,
            EVENT_PROPERTIES_USER_UUID to (userId ?: ""),
            EVENT_PROPERTIES_SESSION_ID to sessionId,
            EVENT_PROPERTIES__TIMESTAMP to (getTimestamp() / 1000),
        )
    }
}