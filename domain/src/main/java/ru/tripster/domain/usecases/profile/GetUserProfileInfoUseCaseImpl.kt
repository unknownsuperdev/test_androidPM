package ru.tripster.domain.usecases.profile

import kotlinx.coroutines.withContext
import ru.tripster.core.ActionResult
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.interactors.profile.GetUserProfileInfoUseCase
import ru.tripster.domain.model.MenuItems
import ru.tripster.domain.model.profile.UserInfo
import ru.tripster.domain.repository.DataStoreRepository
import ru.tripster.domain.repository.profile.ProfileRepository

class GetUserProfileInfoUseCaseImpl(
    private val dispatcher: CoroutineDispatcherProvider,
    private val profileRepository: ProfileRepository,
    private val dataStoreRepository: DataStoreRepository
) : GetUserProfileInfoUseCase {
    override suspend fun invoke(): ActionResult<UserInfo> = withContext(dispatcher.io) {
        dataStoreRepository.saveMenuItem(MenuItems.PROFILE.type)

        when (val apiData = profileRepository.getUserInfo()) {
            is ActionResult.Success -> {
                dataStoreRepository.saveGuidId(apiData.result.id ?: 0)
                dataStoreRepository.saveGuidEmail(apiData.result.email ?: "")
                apiData.result.let {
                    ActionResult.Success(UserInfo.from(it))
                }
            }
            is ActionResult.Error -> {
                ActionResult.Error(apiData.errors)
            }
        }
    }
}