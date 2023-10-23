package ru.tripster.domain.usecases.chat

import android.icu.text.SimpleDateFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.Constants.Companion.CHAT_HEADER_ID
import ru.tripster.domain.interactors.chat.OrderCommentsUseCase
import ru.tripster.domain.model.chat.OrderCommentGetModel
import ru.tripster.domain.model.chat.OrderCommentsHeader
import ru.tripster.domain.model.chat.OrderCommentsModel
import ru.tripster.domain.model.chat.OrderCommentsResult
import ru.tripster.domain.model.events.Order
import ru.tripster.domain.repository.chat.OrderCommentsRepository
import java.util.*

class OrderCommentsUseCaseImpl(
    private val orderCommentsRepository: OrderCommentsRepository,
    private val dispatcher: CoroutineDispatcherProvider,
) : OrderCommentsUseCase {
    override suspend fun invoke(
        model: OrderCommentGetModel
    ): Flow<List<OrderCommentsModel>> =

        withContext(dispatcher.io) {
            val dbList = orderCommentsRepository.getCommentsFromDb(model.id)

            var lastDate = ""

            return@withContext dbList.map {
                it.map { i ->
                    OrderCommentsResult.fromOrderCommentsData(i)
                }
            }.map {
                it.toMutableList().apply {
                    this.sortBy { i -> (i as? OrderCommentsResult)?.submitDate?.parseDateFull() }
                    this.reverse()
                    if (this.isNotEmpty()) {
                        lastDate = (this[0] as? OrderCommentsResult)?.submitDate ?: ""
                    }

                    if (this.size == 0 || model.header != null && (this[this.size - 1] as? OrderCommentsResult)?.nextPage?.isEmpty() == true) {
                        model.header?.let { it1 -> this.add(it1) }
                    }

                    this.forEachIndexed { i, item ->
                        val itemSubmitDate = (item as? OrderCommentsResult)?.run {
                            this.submitDate.parseDate()
                        }

                        val itemSubmitDateFull = (item as? OrderCommentsResult)?.run {
                            this.submitDate.parseDateFull()
                        }

                        if (i == 0)
                            (this[i] as? OrderCommentsResult)?.copy(isLastMessage = true)
                                ?.let { ocr ->
                                    this[i] = ocr
                                }

                        if (model.canOpenContacts && model.ordersRateValue >= 60) {
                            (this[i] as? OrderCommentsResult)?.copy(ordersRate = true)
                                ?.let { ocr ->
                                    this[i] = ocr
                                }
                        }

                        if (itemSubmitDate != null && itemSubmitDateFull != null) {

                            if (itemSubmitDateFull < lastDate.parseDateFull() && (this[i] as? OrderCommentsResult)?.viewedByGuide == true && (this[i - 1] as? OrderCommentsResult)?.viewedByGuide == false) {
                                (this[i - 1] as? OrderCommentsResult)?.copy(addHeaderToMessage = true)
                                    ?.let { ocr ->
                                        this[i - 1] = ocr
                                    }

                                lastDate = item.submitDate
                            }

                            if (itemSubmitDate < lastDate.parseDate()) {
                                (this[i - 1] as? OrderCommentsResult)?.copy(addHeaderToMessage = true)
                                    ?.let { ocr ->
                                        this[i - 1] = ocr
                                    }
                                lastDate = item.submitDate
                            }
                        }

                        if (item.id == CHAT_HEADER_ID && this.size > 1) {
                            (this[i - 1] as? OrderCommentsResult)?.copy(addHeaderToMessage = true)
                                ?.let { ocr ->
                                    this[i - 1] = ocr
                                }
                        }

                        if (i > 0) {
                            if ((this[i - 1] as? OrderCommentsResult)?.userRole != (this[i] as? OrderCommentsResult)?.userRole && (this[i] as? OrderCommentsResult)?.systemEventType?.isEmpty() == true)
                                (this[i] as? OrderCommentsResult)?.copy(prevMsgIsFromOtherUser = true)
                                    ?.let { ocr ->
                                        this[i] = ocr
                                    }

                            if ((this[i] as? OrderCommentsResult)?.systemEventType?.isNotEmpty() == true && (this[i - 1] as? OrderCommentsResult)?.systemEventType?.isEmpty() == true) {
                                if ((this[i - 1] as? OrderCommentsResult)?.userRole == "guide") {
                                    (this[i] as? OrderCommentsResult)?.copy(prevMsgIsFromGuide = true)
                                        ?.let { ocr ->
                                            this[i] = ocr
                                        }
                                } else (this[i] as? OrderCommentsResult)?.copy(
                                    prevMsgIsFromOtherUser = true
                                )
                                    ?.let { ocr ->
                                        this[i] = ocr
                                    }
                            }

                            if ((this[i - 1] as? OrderCommentsResult)?.addHeaderToMessage == true) (this[i] as? OrderCommentsResult)?.copy(
                                prevMsgHaveHeader = true
                            )
                                ?.let { ocr ->
                                    this[i] = ocr
                                }
                        }
                    }
                }
            }
        }
}

fun String.parseDate(): Date {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale("ru"))

    return sdf.parse(this)
}

fun String.parseDateFull(): Date {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale("ru"))

    return sdf.parse(this)
}

