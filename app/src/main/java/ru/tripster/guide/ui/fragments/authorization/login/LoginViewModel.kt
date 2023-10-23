package ru.tripster.guide.ui.fragments.authorization.login

import android.content.Context
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.tripster.core.ActionResult
import ru.tripster.core.AuthErrorBody
import ru.tripster.core.CallException
import ru.tripster.domain.interactors.LoginUserCase
import ru.tripster.guide.analytics.Analytic
import ru.tripster.guide.analytics.UserLoggedIn
import ru.tripster.guide.appbase.viewmodel.BaseViewModel

class LoginViewModel(private val loginUserCase: LoginUserCase, private val analytics: Analytic) :
    BaseViewModel() {

    private val _loginSuccess: MutableSharedFlow<Unit> by lazy { MutableSharedFlow() }
    val loginSuccess = _loginSuccess.asSharedFlow()

    private val _loginError: MutableSharedFlow<Int?> by lazy { MutableSharedFlow() }
    val loginError = _loginError.asSharedFlow()

    var eyeState = true
    var usernameError = true
    var passwordError = true
    var clickCount = 0
    var saveUser = false

    fun login(username: String, password: String, context: Context) {
        viewModelScope.launch {
            when (val result = callData(loginUserCase(username, password, saveUser))) {
                is ActionResult.Success -> {
                    _loginSuccess.emit(Unit)
                    analytics.send(UserLoggedIn(context, username))
                }

                is ActionResult.Error -> {
                    val errorBody = (result.errors as? CallException)?.errorCode
                    _loginError.emit(errorBody)
                }
            }
        }
    }
}