package ru.tripster.domain.usecases.profile

import kotlinx.coroutines.withContext
import ru.tripster.core.ActionResult
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.interactors.profile.DeleteAccountUseCase
import ru.tripster.domain.repository.profile.ProfileRepository

class DeleteAccountUseCaseImpl(
    private val dispatcher: CoroutineDispatcherProvider,
    private val profileRepository: ProfileRepository
) : DeleteAccountUseCase {

    override suspend fun invoke(): ActionResult<Unit> = withContext(dispatcher.io) {
        when (val apiData = profileRepository.deleteAccount()) {
            is ActionResult.Success -> {
                apiData.result.let {
                    ActionResult.Success(Unit)
                }
            }
            is ActionResult.Error -> {
                ActionResult.Error(apiData.errors)
            }
        }
    }

}
