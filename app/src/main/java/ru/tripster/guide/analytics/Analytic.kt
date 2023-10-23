package ru.tripster.guide.analytics

interface Analytic {
    /**
     * Отправка события
     */
    fun send(event: AnalyticEvent)
}