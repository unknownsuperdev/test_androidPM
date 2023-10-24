package com.fiction.me.ui.fragments.profile.settings

import androidx.lifecycle.viewModelScope
import com.analytics.api.Events.USER_PROPERTY__READING_MODE
import com.fiction.core.ActionResult
import com.fiction.domain.interactors.GetProfileInfoUseCase
import com.fiction.domain.interactors.UpdateProfileUseCase
import com.fiction.me.appbase.viewmodel.BaseViewModel
import com.fiction.me.utils.Events
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReadingModeViewModel(
    private val getProfileInfoUseCase: GetProfileInfoUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : BaseViewModel() {

    private val _isAdult = MutableStateFlow<Boolean?>(null)
    val isAdult = _isAdult.asStateFlow()

    fun getReadingMode() {
        viewModelScope.launch {
            when (val result = getProfileInfoUseCase()) {
                is ActionResult.Success -> {
                    _isAdult.emit(result.result.setting.readingMode)
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    fun changeReadingMode(isTeenager: Boolean) {
        viewModelScope.launch {
            when (updateProfileUseCase(readingMode = isTeenager)) {
                is ActionResult.Success -> {
                    setUserPropertyEvent(mapOf(USER_PROPERTY__READING_MODE to if (isTeenager) Events.ADULT else Events.TEENAGER))
                    _isAdult.emit(isTeenager)
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }
}