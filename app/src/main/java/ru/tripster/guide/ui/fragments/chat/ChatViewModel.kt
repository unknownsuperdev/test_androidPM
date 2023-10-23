package ru.tripster.guide.ui.fragments.chat

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.tripster.core.ActionResult
import ru.tripster.domain.interactors.chat.OrderCommentsChatOpenedUseCase
import ru.tripster.domain.interactors.chat.OrderCommentsInsertUseCase
import ru.tripster.domain.interactors.chat.OrderCommentsUseCase
import ru.tripster.domain.interactors.chat.OrderPostCommentUseCase
import ru.tripster.domain.model.chat.*
import ru.tripster.guide.appbase.viewmodel.BaseViewModel

class ChatViewModel(
    private val orderCommentsInsertUseCase: OrderCommentsInsertUseCase,
    private val orderCommentsUseCase: OrderCommentsUseCase,
    private val orderPostCommentUseCase: OrderPostCommentUseCase,
    private val orderCommentsChatOpenedUseCase: OrderCommentsChatOpenedUseCase
) : BaseViewModel() {

    init {
        repeatCall()
    }

    private val _orderComments = MutableStateFlow<OrderComments?>(null)
    val orderComments = _orderComments.asSharedFlow()

    private val _commentsList = MutableSharedFlow<List<OrderCommentsModel?>>()
    val commentsList = _commentsList.asSharedFlow()

    private val _commentsError = MutableSharedFlow<String>()

    val commentsError = _commentsError.asSharedFlow()
    private val _postCommentError = MutableSharedFlow<String>()

    val postCommentError = _postCommentError.asSharedFlow()
    private val _errorMessage = MutableStateFlow<Boolean?>(null)

    val errorMessage = _errorMessage.asSharedFlow()
    private var postedComment: OrderPostComment? = null


    private var orderCommentGetModel: OrderCommentGetModel? = null
    var booleanOfContactsSend = false
    var page = 1
    var isLoading: Boolean = false
    var isLastPage: Boolean = false
    private var firstEntrance: Boolean = true
    var noInternetToastShown: Boolean = false
    var countOfSpace = 0
    var list = emptyList<OrderCommentsModel>()

    fun getOrderComments(model: OrderCommentGetModel, page: Int) {
        orderCommentGetModel = model

        viewModelScope.launch {

            when (val result = orderCommentsInsertUseCase.invoke(
                model.id,
                page,
                model.currency
            )) {

                is ActionResult.Success -> {
                    _orderComments.emit(result.result)
                    if (firstEntrance) {
                        orderCommentsUseCase.invoke(model).collect {
                            this@ChatViewModel.page =
                                (it[it.size - 1] as? OrderCommentsResult)?.nextPage?.substringAfter(
                                    "page="
                                )?.take(1)?.toInt()?.minus(1) ?: 1

                            firstEntrance = false
                            _commentsList.emit(it)
                        }
                    }
                }

                is ActionResult.Error -> {
                    _commentsError.emit(result.errors.message.toString())
                }
            }
        }
    }

    fun markAsRead() {
        viewModelScope.launch {
            _commentsList.collect {
                it.forEach { orderComment ->
                    if ((orderComment as? OrderCommentsResult)?.viewedByGuide == false) {
                        orderCommentsChatOpenedUseCase.invoke(
                            orderComment.id,
                            OrderCommentViewed(true)
                        )
                    }
                }
            }
        }
    }

    fun postComment(comment: OrderPostComment, key: String? = null) {
        viewModelScope.launch {
            postedComment = comment
            orderPostCommentUseCase.invoke(comment, key)
        }
    }

    private fun repeatCall() {
         viewModelScope.launch {
            repeat(Int.MAX_VALUE) {
                orderCommentGetModel?.let { model -> getOrderComments(model, 1) }
                delay(1000)
            }
        }
    }
}