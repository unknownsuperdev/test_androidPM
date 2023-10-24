package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.utils.Constants
import com.fiction.domain.interactors.BuyChapterUseCase
import com.fiction.domain.interactors.GetBookChaptersUseCase
import com.fiction.domain.interactors.GetProfileInfoUseCase
import com.fiction.domain.repository.CoinShopRepo
import kotlinx.coroutines.withContext

class BuyChapterUseCaseImpl(
    private val coinShopRepo: CoinShopRepo,
    private val profileInfoUseCase: GetProfileInfoUseCase,
    private val getBookChaptersUseCase: GetBookChaptersUseCase,
    private val dispatcher: CoroutineDispatcherProvider
): BuyChapterUseCase {

    override suspend fun invoke(chapterId: Long): ActionResult<Int?> =
        withContext(dispatcher.io) {
            when (val apiData = coinShopRepo.buyChapter(chapterId)) {
                is ActionResult.Success -> {
                    apiData.result.data?.let { response ->
                        response.balance?.let { profileInfoUseCase.updateBalance(it) }
                        getBookChaptersUseCase(-1L,lastReadChapterId = chapterId, isPurchased = true)
                        ActionResult.Success(response.balance)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }
                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}