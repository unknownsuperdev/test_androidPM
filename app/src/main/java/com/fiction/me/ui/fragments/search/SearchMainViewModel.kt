package com.fiction.me.ui.fragments.search

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.domain.interactors.SetViewInBookUseCase
import com.fiction.me.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class SearchMainViewModel(
    private val setViewInBookUseCase: SetViewInBookUseCase,
) : BaseViewModel(){

    fun setViewInBook(bookId: Long) {
        viewModelScope.launch {
            when (setViewInBookUseCase(bookId)) {
                is ActionResult.Success -> {
                    Log.i("setViewInBook", "Success")
                }
                is ActionResult.Error -> {

                }
            }
        }
    }
}