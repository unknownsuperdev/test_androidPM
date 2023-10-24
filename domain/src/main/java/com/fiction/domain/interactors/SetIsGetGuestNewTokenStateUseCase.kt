package com.fiction.domain.interactors

interface SetIsGetGuestNewTokenStateUseCase {
    suspend operator fun invoke(isGetNewToken: Boolean)
}
