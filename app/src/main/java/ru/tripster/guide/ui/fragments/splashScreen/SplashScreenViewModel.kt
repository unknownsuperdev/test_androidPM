package ru.tripster.guide.ui.fragments.splashScreen

import android.content.Context
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.tripster.domain.interactors.SplashUseCase
import ru.tripster.domain.interactors.notifications.GetNotificationStateUseCase
import ru.tripster.domain.interactors.notifications.GetRemindLaterTimeUseCase
import ru.tripster.guide.analytics.Analytic
import ru.tripster.guide.analytics.AppOpened
import ru.tripster.guide.appbase.viewmodel.BaseViewModel

class SplashScreenViewModel(
    private val splashUseCase: SplashUseCase,
    private val analytic: Analytic,
    private val getRemindLaterTimeUseCase: GetRemindLaterTimeUseCase,
    private val getNotificationStateUseCase: GetNotificationStateUseCase,
) : BaseViewModel() {

    init {
        getLastRemindLaterTime()
        getNotificationState()
    }

    private val _token = MutableStateFlow<String?>(null)
    val token = _token.asSharedFlow()

    var lastRemindLaterTime = ""
    var notificationStateIsSelected = false

    fun dataStore(deviceId: String, context: Context) {
        viewModelScope.launch {
            _token.emit(splashUseCase.invoke(deviceId))
            analytic.send(AppOpened(context))
        }
    }

    private fun getLastRemindLaterTime() {
        viewModelScope.launch {
            lastRemindLaterTime = getRemindLaterTimeUseCase.invoke()
        }
    }

    private fun getNotificationState() {
        viewModelScope.launch {
            notificationStateIsSelected = getNotificationStateUseCase.invoke()
        }
    }
}