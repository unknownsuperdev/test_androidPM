package com.name.jat.ui.fragments.reader

import androidx.lifecycle.viewModelScope
import com.name.domain.model.ReportFieldsModel
import com.name.jat.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReportViewModel : BaseViewModel() {
    private val selectedReportFields = mutableListOf<Long>()

    private val _reportFields = MutableStateFlow<List<ReportFieldsModel>?>(null)
    val reportFields = _reportFields.asStateFlow()

    private val _reportSend = MutableSharedFlow<Unit>()
    val reportSend = _reportSend.asSharedFlow()
    var isOtherField = false

    fun addReportField(
        isSelected: Boolean,
        id: Long
    ) {
        if (isSelected && !selectedReportFields.contains(id))
            selectedReportFields.add(id)
        else if (!isSelected && selectedReportFields.contains(id)) {
            selectedReportFields.remove(id)
        }
    }

    fun isSelectedReportFieldsListEmpty() = selectedReportFields.isNullOrEmpty()

    fun getReportsList() {
        val selectedReportFields = mutableListOf(
            ReportFieldsModel(91, "Grammatical errors"),
            ReportFieldsModel(92, "Content loading failure"),
            ReportFieldsModel(93, "Typography confusion"),
            ReportFieldsModel(94, "Content loading failure"),
            ReportFieldsModel(95, "Other"),
        )
        viewModelScope.launch {
            _reportFields.emit(selectedReportFields)
        }
    }

    fun sendReports() {
        if (!selectedReportFields.isNullOrEmpty()) {
            viewModelScope.launch {
                delay(2000)
                _reportSend.emit(Unit)
            }
        }
    }
}