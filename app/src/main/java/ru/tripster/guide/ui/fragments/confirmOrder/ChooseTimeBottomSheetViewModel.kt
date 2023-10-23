package ru.tripster.guide.ui.fragments.confirmOrder

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.tripster.domain.interactors.confirmOrEdit.ChooseTourHourUseCase
import ru.tripster.domain.model.order.ChooseTime
import ru.tripster.guide.appbase.viewmodel.BaseViewModel

class ChooseTimeBottomSheetViewModel(private val chooseTourHourUseCase: ChooseTourHourUseCase) :
    BaseViewModel() {

    private val _timeList: MutableStateFlow<List<ChooseTime>?> by lazy { MutableStateFlow(null) }
    val timeList = _timeList.asSharedFlow()

    fun getTimeList(hour: String, listStartTime: String) {
        viewModelScope.launch {
            _timeList.emit(chooseTourHourUseCase.countTotalPrice(hour, listStartTime))
        }
    }

    fun getTimeListForCloseTime(
        hour: String,
        listStartTime: String,
        startTime: String,
        endTime: String
    ) {
        viewModelScope.launch {
            _timeList.emit(
                chooseTourHourUseCase.getTimeListForCloseTime(
                    hour,
                    listStartTime,
                    startTime,
                    endTime
                )
            )
        }
    }
}