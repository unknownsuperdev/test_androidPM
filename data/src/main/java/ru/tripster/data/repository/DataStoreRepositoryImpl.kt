package ru.tripster.data.repository

import kotlinx.coroutines.flow.Flow
import ru.tripster.data.dataservice.appservice.DataStoreService
import ru.tripster.domain.repository.DataStoreRepository

class DataStoreRepositoryImpl(private val storeService: DataStoreService) : DataStoreRepository {

    override suspend fun saveToken(token: String) {
        storeService.setUserToken(token)
    }

    override suspend fun getToken(): Flow<String?> = storeService.getUserToken()

    override suspend fun saveDeviceId(id: String) {
        storeService.setDeviceID(id)
    }

    override suspend fun getDeviceId(): Flow<String?> = storeService.getDeviceId()
    override suspend fun saveExperienceIdFiltration(experienceId: Int) {
        storeService.saveExperienceIdFiltration(experienceId)
    }

    override suspend fun getExperienceIdFiltration(): Flow<Int?> =
        storeService.getExperienceIdFiltration()

    override suspend fun saveExperienceTitleFiltration(title: String) {
        storeService.saveExperienceTitleFiltration(title)
    }

    override suspend fun getExperienceTitleFiltration(): Flow<String?> =
        storeService.getExperienceTitleFiltration()

    override suspend fun saveClosingTimeFiltrationId(experienceId: Int) {
        storeService.saveClosingTimeFiltrationId(experienceId)
    }

    override suspend fun getClosingTimeFiltrationId(): Flow<Int?> =
        storeService.getClosingTimeFiltrationId()


    override suspend fun saveUserData(emailAndPass: String) {
        storeService.setUserData(emailAndPass)

    }

    override suspend fun getUserData(): Flow<String?> = storeService.getUserData()
    override suspend fun saveGuidId(guidId: Int) {
        storeService.saveGuidId(guidId)
    }

    override suspend fun getGuidId(): Flow<Int?> = storeService.getGuidId()
    override suspend fun saveGuidEmail(guidEmail: String) {
        storeService.saveGuidEmail(guidEmail)
    }

    override suspend fun getGuidEmail(): Flow<String?> = storeService.getGuidEmail()
    override suspend fun saveMenuItem(item: String) {
        storeService.saveMenuItem(item)
    }
    override suspend fun getMenuItem(): Flow<String?> = storeService.getMenuItem()
    override suspend fun saveCurrentStage(stage: String) {
        storeService.saveCurrentStage(stage)
    }
    override suspend fun getCurrentStage(): Flow<String?> = storeService.getCurrentStage()
    override suspend fun saveLastRemindLaterTime(time: String) {
        storeService.saveLastRemindLaterTime(time)
    }
    override suspend fun getLastRemindLaterTime(): Flow<String?> = storeService.getLastRemindLaterTime()
    override suspend fun saveNotificationState(isSelected: Boolean) {
        storeService.saveNotificationState(isSelected)
    }
    override suspend fun getNotificationState(): Flow<Boolean?> = storeService.getNotificationState()
}