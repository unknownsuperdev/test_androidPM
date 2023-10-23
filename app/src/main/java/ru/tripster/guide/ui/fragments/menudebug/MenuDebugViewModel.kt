package ru.tripster.guide.ui.fragments.menudebug

import android.content.Context
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.tripster.domain.interactors.menuDebug.GetCurrentStageUseCase
import ru.tripster.domain.interactors.menuDebug.SaveCurrentStageUseCase
import ru.tripster.guide.analytics.Analytic
import ru.tripster.guide.analytics.ApiKeyChanged
import ru.tripster.guide.appbase.viewmodel.BaseViewModel

class MenuDebugViewModel(
    private val analytics: Analytic,
    private val getCurrentStageUseCase: GetCurrentStageUseCase,
    private val saveCurrentStageUseCase: SaveCurrentStageUseCase
) : BaseViewModel() {
    var isSavedUser = false
    private val _currentStage = MutableStateFlow<String?>(null)
    val currentStage = _currentStage.asStateFlow()
    init {
        viewModelScope.launch {
            _currentStage.emit(getCurrentStageUseCase.invoke().first())
        }
    }
    fun changeApiKey(context: Context) {
        analytics.send(ApiKeyChanged(context))
    }

    fun saveCurrentStage(stage:String) {
        viewModelScope.launch {
            saveCurrentStageUseCase.invoke(stage)
        }
    }
}