package com.name.jat.ui.fragments.reader

import androidx.lifecycle.viewModelScope
import com.name.jat.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class OtherReportViewModel : BaseViewModel() {
    private val _reportSend = MutableSharedFlow<Unit?>()
    val reportSend = _reportSend.asSharedFlow()

    fun sendingReport(reportText: String) {
        viewModelScope.launch {
            delay(2000)
            _reportSend.emit(Unit)
        }
    }
}