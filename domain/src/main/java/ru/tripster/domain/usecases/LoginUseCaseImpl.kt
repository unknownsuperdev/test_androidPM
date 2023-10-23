package ru.tripster.domain.usecases

import kotlinx.coroutines.withContext
import ru.tripster.core.ActionResult
import ru.tripster.core.CallException
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.Constants.Companion.ERROR_NULL_DATA
import ru.tripster.domain.interactors.LoginUserCase
import ru.tripster.domain.repository.DataStoreRepository
import ru.tripster.domain.repository.LoginRepository
import ru.tripster.entities.request.LoginRequest

class LoginUseCaseImpl(
    private val loginRepository: LoginRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : LoginUserCase {

    override suspend fun invoke(
        username: String,
        password: String,
        saveUserData: Boolean
    ): ActionResult<Unit> =
        withContext(dispatcher.io) {
            val request = LoginRequest(username, password)
            when (val apiData = loginRepository.login(request)) {
                is ActionResult.Success -> {
                    apiData.result.token?.let {
                        if (saveUserData)
                            dataStoreRepository.saveUserData("$username $password") else dataStoreRepository.saveUserData(
                            ""
                        )
                        dataStoreRepository.saveToken(it)
                        ActionResult.Success(Unit)
                    } ?: ActionResult.Error(CallException(ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}