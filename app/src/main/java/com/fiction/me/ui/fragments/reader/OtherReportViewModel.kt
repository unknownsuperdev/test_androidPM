package com.fiction.me.ui.fragments.reader

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.domain.interactors.SendReportUseCase
import com.fiction.domain.model.ReportParams
import com.fiction.me.appbase.viewmodel.BaseViewModel
import com.fiction.me.utils.Constants.Companion.OTHER
import com.fiction.me.utils.Events
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class OtherReportViewModel(
    private val sendReportUseCase: SendReportUseCase
) : BaseViewModel() {
    private val _reportSend = MutableSharedFlow<Boolean>()
    val reportSend = _reportSend.asSharedFlow()
    private val otherReportId = 5

    fun sendingReport(bookId: Long,reportText: String, chapterId: Long, percent: Float) {
        viewModelScope.launch {
            val reportParams = ReportParams(percent, otherReportId, reportText)
            when (val result = sendReportUseCase(chapterId, reportParams)) {
                is ActionResult.Success -> {
                    trackEvents(
                        Events.REPORT_SENT,
                        hashMapOf(
                            Events.BOOK_ID_PROPERTY to bookId,
                            Events.REASON to OTHER
                        )
                    )
                    _reportSend.emit(true)
                }
                is ActionResult.Error -> {
                    Log.i("sendReport", "sendReports: ERROR")
                    _reportSend.emit(false)
                }
            }
        }
    }
}