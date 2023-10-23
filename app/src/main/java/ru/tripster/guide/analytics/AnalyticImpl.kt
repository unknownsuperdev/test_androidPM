package ru.tripster.guide.analytics

import android.content.Context
import com.amplitude.analytics.connector.util.toJSONObject
import com.amplitude.api.Amplitude
import com.amplitude.api.AmplitudeClient
import ru.tripster.guide.App

class AnalyticImpl(val context: Context, apiKey: String) : Analytic {

    private val client: AmplitudeClient = Amplitude.getInstance()
        .initialize(context.applicationContext, apiKey)
        .enableForegroundTracking(App().getInstance())

    override fun send(event: AnalyticEvent) {
        client.logEvent(event.name, event.parameters.toJSONObject())
    }
}