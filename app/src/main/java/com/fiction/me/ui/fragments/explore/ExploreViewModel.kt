package com.fiction.me.ui.fragments.explore

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.core.utils.Uuid
import com.fiction.domain.interactors.*
import com.fiction.domain.model.BaseExploreDataModel
import com.fiction.domain.model.StoryModel
import com.fiction.domain.model.SuggestedBooksModel
import com.fiction.domain.model.UpdateDevice
import com.fiction.domain.model.gift.GiftType
import com.fiction.domain.model.gift.WelcomeGift
import com.fiction.me.BuildConfig
import com.fiction.me.appbase.viewmodel.BaseViewModel
import com.fiction.me.utils.Events
import com.fiction.me.utils.Events.Companion.ADD_TO_LIBRARY_CLICKED
import com.fiction.me.utils.Events.Companion.ENTER_TYPE
import com.fiction.me.utils.Events.Companion.ENTER_TYPE_INITIAL
import com.fiction.me.utils.Events.Companion.EXPLORE
import com.fiction.me.utils.Events.Companion.EXPLORE_SCREEN
import com.fiction.me.utils.Events.Companion.EXPLORE_SCREEN_SHOWN
import com.fiction.me.utils.Events.Companion.FOR_YOU
import com.fiction.me.utils.Events.Companion.HOT_ROMANCE
import com.fiction.me.utils.Events.Companion.HOT_WEREWOLF
import com.fiction.me.utils.Events.Companion.INITIAL
import com.fiction.me.utils.Events.Companion.NEW_ARRIVAL
import com.fiction.me.utils.Events.Companion.PLACEMENT
import com.fiction.me.utils.Events.Companion.POPULAR
import com.fiction.me.utils.Events.Companion.RETURN
import com.fiction.me.utils.Events.Companion.SECTION
import com.fiction.me.utils.Events.Companion.SEE_ALL_CLICKED
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ExploreViewModel(
    private val exploreDataUseCase: ExploreDataUseCase,
    private val addBookToLibraryUseCase: AddBookToLibraryUseCase,
    private val removeBookFromLibraryUseCase: RemoveBookFromLibraryUseCase,
    private val getIsAppLaunchFirstTimeUseCase: GetIsAppLaunchFirstTimeUseCase,
    private val getIsExistGiftUseCase: GetIsExistGiftUseCase,
    private val getGiftUseCase: GetGiftUseCase,
    private val getPopularTagsUseCase: GetPopularTagsUseCase,
    private val getMostPopularBooksUseCase: GetMostPopularBooksUseCase,
    private val updateDeviceUseCase: UpdateDeviceUseCase,
    private val getAllGenresUseCase: GetAllGenresUseCase,
    private val getIsExploreFirstTimeUseCase: GetIsExploreFirstTimeUseCase,
    private val setIsExploreFirstTimeUseCase: SetIsExploreFirstTimeUseCase,
    private val uuid: Uuid,
) : BaseViewModel() {
    init {
        viewModelScope.launch {
            getPopularTagsUseCase()
            getMostPopularBooksUseCase()
            getAllGenresUseCase()
        }
        getIsExistGift()
        updateDevice()
    }

    private val _exploreDataList = MutableStateFlow<List<BaseExploreDataModel>?>(null)
    val exploreDataList = _exploreDataList.asStateFlow()

    private val _printMessageAddedLib = MutableSharedFlow<Unit?>()
    val printMessageAddedLib = _printMessageAddedLib.asSharedFlow()

    private val _printMessageRemoveLib = MutableSharedFlow<Unit?>()
    val printMessageRemoveLib = _printMessageRemoveLib.asSharedFlow()

    private val _giftWelcomeBox = MutableSharedFlow<WelcomeGift?>()
    val giftWelcomeBox = _giftWelcomeBox.asSharedFlow()

   // val earnedCoin = mutableStateOf(true)

    fun getExploreDataList() {
        viewModelScope.launch {
            when (val result = exploreDataUseCase()) {
                is ActionResult.Success -> {
                    _exploreDataList.emit(result.result)
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    private fun getIsExistGift() {
        viewModelScope.launch {
            when (val result = getIsExistGiftUseCase()) {
                is ActionResult.Success -> {
                    if (result.result) {
                        getGift()
                    }
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    private fun getGift() {
        viewModelScope.launch {
            when (val result = getGiftUseCase()) {
                is ActionResult.Success -> {
                    result.result.run {
                        if (type == GiftType.TYPE_WELCOME_BOX)
                            _giftWelcomeBox.emit(result.result)
                    }
                }
                is ActionResult.Error -> {
                    // Error Handling
                }
            }
        }
    }

    fun updateSuggestedBookItemData(
        dataModelId: String,
        bookItemId: Long,
        isAddedLibrary: Boolean,
        pos: Int,
        offset: Int,
    ) {
        val updatedList = exploreDataList.value?.toMutableList()

        if (!updatedList.isNullOrEmpty()) {
            viewModelScope.launch {
                if (isAddedLibrary) {
                    when (addBookToLibraryUseCase(bookItemId)) {
                        is ActionResult.Success -> {
                            updateExploreDataList(
                                dataModelId,
                                bookItemId,
                                isAddedLibrary,
                                pos,
                                offset,
                                updatedList
                            )
                        }
                        is ActionResult.Error -> {
                            // TODO() //Error Handling
                        }
                    }
                } else {
                    when (removeBookFromLibraryUseCase(bookItemId)) {
                        is ActionResult.Success -> {
                            updateExploreDataList(
                                dataModelId,
                                bookItemId,
                                isAddedLibrary,
                                pos,
                                offset,
                                updatedList
                            )
                        }
                        is ActionResult.Error -> {
                            // TODO() //Error Handling
                        }
                    }
                }
            }
        }
    }

    fun updateStoryItemData(
        dataModelId: String,
        storyItemId: Long,
        isAddedLibrary: Boolean,
        pos: Int,
        offset: Int,
    ) {
        val updatedList = exploreDataList.value?.toMutableList()
        if (!updatedList.isNullOrEmpty()) {
            viewModelScope.launch {
                if (isAddedLibrary) {
                    when (addBookToLibraryUseCase(storyItemId)) {
                        is ActionResult.Success -> {
                            updateStoryInExploreDataList(
                                dataModelId,
                                storyItemId,
                                isAddedLibrary,
                                pos,
                                offset,
                                updatedList
                            )
                        }
                        is ActionResult.Error -> {
                            // TODO() //Error Handling
                        }
                    }
                } else {
                    when (removeBookFromLibraryUseCase(storyItemId)) {
                        is ActionResult.Success -> {
                            updateStoryInExploreDataList(
                                dataModelId,
                                storyItemId,
                                isAddedLibrary,
                                pos,
                                offset,
                                updatedList
                            )
                        }
                        is ActionResult.Error -> {
                            // TODO() //Error Handling
                        }
                    }
                }
            }
        }
    }

    private fun updateExploreDataList(
        dataModelId: String,
        bookItemId: Long,
        isAddedLibrary: Boolean,
        pos: Int,
        offset: Int,
        updatedList: MutableList<BaseExploreDataModel>,
    ) {
        for (i in updatedList.indices) {
            if (updatedList[i].id == dataModelId) {
                val itemSuggestedBooks = updatedList[i] as SuggestedBooksModel
                val findItem = itemSuggestedBooks.booksList.find { it.id == bookItemId }
                val index =
                    if (findItem != null) itemSuggestedBooks.booksList.indexOf(findItem) else -1

                if (findItem != null && index != -1) {
                    val bookList = itemSuggestedBooks.booksList.toMutableList().apply {
                        this[index] = this[index].copy(isAddedLibrary = isAddedLibrary)
                    }
                    updatedList[i] = itemSuggestedBooks.copy(
                        booksList = bookList,
                        position = pos,
                        offset = offset
                    )
                }
                sendEventAddedLibrary(isAddedLibrary, itemSuggestedBooks.title)
                break
            }
        }
        viewModelScope.launch {
            if (isAddedLibrary)
                _printMessageAddedLib.emit(Unit)
            else _printMessageRemoveLib.emit(Unit)
            _exploreDataList.emit(updatedList)
        }
    }

    private fun updateStoryInExploreDataList(
        dataModelId: String,
        storyId: Long,
        isAddedLibrary: Boolean,
        pos: Int,
        offset: Int,
        updatedList: MutableList<BaseExploreDataModel>,
    ) {
        for (i in updatedList.indices) {
            if (updatedList[i].id == dataModelId) {
                val itemStory = updatedList[i] as StoryModel
                val findItem = itemStory.storyList.find { it.id == storyId }
                val index = if (findItem != null) itemStory.storyList.indexOf(findItem) else -1

                if (findItem != null && index != -1) {
                    val storyList = itemStory.storyList.toMutableList().apply {
                        this[index] = this[index].copy(isAddedLibrary = isAddedLibrary)
                    }

                    updatedList[i] = itemStory.copy(
                        storyList = storyList,
                        position = pos,
                        offset = offset
                    )
                }

                sendEventAddedLibrary(isAddedLibrary, itemStory.title)
                break
            }
        }
        viewModelScope.launch {
            if (isAddedLibrary)
                _printMessageAddedLib.emit(Unit)
            else _printMessageRemoveLib.emit(Unit)
            _exploreDataList.emit(updatedList)
        }
    }

    private fun updateDevice() {
        viewModelScope.launch {
            when (val result =
                updateDeviceUseCase(
                    updateDevice = UpdateDevice(
                        appVersion = BuildConfig.VERSION_NAME,
                        buildVersion = BuildConfig.VERSION_CODE.toString(), udid = uuid.getUuid()
                    )
                )) {
                is ActionResult.Success -> {
                    Log.i("FromExplore", "updateDevice")
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    fun sendEventForAppLaunch() {
        viewModelScope.launch {
            val isShow = getIsExploreFirstTimeUseCase().first()
            if (isShow == false) {
                trackEvents(
                    EXPLORE_SCREEN_SHOWN,
                    hashMapOf(ENTER_TYPE to INITIAL)
                )
                trackEvents(ENTER_TYPE_INITIAL)
                setIsExploreFirstTimeUseCase(true)
            } else trackEvents(
                EXPLORE_SCREEN_SHOWN, hashMapOf(
                    ENTER_TYPE to RETURN
                )
            )
        }
    }

    fun sendEventSeeAllClick(title: String) {
        trackEvents(
            SEE_ALL_CLICKED, hashMapOf(
                PLACEMENT to EXPLORE,
                SECTION to getSection(title)
            )
        )
    }

    fun sendEventToOpenBookSummary(section: String, id: Long?) {
        trackEvents(
            Events.BOOK_SUMMARY_SHOWN, hashMapOf(
                Events.REFERRER to EXPLORE,
                SECTION to getSection(section),
                Events.BOOK_ID_PROPERTY to (id ?: -1)
            )
        )
    }

    private fun sendEventAddedLibrary(isAddedLibrary: Boolean, title: String) {
        if (isAddedLibrary)
            trackEvents(
                ADD_TO_LIBRARY_CLICKED, hashMapOf(
                    PLACEMENT to EXPLORE_SCREEN,
                    SECTION to getSection(title)
                )
            )
    }

    fun getSection(title: String) =
        when (title) {
            "For you" -> FOR_YOU
            "Hot Werewolf" -> HOT_WEREWOLF
            "New Arrivals" -> NEW_ARRIVAL
            "Hot Romance" -> HOT_ROMANCE
            else -> POPULAR
        }
}
