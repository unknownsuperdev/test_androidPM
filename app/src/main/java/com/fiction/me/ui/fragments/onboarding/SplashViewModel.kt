package com.fiction.me.ui.fragments.onboarding

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.analytics.api.Events
import com.analytics.api.Events.USER_PROPERTIES__USER_UUID
import com.analytics.api.Events.USER_PROPERTY_STATUS_NOT_REGISTERED
import com.analytics.api.Events.USER_PROPERTY__THEME_MODE_DARK
import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.utils.Uuid
import com.fiction.domain.interactors.*
import com.fiction.domain.repository.AppInfoRepository
import com.fiction.me.BuildConfig
import com.fiction.me.appbase.viewmodel.BaseViewModel
import com.fiction.me.ui.analytics.Analytics
import com.fiction.me.utils.Constants.Companion.NETWORK_LOST
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class SplashViewModel(
    private val getOnBoardingScreensUseCase: GetOnBoardingScreensUseCase,
    private val setIsAppLaunchFirstTimeUseCase: SetIsAppLaunchFirstTimeUseCase,
    private val getIsAppLaunchFirstTimeUseCase: GetIsAppLaunchFirstTimeUseCase,
    private val getExploreDataUseCase: ExploreDataUseCase,
    private val getProfileInfoUseCase: GetProfileInfoUseCase,
    private val getCurrentReadingBooksUseCase: GetCurrentReadingBooksUseCase,
    private val insertLaunchInfoFromDBUseCase: InsertLaunchInfoFromDBUseCase,
    private val getUserTokenFromDatastoreUseCase: GetUserTokenFromDatastoreUseCase,
    private val getGuestUseCase: GuestUseCase,
    private val getLaunchInfoFromDBUseCase: GetLaunchInfoFromDBUseCase,
    private val uuid: Uuid,
    private val appInfoRepository: AppInfoRepository
) : BaseViewModel() {

    init {
        viewModelScope.launch {
            val token = getUserTokenFromDatastoreUseCase()
            if (token?.isEmpty() == true) {
                getGuestToken()
            } else getCache()
        }
        getIsAppLaunchFirst()
    }

    private var screensList: List<String>? = listOf("splash")

    private val _screens = MutableStateFlow<List<String>?>(null)
    val screens = _screens.asStateFlow()

    fun installSendEvent() {
        viewModelScope.launch {
            setUserPropertyEvent(
                mapOf(
                    Events.USER_PROPERTY__COHORT_DAY to appInfoRepository.getCohortDay(),
                    Events.USER_PROPERTY__COHORT_WEEK to appInfoRepository.getCohortWeek(),
                    Events.USER_PROPERTY__COHORT_MONTH to appInfoRepository.getCohortMonth(),
                    Events.USER_PROPERTY__COHORT_YEAR to appInfoRepository.getCohortYear(),
                    Events.USER_PROPERTIES__START_APP_VERSION to appInfoRepository.getStartAppVersion(),

                    Events.USER_PROPERTIES__APP_VERSION to BuildConfig.VERSION_NAME,
                    Events.USER_PROPERTIES__DEVICE_UUID to uuid.getUuid(),

                    Events.USER_PROPERTY__STATUS to USER_PROPERTY_STATUS_NOT_REGISTERED,
                    Events.USER_PROPERTY__THEME_MODE to USER_PROPERTY__THEME_MODE_DARK,
                )
            )
        }
    }

    private fun getGuestToken() {
        viewModelScope.launch {
            when (val result = getGuestUseCase(uuid.getUuid())) {
                is ActionResult.Success -> {
                    getProfileInfo()
                    getCache()
                }
                is ActionResult.Error -> {
                    if ((result.errors as? CallException)?.errorCode == NETWORK_LOST) {
                        delay(500)
                        getGuestToken()
                    }
                }
            }
        }
    }

    private fun getCache() {
     viewModelScope.launch {
            if (getLaunchInfoFromDBUseCase() == false) {
                val startTime = System.currentTimeMillis()
                Log.i("asyncTimeTest", "getCache: ${startTime}")
                val asyncCalls = listOf(
                    async { getCurrentReadingBooks() },
                    async { getExploreData() },
                    async { getOnBoardingScreens() },
                    async { getProfileInfo()}
                )
                asyncCalls.awaitAll()
                Log.i("asyncTimeTest", "getCache: ${System.currentTimeMillis() - startTime}  ,endTime = ${System.currentTimeMillis()}")
                Log.i("screensList", "getCache: $screensList")
                _screens.emit(screensList)
            } else {
                 getAllData()
            }
        }
    }

    private suspend fun getOnBoardingScreens() {
        when (val result = getOnBoardingScreensUseCase()) {
            is ActionResult.Success -> {
                screensList = result.result
                Log.i("screensList", "getOnBoardingScreens: $screensList")
            }
            is ActionResult.Error -> {
                if ((result.errors as? CallException)?.errorCode == NETWORK_LOST) {
                    delay(500)
                    Log.i("screensList", "getOnBoardingScreens: ${(result.errors as? CallException)?.errorMessage}")
                    getOnBoardingScreens()
                }
            }
        }
    }

    private suspend fun getExploreData() {
        Log.i("getExploreData", "getExploreData: out")
        when (val result = getExploreDataUseCase()) {
            is ActionResult.Success -> {
                Log.i("screensList", "getExploreData: Success")
            }
            is ActionResult.Error -> {
                Log.i("screensList", "getExploreData: Error")
                if ((result.errors as? CallException)?.errorCode == NETWORK_LOST) {
                    delay(500)
                     getExploreData()
                }
            }
        }
    }

    private suspend fun getCurrentReadingBooks() {
        when (val result = getCurrentReadingBooksUseCase()) {
            is ActionResult.Success -> {
                Log.i("screensList", "getCurrentReadingBooks: $screensList")
            }
            is ActionResult.Error -> {
                if ((result.errors as? CallException)?.errorCode == NETWORK_LOST) {
                    delay(500)
                    Log.i("screensList", "getCurrentReadingBooks: ${(result.errors as? CallException)?.errorMessage}")
                    getCurrentReadingBooks()
                }
            }
        }
    }

    private suspend fun getProfileInfo() {
        when (val result = getProfileInfoUseCase()) {
            is ActionResult.Success -> {
                analytics.setUserId(result.result.uuid)
                setUserPropertyEvent(mapOf(USER_PROPERTIES__USER_UUID to result.result.uuid))
            }
            is ActionResult.Error -> {
                if ((result.errors as? CallException)?.errorCode == NETWORK_LOST) {
                    delay(500)
                    Log.i("screensList", "getProfileInfo: ${(result.errors as? CallException)?.errorMessage}")
                    getProfileInfo()
                }
            }
        }
    }

    private fun getAllData() {
        viewModelScope.launch {
            Log.i("CacheData", "getCache: getAllData")
            when (val result = getOnBoardingScreensUseCase()) {
                is ActionResult.Success -> {
                    async {
                        insertLaunchInfoFromDBUseCase()
                        if (result.result.size == 1)
                            getExploreDataUseCase()
                        getCurrentReadingBooksUseCase()
                        //val list = listOf("splash","gender","reading_time","tags","for_you")  // For Test
                        _screens.emit(result.result)
                    }.await()
                }
                is ActionResult.Error -> {
                    if ((result.errors as? CallException)?.errorCode == NETWORK_LOST) {
                        delay(1000)
                        getAllData()
                    }
                    Log.i(
                        "NetworkStatus",
                        "getCachingData: ${(result.errors as? CallException)?.errorCode}"
                    )
                }
            }
        }
    }

    fun getNextOpeningScreenList(position: Int) =
        screens.value?.filterIndexed { index, s -> index > position }

    fun getIsAppLaunchFirst() {
        viewModelScope.launch {
            getIsAppLaunchFirstTimeUseCase().collectLatest {
                if (it == false) {
                    trackEvents(com.fiction.me.utils.Events.LAUNCH_FIRST_TIME, eventsType = Analytics.EventsType.ALL)
                    installSendEvent()
                    setIsAppLaunchFirstTimeUseCase(true)
                }
            }
        }
    }
}
