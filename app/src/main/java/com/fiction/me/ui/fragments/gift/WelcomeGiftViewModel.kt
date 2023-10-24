package com.fiction.me.ui.fragments.gift

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.domain.interactors.CompleteGiftUseCase
import com.fiction.me.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class WelcomeGiftViewModel(
    private val completeGiftUseCase: CompleteGiftUseCase
) : BaseViewModel() {
    private val _claimCoins = MutableSharedFlow<String?>()
    val claimCoins = _claimCoins.asSharedFlow()

    fun completeGift(eventsId: Int, coinCount: Int) {
        viewModelScope.launch {
            when (val result = completeGiftUseCase(listOf(eventsId), coinCount)) {
                is ActionResult.Success -> {
                    _claimCoins.emit(result.result)
                    Log.i("claimCoins", "completeGift: completeGiftUseCase")
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }
}