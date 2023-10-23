package ru.tripster.data.dataservice.appservice

import kotlinx.coroutines.flow.Flow

interface DataStoreService {

    suspend fun setUserToken(token: String)
    fun getUserToken(): Flow<String?>
    suspend fun saveExperienceIdFiltration(experienceId: Int)
    fun getExperienceIdFiltration(): Flow<Int?>
    suspend fun saveExperienceTitleFiltration(title: String)
    fun getExperienceTitleFiltration(): Flow<String?>
    suspend fun saveClosingTimeFiltrationId(experienceId: Int?)
    fun getClosingTimeFiltrationId(): Flow<Int?>
    suspend fun setDeviceID(id: String)
    fun getDeviceId(): Flow<String?>

    suspend fun setGuestToken(token: String)
    fun getGuestToken(): Flow<String?>

    suspend fun setUserData(emailAndPassword: String?)
    fun getUserData(): Flow<String?>

    suspend fun saveGuidId(guidId: Int)
    fun getGuidId(): Flow<Int?>

    suspend fun saveGuidEmail(guidEmail: String)
    fun getGuidEmail(): Flow<String?>

    suspend fun saveMenuItem(item: String?)
    fun getMenuItem(): Flow<String?>

    suspend fun saveCurrentStage(stage: String?)
    fun getCurrentStage(): Flow<String?>
    suspend fun saveLastRemindLaterTime(time: String?)

    fun getLastRemindLaterTime(): Flow<String?>

    suspend fun saveNotificationState(isSelected: Boolean?)

    fun getNotificationState(): Flow<Boolean?>

}