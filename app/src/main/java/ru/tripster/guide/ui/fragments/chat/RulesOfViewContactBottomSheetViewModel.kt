package ru.tripster.guide.ui.fragments.chat

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.tripster.core.ActionResult
import ru.tripster.domain.interactors.chat.EditTravelerContactsFieldUseCase
import ru.tripster.domain.model.confirmOrEdit.EditTravelerContactsOpen
import ru.tripster.domain.model.confirmOrEdit.OrderConfirmOrEditData
import ru.tripster.domain.model.order.OrderDetails
import ru.tripster.guide.appbase.viewmodel.BaseViewModel

class RulesOfViewContactBottomSheetViewModel(private val orderEditUseCase: EditTravelerContactsFieldUseCase) :
    BaseViewModel() {

    private val _editOrder: MutableStateFlow<OrderDetails?> by lazy { MutableStateFlow(null) }
    val editOrder = _editOrder.asSharedFlow()

    private val _dismiss = MutableSharedFlow<Boolean>()
    val dismiss = _dismiss.asSharedFlow()

    fun editOrder(id: Int, editOrder: EditTravelerContactsOpen) {
        viewModelScope.launch {
            when (val result = orderEditUseCase(id, editOrder)) {
                is ActionResult.Success -> {
                    _editOrder.emit(result.result)
                    _dismiss.emit(true)
                }
                is ActionResult.Error -> {
                    _dismiss.emit(false)
                }
            }
        }
    }
}