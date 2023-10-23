package ru.tripster.domain.interactors

import ru.tripster.core.ActionResult

interface LoginUserCase {
    suspend operator fun invoke(
        username: String,
        password: String,
        saveUserData: Boolean
    ): ActionResult<Unit>
}