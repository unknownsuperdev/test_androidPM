package ru.tripster.guide.ui.fragments.confirmOrder

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import ru.tripster.guide.appbase.viewmodel.BaseViewModel

class EditPriceBottomSheetViewModel: BaseViewModel() {
    var totalPrice = 0
    private val _dismiss = MutableSharedFlow<Unit>()
    val dismiss = _dismiss


    fun dismiss() {
        viewModelScope.launch {
            delay(1000)
            _dismiss.emit(Unit)
        }
    }
}