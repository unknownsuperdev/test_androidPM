package com.fiction.me.ui.analytics

import android.content.Context
import com.amplitude.api.AmplitudeClient
import com.analytics.api.AnalyticProvider
import com.analytics.api.Events
import com.appsflyer.AppsFlyerLib
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.Collections

class AnalyticsImpl(
    private val context: Context,
    private val amplitudeClient: AmplitudeClient,
    private val appsflyer: AppsFlyerLib,
    private val kinesisClient: AnalyticProvider
) : Analytics {

    private val coroutineScope = CoroutineScope(Job())
    private var userId: String? = null
    private val eventBuffer = mutableListOf<MutableMap<String, Map<String, Any>>>()
    private val userPropertyBuffer = mutableListOf<Map<String, Any>>()

    override fun setUserId(userId: String) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    amplitudeClient.userId = userId
                    kinesisClient.setUserId(userId)
                    val eventBufferIterator = eventBuffer.iterator()
                    while (eventBufferIterator.hasNext()) {
                        val events = eventBufferIterator.next()
                        events.entries.forEach { event ->
                            amplitudeClient.logEvent(
                                event.key,
                                JSONObject(event.value as Map<*, *>)
                            )
                            kinesisClient.logEvent(event.key, event.value)
                        }
                    }
                } catch (t: Throwable) {
                }
            }
            val userPropertyBufferIterator = userPropertyBuffer.iterator()
            while (userPropertyBufferIterator.hasNext()) {
                try {
                    val events = userPropertyBufferIterator.next()
                    kinesisClient.setUserProperty(events)
                } catch (t: Throwable) {
                }
            }
            this@AnalyticsImpl.userId = userId
        }
    }


    override fun trackEvent(
        eventsType: Analytics.EventsType,
        eventName: String,
        params: HashMap<String, Any>
    ) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    when (eventsType) {
                        Analytics.EventsType.AMPLITUDE -> {
                            if (userId == null) {
                                eventBuffer.add(mutableMapOf(eventName to params))
                            } else {
                                amplitudeClient.logEvent(eventName, JSONObject(params as Map<*, *>))
                                prepareAndSendEventKinesis(eventName, params)
                            }
                        }

                        Analytics.EventsType.APPS_FLYER -> {
                            appsflyer.logEvent(context, eventName, params)
                        }

                        else -> {
                            if (userId == null) {
                                eventBuffer.add(mutableMapOf(eventName to params))
                            } else {
                                amplitudeClient.logEvent(eventName, JSONObject(params as Map<*, *>))
                                prepareAndSendEventKinesis(eventName, params)
                            }
                            appsflyer.logEvent(context, eventName, params)
                        }
                    }
                } catch (t: Throwable) {
                }
            }
        }
    }

    override fun setUserProperty(property: Map<String, Any>) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    if (userId == null) {
                        userPropertyBuffer.add(property)
                    } else {
                        kinesisClient.setUserProperty(property)
                    }
                }catch (e: Throwable){}
            }
        }
    }

    private suspend fun prepareAndSendEventKinesis(
        eventName: String,
        params: HashMap<String, Any>
    ) {
        val plusEventPropertiesName = mutableMapOf<String, Any>()
        params.entries.forEach {
            plusEventPropertiesName["${Events.EVENT_PROPERTIES}${it.key}"] = it.value
        }
        kinesisClient.logEvent(eventName, plusEventPropertiesName)
    }
}