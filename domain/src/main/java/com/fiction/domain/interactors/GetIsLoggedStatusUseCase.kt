package com.fiction.domain.interactors

interface GetIsLoggedStatusUseCase {
 suspend operator fun invoke(): Boolean?
}