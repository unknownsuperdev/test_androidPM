package com.fiction.me.ui.fragments.onboarding

import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.domain.interactors.ExploreDataUseCase
import com.fiction.domain.interactors.GetBooksForYouUseCase
import com.fiction.domain.model.onboarding.BooksForYou
import com.fiction.me.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnalyzeNovelViewModel(
    private val getBooksForYouUseCase: GetBooksForYouUseCase,
    private val getExploreDataUseCase: ExploreDataUseCase
) : BaseViewModel() {

    init {
        cacheBooksForYou()
        cacheExploreInfo()
    }

    var isCacheData = false

    private val _bookForYou = MutableStateFlow<List<BooksForYou>?>(null)
    val bookForYou = _bookForYou.asStateFlow()

    private fun cacheBooksForYou() {
        viewModelScope.launch {
            when (val result = getBooksForYouUseCase()) {
                is ActionResult.Success -> {
                    _bookForYou.emit(result.result)
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    private  fun cacheExploreInfo(){
       CoroutineScope(Dispatchers.IO).launch {
           getExploreDataUseCase()
       }
    }
}