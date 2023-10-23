package ru.tripster.guide.ui.fragments.profile

import android.content.Context
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.tripster.core.ActionResult
import ru.tripster.domain.interactors.profile.GetUserProfileInfoUseCase
import ru.tripster.domain.interactors.profile.LogOutUseCase
import ru.tripster.domain.model.MenuItems
import ru.tripster.domain.model.profile.UserInfo
import ru.tripster.guide.analytics.Analytic
import ru.tripster.guide.analytics.AnalyticsConstants.PROFILE_MENU_BUTTON
import ru.tripster.guide.analytics.MenuItemClicked
import ru.tripster.guide.appbase.viewmodel.BaseViewModel

class ProfileViewModel(
    private val profileInfoUseCase: GetUserProfileInfoUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val analytic: Analytic,
) :
    BaseViewModel() {

    private val _profileInfo: MutableSharedFlow<UserInfo> by lazy { MutableSharedFlow() }
    val profileInfo = _profileInfo.asSharedFlow()

    private val _errorUserInfo: MutableSharedFlow<Boolean?> by lazy { MutableSharedFlow() }
    val errorUserInfo = _errorUserInfo.asSharedFlow()

    private val _userInfo = MutableStateFlow<String?>(null)
    val userInfo = _userInfo.asSharedFlow()

    var details: UserInfo? = null

    fun getUserInfo() {
        viewModelScope.launch {
            when (val userInfo = callData(profileInfoUseCase())) {
                is ActionResult.Success -> {
                    _profileInfo.emit(userInfo.result)
                }

                is ActionResult.Error -> {
                    _errorUserInfo.emit(true)
                }
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            _userInfo.emit(logOutUseCase.invoke())
        }
    }

    fun menuItemClicked(context: Context) {
        analytic.send(
            MenuItemClicked(
                context = context,
                email = details?.email ?: "",
                guidId = details?.id ?: 0,
                menuButton = MenuItems.PROFILE.type,
                name = PROFILE_MENU_BUTTON
            )
        )
    }
}