package com.fiction.me.ui.fragments.explore.notenoughcoins

import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.domain.interactors.GetBalanceUseCase
import com.fiction.me.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotEnoughCoinsViewModel(
    private val getBalanceUseCase: GetBalanceUseCase
):BaseViewModel() {
    private val _userBalance = MutableStateFlow<Int?>(null)
    val userBalance = _userBalance.asStateFlow()

    fun getBalance() {
        viewModelScope.launch {
            when (val result = getBalanceUseCase()) {
                is ActionResult.Success -> {
                    _userBalance.emit(result.result)
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }
}