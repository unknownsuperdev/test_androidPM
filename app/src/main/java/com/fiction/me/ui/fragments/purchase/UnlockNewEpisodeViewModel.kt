package com.fiction.me.ui.fragments.purchase

import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.domain.interactors.BuyChapterUseCase
import com.fiction.domain.interactors.GetBalanceUseCase
import com.fiction.me.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UnlockNewEpisodeViewModel(
    private val getBalanceUseCase: GetBalanceUseCase,
    private val buyChapterUseCase: BuyChapterUseCase
) : BaseViewModel() {

    private val _userBalance = MutableStateFlow<Int?>(null)
    val userBalance = _userBalance.asStateFlow()

    private val _isUnlockChapter = MutableStateFlow<Boolean?>(null)
    val isuUnlockChapter = _isUnlockChapter.asStateFlow()

    fun getBalance() {
        viewModelScope.launch {
            when (val result = getBalanceUseCase()) {
                is ActionResult.Success -> {
                    _userBalance.emit(result.result)
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    fun buyChapter(chapterId: Long) {
        viewModelScope.launch {
            when (val result = buyChapterUseCase(chapterId)) {
                is ActionResult.Success -> {
                    _isUnlockChapter.emit(true)
                }
                is ActionResult.Error -> {
                    _isUnlockChapter.emit(false)
                }
            }
        }
    }
}