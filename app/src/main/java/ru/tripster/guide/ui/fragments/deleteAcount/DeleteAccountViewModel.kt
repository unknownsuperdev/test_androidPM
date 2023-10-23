package ru.tripster.guide.ui.fragments.deleteAcount

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.tripster.core.ActionResult
import ru.tripster.domain.interactors.profile.DeleteAccountUseCase
import ru.tripster.guide.appbase.viewmodel.BaseViewModel

class DeleteAccountViewModel(
    private val deleteAccountUseCase: DeleteAccountUseCase
) : BaseViewModel() {

    private val _deleteAccount = MutableStateFlow(false)
    val deleteAccount = _deleteAccount.asSharedFlow()

    private val _deleteAccountError: MutableSharedFlow<Boolean> by lazy { MutableSharedFlow() }
    val deleteAccountError = _deleteAccountError.asSharedFlow()

    fun deleteAccount() {
        viewModelScope.launch {
            when (val result = deleteAccountUseCase.invoke()) {
                is ActionResult.Success -> {
                    _deleteAccount.emit(true)
                }
                is ActionResult.Error -> {
                    _deleteAccountError.emit(true)
                }
            }
        }

    }
}
