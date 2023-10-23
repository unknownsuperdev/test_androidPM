package ru.tripster.guide.ui.fragments.calendar

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.*
import ru.tripster.domain.interactors.сalendar.ExperienceUseCase
import ru.tripster.guide.appbase.viewmodel.BaseViewModel

class CalendarFiltrationBottomSheetViewModel(
    private val experienceUseCase: ExperienceUseCase
) : BaseViewModel() {
    var isClosingTime = false

    val pagingData = MutableStateFlow(false)
        .map { experienceUseCase.invoke(true, isClosingTime) }
        .flatMapLatest { it }
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty()).cachedIn(viewModelScope)
}