package com.fiction.me.ui.fragments.reader

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.domain.interactors.SendReportUseCase
import com.fiction.domain.model.ReportFieldsModel
import com.fiction.domain.model.ReportParams
import com.fiction.me.appbase.viewmodel.BaseViewModel
import com.fiction.me.utils.Events.Companion.BOOK_ID_PROPERTY
import com.fiction.me.utils.Events.Companion.REASON
import com.fiction.me.utils.Events.Companion.REPORT_SENT
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReportViewModel(
    private val sendReportUseCase: SendReportUseCase
) : BaseViewModel() {

    private val _reportFields = MutableStateFlow<List<ReportFieldsModel>?>(null)
    val reportFields = _reportFields.asStateFlow()

    private val _reportSend = MutableSharedFlow<Unit>()
    val reportSend = _reportSend.asSharedFlow()
    var isOtherField = false
    private var selectedId: Int? = null

    private val selectedReportFields = mutableListOf(
        ReportFieldsModel(1, "Grammatical errors"),
        ReportFieldsModel(2, "Content loading failure"),
        ReportFieldsModel(3, "Typography confusion"),
        ReportFieldsModel(4, "Repeated content or chapters"),
        ReportFieldsModel(5, "Other")
    )

    fun getReportsList() {
        viewModelScope.launch {
            _reportFields.emit(selectedReportFields)
        }
    }

    fun updateReportSelection(id: Int) {
        val reports = reportFields.value
        selectedId = id
        val updatedList = reports?.map { it.copy(isSelected = it.id == id) }
        viewModelScope.launch {
            _reportFields.emit(updatedList)
        }
    }

    fun sendReports(bookId: Long, chapterId: Long, percent: Float) {
        viewModelScope.launch {
            selectedId?.let { id ->
                val reportParams = ReportParams(percent, id)
                when (val result = sendReportUseCase(chapterId, reportParams)) {
                    is ActionResult.Success -> {
                        trackEvents(
                            REPORT_SENT,
                            hashMapOf(
                                BOOK_ID_PROPERTY to bookId,
                                REASON to selectedReportFields[id-1].text
                            )
                        )
                        _reportSend.emit(Unit)
                    }
                    is ActionResult.Error -> {
                        Log.i("sendReport", "sendReports: ERROR")
                        // TODO() //Error Handling
                    }
                }
            }
        }
    }
}