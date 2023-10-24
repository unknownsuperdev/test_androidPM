package com.fiction.me.ui.fragments.reader

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.domain.interactors.AddBookToLibraryUseCase
import com.fiction.domain.interactors.BookSettingsGetUseCase
import com.fiction.domain.interactors.BookSettingsInsertUseCase
import com.fiction.domain.interactors.BuyChapterUseCase
import com.fiction.domain.interactors.BuyTariffUseCase
import com.fiction.domain.interactors.GetAvailableTariffsUseCase
import com.fiction.domain.interactors.GetBalanceUseCase
import com.fiction.domain.interactors.GetBookChaptersUseCase
import com.fiction.domain.interactors.GetBookInfoByIdUseCase
import com.fiction.domain.interactors.GetChapterInfoUseCase
import com.fiction.domain.interactors.GetCurrentReadingBooksUseCase
import com.fiction.domain.interactors.GetProfileInfoUseCase
import com.fiction.domain.interactors.GetSuggestionBooksUseCase
import com.fiction.domain.interactors.InsertBookUseCase
import com.fiction.domain.interactors.RemoveBookFromLibraryUseCase
import com.fiction.domain.interactors.SendReaderAnalyticsUseCase
import com.fiction.domain.interactors.SetReadProgressUseCase
import com.fiction.domain.interactors.SetViewInBookUseCase
import com.fiction.domain.interactors.UpdateProfileUseCase
import com.fiction.domain.model.BookSettingsData
import com.fiction.domain.model.BookSummaryInfo
import com.fiction.domain.model.ChapterInfo
import com.fiction.domain.model.bookroom.Book
import com.fiction.me.appbase.viewmodel.BaseViewModel
import com.fiction.me.utils.Constants.Companion.LOW_BALANCE
import com.fiction.me.utils.Events
import com.fiction.me.utils.Events.Companion.AUTOMATIC
import com.fiction.me.utils.Events.Companion.BOOK_ID_PROPERTY
import com.fiction.me.utils.Events.Companion.CHAPTER
import com.fiction.me.utils.Events.Companion.COINS
import com.fiction.me.utils.Events.Companion.COINS_SPEND
import com.fiction.me.utils.Events.Companion.COINS_SPEND_TYPE
import com.fiction.me.utils.Events.Companion.READING_SCREEN
import com.fiction.me.utils.Events.Companion.REFERRER
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReaderViewModel(
    private val useCaseSettingsGet: BookSettingsGetUseCase,
    private val useCaseSettingsInsert: BookSettingsInsertUseCase,
    private val getChapterInfoUseCase: GetChapterInfoUseCase,
    private val getBookInfoByIdUseCase: GetBookInfoByIdUseCase,
    private val addBookToLibraryUseCase: AddBookToLibraryUseCase,
    private val removeBookFromLibraryUseCase: RemoveBookFromLibraryUseCase,
    private val setReadProgressUseCase: SetReadProgressUseCase,
    private val insertBookUseCase: InsertBookUseCase,
    private val getBalanceUseCase: GetBalanceUseCase,
    private val getProfileInfoUseCase: GetProfileInfoUseCase,
    private val buyTariffUseCase: BuyTariffUseCase,
    private val buyChapterUseCase: BuyChapterUseCase,
    private val sendReaderAnalyticsUseCase: SendReaderAnalyticsUseCase,
    private val getCurrentReadingBooksUseCase: GetCurrentReadingBooksUseCase,
    private val getSuggestionBooksUseCase: GetSuggestionBooksUseCase,
    private val getBookChaptersUseCase: GetBookChaptersUseCase,
    private val getAvailableTariffsUseCase: GetAvailableTariffsUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val setViewInBookUseCase: SetViewInBookUseCase
) : BaseViewModel() {

    private val _timer = MutableSharedFlow<Int>()
    val timer = _timer.asSharedFlow()
    private var currentPagePosition = 0

    private val _bookSettings = MutableSharedFlow<BookSettingsData?>()
    val bookSettings = _bookSettings.asSharedFlow()
    var bookToolsSetting: BookSettingsData? = null

    private val _chapterInfo = MutableStateFlow<ChapterInfo?>(null)
    val chapterInfo = _chapterInfo.asStateFlow()

    private val _isNextBtnClicked = MutableStateFlow<Boolean?>(null)
    val isNextBtnClicked = _isNextBtnClicked.asStateFlow()

    private val _bookInfo = MutableSharedFlow<BookSummaryInfo?>()
    val bookInfo = _bookInfo.asSharedFlow()

    private val _addBookToLib = MutableSharedFlow<Unit>()
    val addBookToLib = _addBookToLib.asSharedFlow()

    private val _removeBookFromLib = MutableSharedFlow<Unit>()
    val removeBookFromLib = _removeBookFromLib.asSharedFlow()

    private val _unlockChapter = MutableSharedFlow<Long>()
    val unlockChapter = _unlockChapter.asSharedFlow()

    private val _buyCoins = MutableSharedFlow<Long?>()
    val buyCoins = _buyCoins.asSharedFlow()

    private val _dismissDialog = MutableSharedFlow<Unit?>()
    val dismissDialog = _dismissDialog.asSharedFlow()

    var isPagesFirstInit = true

    private val _progressBarVisibility = MutableSharedFlow<Unit?>()
    val progressBarVisibility = _progressBarVisibility.asSharedFlow()

    private val _isTurnOnAutoUnlock = MutableSharedFlow<Boolean>()
    val isTurnOnAutoUnlock = _isTurnOnAutoUnlock.asSharedFlow()
    val progressState = mutableStateOf(false)
    var bookSummaryInfo: BookSummaryInfo? = null
    var myBalance = 0
    var isBalanceEnough = false
    var isTurnOnUnlock = false
    var isChangedUnlockState = false
    var openCoinShop = false
    var isDialogShown = mutableStateOf(false)
        private set

    var isUnlockBottomDialogShown = mutableStateOf(false)

    var unlockSwitchState = mutableStateOf(isTurnOnUnlock)
    var isShowProgressBar = mutableStateOf(false)

    var settingsUpdated = false

    var isAddedToLib = false
    var isNextChapterLocked = false
    var chapterTitle: String = ""
    var chapterContext: String = ""
    var currentReadChapterId = 0L
    var nextReadChapterId = 0L
    var currentReadBookId = 0L
    var duration = 0L
    var readPercent = 0.0
    var bookName = ""
    var isUpdateCurrentBooks = false
    private var isAutoUnlock = false
    var debounceJob: Job? = null
    var ticker: Int = 0
        set(value) {
            field = value
            debounceJob?.cancel()
            debounceJob = viewModelScope.launch {
                while (field > 0) {
                    delay(1000)
                    field--
                }
                _timer.emit(0)
            }
        }

    init {
        viewModelScope.launch {
            val result = useCaseSettingsGet()
            bookToolsSetting = result
            _bookSettings.emit(result)
        }
    }

    fun hideUnlockDialog() {
        if (isUnlockBottomDialogShown.value)
            isUnlockBottomDialogShown.value = false
    }

    fun showUnlockDialog() {
        if (!isUnlockBottomDialogShown.value)
            isUnlockBottomDialogShown.value = true
    }

    fun onAutoUnlockStateChanged() {
        isDialogShown.value = true
    }

    fun onDismissDialog() {
        isDialogShown.value = false
    }



    fun hideProgressBar(){
        if (isPagesFirstInit) return
        viewModelScope.launch {
            delay(1000)
            _progressBarVisibility.emit(Unit)
        }
    }

    fun readingStatus() =
        bookSummaryInfo?.let {
            it.bookData.chapters.isNotEmpty() && it.bookData.chapters[0].id != it.readingProgress.chapterId || it.readingProgress.percent != 0.0
        }

    fun getChapterOrder(): Long {
        return _chapterInfo.value?.order?.toLong() ?: nextReadChapterId
    }

    fun getIsBookCompleted() = bookSummaryInfo?.bookData?.isCompleted ?: true

    fun getBookSettings() {
        viewModelScope.launch {
            val result = useCaseSettingsGet()
            _bookSettings.emit(result)
        }
    }

    fun setClickState(isClick: Boolean) {
        viewModelScope.launch {
            Log.i("setMoveState", "processTransition: $isClick")
            _isNextBtnClicked.emit(isClick)
        }
    }

    fun getInfoById(bookId: Long, chapterId: Long = -1L) {
        viewModelScope.launch {
            when (val result = getBookInfoByIdUseCase(bookId)) {
                is ActionResult.Success -> {
                    isAddedToLib = result.result.bookData.isAddedToLibrary
                    currentReadChapterId =
                        if (chapterId == -1L) result.result.readingProgress.chapterId else chapterId
                    currentReadBookId = bookId
                    getRetentionBooks()
                    bookSummaryInfo = result.result
                    bookName = result.result.bookInfo.title

                    Log.i("GetPurchasedCoin", "onEach: Success ${bookSummaryInfo}")
                    _bookInfo.emit(bookSummaryInfo)
                    getBookChaptersUseCase(currentReadBookId)
                }

                is ActionResult.Error -> {
                    Log.i("GetPurchasedCoin", "onEach:Error ${result.errors.message}")

                }
            }
        }
    }

    fun insertBookInDB() {
        viewModelScope.launch {
            insertBookUseCase(Book(currentReadBookId, currentPagePosition, currentReadChapterId))
        }
    }

    fun addOrRemoveBook() {
        viewModelScope.launch {
            if (isAddedToLib) {
                when (removeBookFromLibraryUseCase(currentReadBookId)) {
                    is ActionResult.Success -> {
                        isAddedToLib = false
                        _removeBookFromLib.emit(Unit)
                    }

                    is ActionResult.Error -> {
                        // TODO() //Error Handling
                    }
                }
            } else {
                when (addBookToLibraryUseCase(currentReadBookId)) {
                    is ActionResult.Success -> {
                        isAddedToLib = true
                        trackEvents(
                            Events.ADD_TO_LIBRARY_CLICKED, hashMapOf(
                                Events.PLACEMENT to Events.READING_SCREEN,
                                BOOK_ID_PROPERTY to (bookSummaryInfo?.bookData?.id ?: -1)
                                /* SECTION to getSection(title)*/ //TODO()
                            )
                        )
                        _addBookToLib.emit(Unit)
                    }

                    is ActionResult.Error -> {
                        //TODO() // Error handling
                    }
                }
            }
        }
    }

    fun setViewInBook(bookId: Long) {
        viewModelScope.launch {
            progressState.value = true
            when (setViewInBookUseCase(bookId)) {
                is ActionResult.Success -> {
                    progressState.value = false
                    Log.i("setViewInBook", "Success")
                }

                is ActionResult.Error -> {
                    progressState.value = false
                }
            }
        }
    }

    fun setReadProgress() {
        viewModelScope.launch {
            val percent = if (isNextChapterLocked) 100.0 else readPercent
            when (val result = setReadProgressUseCase(currentReadChapterId, percent)) {
                is ActionResult.Success -> {
                    println("Success")
                }

                is ActionResult.Error -> {

                }
            }
        }
    }

    fun changeBookSettings(bookSettingsData: BookSettingsData?) {
        viewModelScope.launch {
            bookSettingsData?.let {
                useCaseSettingsInsert(it)
            }
            settingsUpdated = true
            bookToolsSetting = bookSettingsData
            _bookSettings.emit(bookSettingsData)
        }
    }

    fun countPercentageOfReadPages(countOfReadPage: Int, bookPageCount: Int): String {
        readPercent = (countOfReadPage * 100 / bookPageCount.toDouble())
        return "${readPercent.toInt()}%"
    }


    fun setCurrentPagePosition(position: Int) {
        currentPagePosition = position
    }

    fun getCurrentPagePosition() = currentPagePosition

    fun getChapterInfo(chapterId: Long = -1L, isNextChapter: Boolean = false) {
        nextReadChapterId = if (chapterId != -1L) chapterId else currentReadChapterId

        viewModelScope.launch {
            when (val result = getChapterInfoUseCase(currentReadBookId, nextReadChapterId)) {
                is ActionResult.Success -> {
                    result.result.run {
                        if (this.first.isPurchased) {
                            chapterTitle = this.first.title
                            chapterContext = this.first.text
                            currentPagePosition = if (isNextChapter) 0 else this.second
                            _chapterInfo.emit(this.first)
                            currentReadChapterId = nextReadChapterId
                            if (progressState.value)
                                progressState.value = false
                            _dismissDialog.emit(Unit)
                            getBookChaptersUseCase(currentReadBookId, currentReadChapterId)
                        } else getPaidChapter()
                    }
                }

                is ActionResult.Error -> {
                    val errorMessage = (result.errors as? CallException)?.errorMessage
                    if (errorMessage?.isNotEmpty() == true && (errorMessage[0].substringAfter('+')) == LOW_BALANCE) {
                        getPaidChapter()
                        isNextChapterLocked = true
                        insertBookInDB()
                    } else _dismissDialog.emit(Unit)
                }
            }
        }
    }

    fun isLastChapter() = bookSummaryInfo?.bookData?.chapters?.last()?.id == nextReadChapterId

    fun cacheTariffs() {
        viewModelScope.launch {
            getAvailableTariffsUseCase()
        }
    }

    fun getPaidChapter() {
        viewModelScope.launch {
            bookSummaryInfo?.bookData?.chapters?.forEach {
                if (it.id == nextReadChapterId && !it.isFree) {
                    getBalance(it.coins)
                    return@launch
                }
            }
        }
    }

    fun changeAutoUnlockMode(isTurnOnAutoUnlock: Boolean) {
        viewModelScope.launch {
            when (updateProfileUseCase(autoUnlockPaid = isTurnOnAutoUnlock)) {
                is ActionResult.Success -> {
                    isTurnOnUnlock = isTurnOnAutoUnlock
                    isChangedUnlockState = true
                }

                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    private fun getRetentionBooks() {
        viewModelScope.launch {
            getSuggestionBooksUseCase(currentReadBookId)
        }
    }

    fun openNextChapter() {
        val currentChapterIndex = bookSummaryInfo?.bookData?.chapters?.indexOfFirst {
            it.id == currentReadChapterId
        } ?: -1
        val nextChapterIndex = currentChapterIndex + 1
        val chaptersSize = bookSummaryInfo?.bookData?.chapters?.size ?: 0
        if (nextChapterIndex in 1 until chaptersSize) {
            val nextChapterId = bookSummaryInfo?.bookData?.chapters?.get(nextChapterIndex)?.id
            nextChapterId?.let { getChapterInfo(it, true) }
        }
    }

    fun setChapterLockState(isChapterLocked: Boolean) {
        if (!isNextChapterLocked)
            isNextChapterLocked = isChapterLocked
    }

    private fun getBalance(chapterCoins: Int) {
        viewModelScope.launch {
            when (val result = getBalanceUseCase()) {
                is ActionResult.Success -> {
                    result.result?.let { balance ->
                        myBalance = balance
                        isBalanceEnough = myBalance > chapterCoins
                        getLockState()
                        //checkBalance(chapterInfo)
                    }
                }

                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    fun buyChapterOrCoin() {
        if (isBalanceEnough)
            buyChapter(nextReadChapterId)
        else viewModelScope.launch {
            _buyCoins.emit(nextReadChapterId)
        }
    }

    fun isChapterFree(): Boolean {
        bookSummaryInfo?.bookData?.chapters?.forEach {
            if (it.id == currentReadChapterId) {
                return it.isFree
            }
        }
        return false
    }

    private fun getLockState(isFromCoinShop: Boolean = false) {
        viewModelScope.launch {
            when (val result = getProfileInfoUseCase()) {
                is ActionResult.Success -> {
                    isTurnOnUnlock = result.result.setting.autoUnlockPaid
                    if (isTurnOnUnlock && isBalanceEnough) {
                        buyChapter(nextReadChapterId)
                        Log.i("GetPurchasedCoin", "buyChapter: ${result.result}")
                    } else {
                        Log.i("GetPurchasedCoin", "buyChapter: ${result.result}")

                        if (isFromCoinShop)
                            _isTurnOnAutoUnlock.emit(isTurnOnUnlock)
                        else _unlockChapter.emit(nextReadChapterId)
                        if (progressState.value)
                            progressState.value = false
                        _dismissDialog.emit(Unit)
                    }
                }

                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    fun getChapterCost(chapterId: Long): Int {
        bookSummaryInfo?.bookData?.chapters?.forEach {
            if (it.id == chapterId) {
                return it.coins
            }
        }
        return 0
    }

    private fun buyChapter(chapterId: Long) {
        viewModelScope.launch {
            when (val result = buyChapterUseCase(chapterId)) {
                is ActionResult.Success -> {
                    val coinSpendType = if (isAutoUnlock) AUTOMATIC else CHAPTER
                    trackEvents(
                        COINS_SPEND,
                        hashMapOf(
                            REFERRER to READING_SCREEN,
                            BOOK_ID_PROPERTY to currentReadBookId,
                            COINS to getChapterCost(chapterId),
                            COINS_SPEND_TYPE to coinSpendType,
                        )
                    )
                    getChapterInfo(chapterId)
                }

                is ActionResult.Error -> {

                }
            }
        }
    }

    fun sendAnalytics(bookId: Long, sessionId: String, udid: String) {
        viewModelScope.launch {
            when (val result = sendReaderAnalyticsUseCase(bookId, sessionId, udid)) {
                is ActionResult.Success -> {
                    Log.i("analytics", "sendAnalytics: every 5 minutes")
                }

                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    fun updateLibCurrentReadBooks(updateCurrentBooks: Boolean) {
        if (!isUpdateCurrentBooks && updateCurrentBooks) {
            viewModelScope.launch {
                delay(2000)
                getCurrentReadingBooksUseCase(isMakeCallAnyway = true)
                isUpdateCurrentBooks = updateCurrentBooks
            }
        }
    }
}