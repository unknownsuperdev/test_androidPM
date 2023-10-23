package ru.tripster.guide.analytics

open class AnalyticEvent(
    val name: String,
    val parameters: Map<String, String> = emptyMap()
)