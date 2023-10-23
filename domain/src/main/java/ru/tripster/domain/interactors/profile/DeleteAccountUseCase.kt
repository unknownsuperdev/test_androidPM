package ru.tripster.domain.interactors.profile

import ru.tripster.core.ActionResult

interface DeleteAccountUseCase {

    suspend operator fun invoke(): ActionResult<Unit>

}
