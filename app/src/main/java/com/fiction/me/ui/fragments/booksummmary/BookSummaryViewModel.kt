package com.fiction.me.ui.fragments.booksummmary

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.domain.baseusecase.FlowPagingUseCase
import com.fiction.domain.interactors.AddBookToLibraryUseCase
import com.fiction.domain.interactors.BookSettingsGetUseCase
import com.fiction.domain.interactors.BuyChapterUseCase
import com.fiction.domain.interactors.GetAvailableTariffsUseCase
import com.fiction.domain.interactors.GetBalanceUseCase
import com.fiction.domain.interactors.GetBookInfoByIdUseCase
import com.fiction.domain.interactors.GetChapterInfoUseCase
import com.fiction.domain.interactors.GetPopularTagsUseCase
import com.fiction.domain.interactors.GetProfileInfoUseCase
import com.fiction.domain.interactors.RemoveBookFromLibraryUseCase
import com.fiction.domain.interactors.RemoveLikeUseCase
import com.fiction.domain.interactors.SetLikeUseCase
import com.fiction.domain.interactors.SetViewInBookUseCase
import com.fiction.domain.interactors.UpdateProfileUseCase
import com.fiction.domain.model.BookInfoAdapterModel
import com.fiction.domain.model.BookSummaryInfo
import com.fiction.domain.model.BooksDataModel
import com.fiction.domain.model.ChaptersDataModel
import com.fiction.domain.model.PopularTagsDataModel
import com.fiction.domain.model.enums.ColorThemeData
import com.fiction.me.appbase.viewmodel.BaseViewModel
import com.fiction.me.utils.Constants.Companion.LOW_BALANCE
import com.fiction.me.utils.Events
import com.fiction.me.utils.Events.Companion.ADD_TO_LIBRARY_CLICKED
import com.fiction.me.utils.Events.Companion.BOOK_ID_PROPERTY
import com.fiction.me.utils.Events.Companion.BOOK_SUMMARY
import com.fiction.me.utils.Events.Companion.PLACEMENT
import com.fiction.me.utils.Events.Companion.SECTION
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BookSummaryViewModel(
    private val addBookToLibraryUseCase: AddBookToLibraryUseCase,
    private val removeBookFromLibraryUseCase: RemoveBookFromLibraryUseCase,
    private val getBookInfoByIdUseCase: GetBookInfoByIdUseCase,
    private val getPopularTagsUseCase: GetPopularTagsUseCase,
    private val getProfileInfoUseCase: GetProfileInfoUseCase,
    private val setLikeUseCase: SetLikeUseCase,
    private val removeLikeUseCase: RemoveLikeUseCase,
    private val getChapterInfoUseCase: GetChapterInfoUseCase,
    private val getBalanceUseCase: GetBalanceUseCase,
    private val buyChapterUseCase: BuyChapterUseCase,
    private val getAvailableTariffsUseCase: GetAvailableTariffsUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val useCaseSettingsGet: BookSettingsGetUseCase,
    private val setViewInBookUseCase: SetViewInBookUseCase,
    private val getAllAlsoLikeBooksUseCase: FlowPagingUseCase<Long, BookInfoAdapterModel>
) : BaseViewModel() {

    init {
        getBookSettings()
    }

    private val _bookInfo = MutableStateFlow<BookSummaryInfo?>(null)
    val bookInfo = _bookInfo.asStateFlow()
    var bookSummaryInfo: BookSummaryInfo? = null

    private val _suggestedBooksList = MutableStateFlow<List<BooksDataModel>?>(null)
    val suggestedBooksList = _suggestedBooksList.asStateFlow()

    private val _alsoLikeBooksById = MutableStateFlow<Long?>(null)

    private val _popularTagsList = MutableStateFlow<List<PopularTagsDataModel>?>(null)
    val popularTagsList = _popularTagsList.asStateFlow()

    private val _readBook = MutableSharedFlow<Unit>()
    val readBook = _readBook.asSharedFlow()

    private val _likeErrorMessage = MutableSharedFlow<String>()
    val likeErrorMessage = _likeErrorMessage.asSharedFlow()

    private val _addBookErrorMessage = MutableSharedFlow<String>()
    val addBookErrorMessage = _addBookErrorMessage.asSharedFlow()

    private val _purchasedChapter = MutableSharedFlow<Long>()
    val purchasedChapter = _purchasedChapter.asSharedFlow()

    private val _unlockChapter = MutableSharedFlow<Long>()
    val unlockChapter = _unlockChapter.asSharedFlow()

    private val _buyCoins = MutableSharedFlow<Long?>()
    val buyCoins = _buyCoins.asSharedFlow()

    private val _isTurnOnAutoUnlock = MutableSharedFlow<Boolean?>()
    val isTurnOnAutoUnlock = _isTurnOnAutoUnlock.asSharedFlow()

    private val _updateInfo = MutableSharedFlow<BookSummaryInfo?>()
    val updateInfo = _updateInfo.asSharedFlow()

    private val _dismissDialog = MutableSharedFlow<Unit?>()
    val dismissDialog = _dismissDialog.asSharedFlow()

    val progressState = mutableStateOf(false)
    var isShowProgressBar = mutableStateOf(false)

    var isTeenager = false
    var openCoinShop = false
    var isAddedToLib = false
    var isBookLiked = false
    private var isAutoUnlock = false
    var theme: ColorThemeData = ColorThemeData.BLACK
    var bookSummaryBookId = 0L

    var myBalance = 0
    var isBalanceEnough = false
    var isTurnOnUnlock = false
    var isChangedUnlockState = false
    var nextReadChapterId = 0L
    var isUnlockBottomDialogShown = mutableStateOf(false)

    val alsoLikeBooks = _alsoLikeBooksById
        .map { getAllAlsoLikeBooksUseCase(_alsoLikeBooksById.value ?: 0L) }
        .flatMapLatest { it }
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
        .cachedIn(viewModelScope)

    fun getAlsoLikeBooks(bookId: Long) {
        viewModelScope.launch {
            bookSummaryBookId = bookId
            _alsoLikeBooksById.emit(bookId)
        }
    }

    fun hideUnlockDialog() {
        if (isUnlockBottomDialogShown.value)
            isUnlockBottomDialogShown.value = false
    }

    fun showUnlockDialog() {
        isUnlockBottomDialogShown.value = true
    }

    var unlockSwitchState = mutableStateOf(isTurnOnUnlock)

    var isDialogShown = mutableStateOf(false)
        private set

    fun onAutoUnlockStateChanged() {
        isDialogShown.value = true
    }

    fun onDismissDialog() {
        isDialogShown.value = false
    }

    fun getChapterOrder(chapterId: Long): Int {
        return bookSummaryInfo?.bookData?.chapters?.find { it.id == chapterId }?.order ?: -1
    }

    fun setBookSummaryId(id: Long) {
        if (bookSummaryBookId == 0L)
            bookSummaryBookId = id
    }

    fun readingStatus() =
        bookSummaryInfo?.let {
            it.bookData.chapters.isNotEmpty() && it.bookData.chapters[0].id != it.readingProgress.chapterId || it.readingProgress.percent != 0.0
        }

    fun getIsBookCompleted() = bookSummaryInfo?.bookData?.isCompleted ?: true

    var likeCount = 0
        set(value) {
            if (value >= 0)
                field = value
        }

    fun getInfoById(isNotFirstCall: Boolean = true) {
        viewModelScope.launch {
            when (val result = getBookInfoByIdUseCase(bookSummaryBookId)) {
                is ActionResult.Success -> {
                    setViewInBook(bookSummaryBookId)
                    result.result.bookData.run {
                        bookSummaryBookId = id
                        isAddedToLib = isAddedToLibrary
                        isBookLiked = isLiked
                    }
                    bookSummaryInfo = result.result
                    likeCount = bookSummaryInfo?.bookData?.likes ?: 0
                    _updateInfo.emit(bookSummaryInfo)
                    if (isNotFirstCall) _bookInfo.emit(result.result)
                }

                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

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

    /*    fun getSuggestedBooksList() {
            viewModelScope.launch {
                when (val result = getAlsoLikeBooks(bookSummaryBookId)) {
                    is ActionResult.Success -> {
                        _suggestedBooksList.emit(result.result)
                    }
                    is ActionResult.Error -> {
                        // TODO : Handler
                    }
                }
            }
        }*/

    fun getPopularTagsList() {
        viewModelScope.launch {
            when (val result = getPopularTagsUseCase()) {
                is ActionResult.Success -> {
                    _popularTagsList.emit(result.result)
                }

                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    fun getReadingMode() {
        viewModelScope.launch {
            when (val result = getProfileInfoUseCase()) {
                is ActionResult.Success -> {
                    isTeenager = !result.result.setting.readingMode
                }

                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    fun updateSuggestedBooksList(bookId: Long, isAddedLibrary: Boolean, section: String) {
        viewModelScope.launch {
            if (isAddedLibrary) {
                when (addBookToLibraryUseCase(bookId)) {
                    is ActionResult.Success -> {
                        updateLibraryBooksList(
                            bookId,
                            isAddedLibrary,
                            section
                        )
                    }

                    is ActionResult.Error -> {
                        // TODO() //Error Handling
                    }
                }
            } else {
                when (removeBookFromLibraryUseCase(bookId)) {
                    is ActionResult.Success -> {
                        updateLibraryBooksList(
                            bookId,
                            isAddedLibrary,
                            section
                        )
                    }

                    is ActionResult.Error -> {
                        // TODO() //Error Handling
                    }
                }
            }
        }
    }

    fun startReadBook() {
        viewModelScope.launch {
            delay(1000)
            _readBook.emit(Unit)
        }
    }

    fun changeBookLikeState() {
        viewModelScope.launch {
            if (isBookLiked)
                removeLike()
            else addLike()
            isBookLiked = !isBookLiked
        }
    }

    fun getChapterInfo(chapterId: Long) {
        viewModelScope.launch {
            when (val result =
                bookSummaryInfo?.bookData?.id?.let { getChapterInfoUseCase(it, chapterId) }) {
                is ActionResult.Success -> {
                    result.result.run {
                        if (this.first.isPurchased) {
                            if (progressState.value)
                                progressState.value = false
                            _dismissDialog.emit(Unit)
                            _purchasedChapter.emit(chapterId)
                        }
                    }
                }

                is ActionResult.Error -> {
                    val errorMessage = (result.errors as? CallException)?.errorMessage
                    if (errorMessage?.isNotEmpty() == true && (errorMessage[0].substringAfter('+')) == LOW_BALANCE) {
                        getPaidChapter(chapterId)
                    }
                }

                else -> {}
            }
        }
    }

    fun cacheTariffs() {
        viewModelScope.launch {
            getAvailableTariffsUseCase()
        }
    }

    private fun getPaidChapter(chapterId: Long) {
        viewModelScope.launch {
            bookSummaryInfo?.bookData?.chapters?.forEach {
                if (it.id == chapterId && !it.isFree) {
                    nextReadChapterId = it.id
                    getBalance(it)
                    return@launch
                }
            }
        }
    }

    private fun getBalance(chapterInfo: ChaptersDataModel) {
        viewModelScope.launch {
            when (val result = getBalanceUseCase()) {
                is ActionResult.Success -> {
                    result.result?.let { balance ->
                        myBalance = balance
                        isBalanceEnough = myBalance > chapterInfo.coins
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

    private fun addLike() {
        viewModelScope.launch {
            val bookId = bookSummaryInfo?.bookData?.id
                ?: if (bookSummaryBookId != 0L) bookSummaryBookId else null
            bookId?.let {
                when (val result = setLikeUseCase(bookId, likeCount)) {
                    is ActionResult.Success -> {

                    }

                    is ActionResult.Error -> {
                        _likeErrorMessage.emit(messageForError(result.errors))
                    }
                }
            }
        }
    }

    private fun removeLike() {
        viewModelScope.launch {
            val bookId = bookSummaryInfo?.bookData?.id
                ?: if (bookSummaryBookId != 0L) bookSummaryBookId else null
            bookId?.let {
                when (val result = removeLikeUseCase(bookId, likeCount)) {
                    is ActionResult.Success -> {
                        //likeCount--
                    }

                    is ActionResult.Error -> {
                        _likeErrorMessage.emit(messageForError(result.errors))
                    }
                }
            }
        }
    }

    fun addOrRemoveBook(bookId: Long, section: String) {
        isAddedToLib = !isAddedToLib
        viewModelScope.launch {
            when (val result =
                if (isAddedToLib) addBookToLibraryUseCase(
                    bookId
                ) else removeBookFromLibraryUseCase(bookId)) {
                is ActionResult.Success -> {
                    if (isAddedToLib) {
                        sendEventAddedLibrary(section)
                    }
                }
                is ActionResult.Error -> {
                    _addBookErrorMessage.emit(messageForError(result.errors))
                }
            }
        }
    }

    private fun messageForError(errors: Throwable): String {
        val errorMessage = (errors as? CallException)?.errorMessage
        var errorText = ""
        errorMessage?.forEach {
            errorText = it.substringAfter('+')
        }
        return errorText
    }

    fun getChapterCost(chapterId: Long): Int {
        bookSummaryInfo?.bookData?.chapters?.forEach {
            if (it.id == chapterId) {
                return it.coins
            }
        }
        return 0
    }

    private fun updateLibraryBooksList(
        bookId: Long,
        isAddedLibrary: Boolean,
        section: String
    ) {
        var booksList = suggestedBooksList.value
        val findItem = booksList?.find { it.id == bookId }
        val index = if (findItem != null) booksList?.indexOf(findItem) else -1

        if (isAddedLibrary) sendEventAddedLibrary(section)

        if (index != -1 && index != null) {
            booksList = booksList?.toMutableList().apply {
                this?.set(index, this[index].copy(isAddedLibrary = isAddedLibrary))
            }
        }
        viewModelScope.launch {
            _suggestedBooksList.emit(booksList)
            /* if (isAddedLibrary) {
                 _addBookToLib.emit(bookId)
             } else _removeBookFromLib.emit(bookId)*/
        }
    }

    private fun sendEventAddedLibrary(section: String) {
        val eventMap = hashMapOf<String, Any>(
            PLACEMENT to BOOK_SUMMARY,
            BOOK_ID_PROPERTY to (bookSummaryInfo?.bookData?.id ?: -1)
        )
        if (section.isNotEmpty()){
            eventMap[SECTION] = section
        }
        trackEvents(
            ADD_TO_LIBRARY_CLICKED, eventMap
        )
    }

    private fun buyChapter(chapterId: Long) {
        viewModelScope.launch {
            when (val result = buyChapterUseCase(chapterId)) {
                is ActionResult.Success -> {
                    val coinSpendType = if (isAutoUnlock) Events.AUTOMATIC else Events.CHAPTER
                    trackEvents(
                        Events.COINS_SPEND,
                        hashMapOf(
                            Events.REFERRER to Events.READING_SCREEN,
                            Events.BOOK_ID_PROPERTY to (bookSummaryInfo?.bookData?.id ?: -1),
                            Events.COINS to getChapterCost(chapterId),
                            Events.COINS_SPEND_TYPE to coinSpendType,
                        )
                    )
                    getChapterInfo(chapterId)
                }

                is ActionResult.Error -> {

                }
            }
        }
    }

    private fun getLockState(isFromCoinShop: Boolean = false) {
        viewModelScope.launch {
            when (val result = getProfileInfoUseCase()) {
                is ActionResult.Success -> {
                    isTurnOnUnlock = result.result.setting.autoUnlockPaid
                    if (isTurnOnUnlock && isBalanceEnough)
                        buyChapter(nextReadChapterId)
                    else {
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
    /*
            private fun getLockState(chapterId: Long) {
                viewModelScope.launch {
                    when (val result = getProfileInfoUseCase()) {
                        is ActionResult.Success -> {
                            isAutoUnlock = result.result.setting.autoUnlockPaid
                            if (isAutoUnlock)
                                buyChapter(chapterId)
                            else _unlockChapter.emit(chapterId)
                        }
                        is ActionResult.Error -> {
                            // TODO() //Error Handling
                        }
                    }
                }
          }*/

    private fun getBookSettings() {
        viewModelScope.launch {
            val result = useCaseSettingsGet()
            theme = result.colorTheme
        }
    }
}