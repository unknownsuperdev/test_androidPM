package ru.tripster.guide.ui.fragments.notifications

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.tripster.domain.interactors.notifications.SetNotificationStateUseCase
import ru.tripster.domain.interactors.notifications.SetRemindLaterTimeUseCase
import ru.tripster.guide.appbase.viewmodel.BaseViewModel
import ru.tripster.guide.extensions.currentDateAndTime

class NotificationsViewModel(
    private val setRemindLaterTimeUseCase: SetRemindLaterTimeUseCase,
    private val setNotificationStateUseCase : SetNotificationStateUseCase
) :
    BaseViewModel() {

    fun setRemindLaterTime() {
        viewModelScope.launch {
            setRemindLaterTimeUseCase.invoke(currentDateAndTime())
        }
    }

    fun setNotificationState() {
        viewModelScope.launch {
            setNotificationStateUseCase.invoke(true)
        }
    }
}