package com.name.domain.usecases

import com.name.core.ActionResult
import com.name.core.CallException
import com.name.core.dispatcher.CoroutineDispatcherProvider
import com.name.domain.Constants.Companion.ERROR_NULL_DATA
import com.name.domain.interactors.GuestUseCase
import com.name.domain.model.registration.GuestToken
import com.name.domain.repository.AuthRepository
import com.name.domain.repository.DataStoreRepository
import kotlinx.coroutines.withContext

class GuestUseCaseImpl(
    private val authRepo: AuthRepository,
    private val dataStoreRepo: DataStoreRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : GuestUseCase {

    override suspend fun invoke(uuId: String): ActionResult<GuestToken> =
        withContext(dispatcher.io) {

            when (val apiData = authRepo.getGuestToken(uuId)) {
                is ActionResult.Success -> {

                    apiData.result.data?.let {
                        val guestToken = GuestToken.from(it)
                        dataStoreRepo.setGuestToken(guestToken)
                        ActionResult.Success(guestToken)
                    } ?: ActionResult.Error(CallException(ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}