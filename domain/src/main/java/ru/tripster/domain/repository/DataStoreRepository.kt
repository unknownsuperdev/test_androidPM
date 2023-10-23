package ru.tripster.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun saveToken(token: String)
    suspend fun getToken(): Flow<String?>
    suspend fun saveDeviceId(id: String)
    suspend fun getDeviceId(): Flow<String?>
    suspend fun saveExperienceIdFiltration(experienceId: Int)
    suspend fun getExperienceIdFiltration(): Flow<Int?>

    suspend fun saveExperienceTitleFiltration(title: String)
    suspend fun getExperienceTitleFiltration(): Flow<String?>

    suspend fun saveClosingTimeFiltrationId(experienceId: Int)
    suspend fun getClosingTimeFiltrationId(): Flow<Int?>

    suspend fun saveUserData(emailAndPass: String)
    suspend fun getUserData(): Flow<String?>

    suspend fun saveGuidId(guidId: Int)
    suspend fun getGuidId(): Flow<Int?>

    suspend fun saveGuidEmail(guidEmail: String)
    suspend fun getGuidEmail(): Flow<String?>

    suspend fun saveMenuItem(item: String)
    suspend fun getMenuItem(): Flow<String?>

    suspend fun saveCurrentStage(stage: String)
    suspend fun getCurrentStage(): Flow<String?>
    suspend fun saveLastRemindLaterTime(time: String)
    suspend fun getLastRemindLaterTime(): Flow<String?>

    suspend fun saveNotificationState(isSelected: Boolean)
    suspend fun getNotificationState(): Flow<Boolean?>
}