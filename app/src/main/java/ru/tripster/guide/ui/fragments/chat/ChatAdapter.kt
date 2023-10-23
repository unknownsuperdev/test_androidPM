package ru.tripster.guide.ui.fragments.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import ru.tripster.domain.model.chat.*
import ru.tripster.domain.model.events.Status
import ru.tripster.guide.Constants.CHAT_HEADER_ID
import ru.tripster.guide.R
import ru.tripster.guide.appbase.adapter.BaseAdapter
import ru.tripster.guide.appbase.adapter.BaseViewHolder
import ru.tripster.guide.databinding.*
import ru.tripster.guide.extensions.*

class ChatAdapter(
    val itemClickError: (comment: String, key: String) -> Unit,
    val showContact: () -> Unit,
    val howToSeeContact: () -> Unit,
    val rootClick: () -> Unit,
    val openTicketLink: () -> Unit
) :
    BaseAdapter<ViewBinding, OrderCommentsModel, BaseViewHolder<OrderCommentsModel, ViewBinding>>() {

    private var personCount = 0
    var orderDetail: (String, Boolean, String) -> Unit = { status, completed, type ->
        ordersStatus = status
        ordersType = type
        booleanOfCompleted = completed
    }
    private var ordersStatus = ""
    private var ordersType = ""
    private var booleanOfCompleted = false


    override fun getItemViewType(position: Int): Int {
        return when {
            (getItem(position) as? OrderCommentsResult)?.error?.isNotEmpty() == true -> ChatItemTypeEnum.ERROR.number
            (getItem(position) as? OrderCommentsHeader)?.id == CHAT_HEADER_ID -> ChatItemTypeEnum.USER.number
            (getItem(position) as? OrderCommentsResult)?.systemEventData != SystemEventData.empty() || (getItem(
                position
            ) as OrderCommentsResult).systemEventType.isNotEmpty() -> ChatItemTypeEnum.TICKETS.number
            (getItem(position) as? OrderCommentsResult)?.userRole == UserRoleChat.GUIDE.value -> ChatItemTypeEnum.SENT.number
            (getItem(position) as? OrderCommentsResult)?.userRole == UserRoleChat.TRAVELER.value -> ChatItemTypeEnum.RECEIVED.number

            else -> -1
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<OrderCommentsModel, ViewBinding> {
        return when (ChatItemTypeEnum.toEnum(viewType)) {
            ChatItemTypeEnum.SENT -> {
                SentItemViewHolder(
                    ItemInputMessageTypeBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            ChatItemTypeEnum.RECEIVED -> {
                ReceivedItemViewHolder(
                    ItemReceivedMessageTypeBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            ChatItemTypeEnum.TICKETS -> {
                UserTicketInfoViewHolder(
                    ItemStandartMessageTypeBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            ChatItemTypeEnum.ERROR -> {
                ErrorItemViewHolder(
                    ItemInputMessageErrorBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            ChatItemTypeEnum.USER -> {
                UserItemViewHolder(
                    ItemUserTypeBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    private inner class SentItemViewHolder(private val binding: ItemInputMessageTypeBinding) :
        BaseViewHolder<OrderCommentsModel, ViewBinding>(binding) {
        override fun onItemClick(item: OrderCommentsModel) {
            super.onItemClick(item)
            rootClick.invoke()
        }

        override fun bind(item: OrderCommentsModel, context: Context) {
            with(binding) {
                (item as? OrderCommentsResult)?.run {

                    val params = container.layoutParams as ViewGroup.MarginLayoutParams

                    when {
                        item.prevMsgHaveHeader -> params.bottomMargin =
                            context.dpToPx(R.dimen.space_12)

                        item.isLastMessage || item.prevMsgIsFromOtherUser -> {
                            params.bottomMargin = context.dpToPx(R.dimen.space_16)
                        }

                        else -> params.bottomMargin = context.dpToPx(R.dimen.space_4)
                    }

                    container.layoutParams = params

                    if (item.addHeaderToMessage == true && item.submitDate.isTodayOrYesterday(
                            context, item.viewedByGuide
                        ).isNotEmpty()
                    ) {
                        dateOfMessage.makeVisible()
                        dateOfMessage.text =
                            item.submitDate.isTodayOrYesterday(context, item.viewedByGuide)
                    } else {
                        dateOfMessage.makeVisibleGone()
                    }

                    message.text = item.comment
                    date.text = item.submitDate.dateFormattingOnlyHour()
                    readStatus.setImageResource(0)
                    when {
                        item.id == 0 && !item.viewedByTraveler -> readStatus.background =
                            ContextCompat.getDrawable(context, R.drawable.ic_clock)
                        item.viewedByTraveler -> readStatus.background =
                            ContextCompat.getDrawable(context, R.drawable.ic_send_status)
                        !item.viewedByTraveler -> readStatus.background =
                            ContextCompat.getDrawable(context, R.drawable.ic_baseline_check_24)
                    }
                }
            }
        }
    }

    private inner class ReceivedItemViewHolder(private val binding: ItemReceivedMessageTypeBinding) :
        BaseViewHolder<OrderCommentsModel, ViewBinding>(binding) {
        override fun onItemClick(item: OrderCommentsModel) {
            super.onItemClick(item)
            rootClick.invoke()
        }

        override fun bind(item: OrderCommentsModel, context: Context) {
            (item as? OrderCommentsResult)?.run {
                with(binding) {
                    message.text = item.comment
                    date.text = item.submitDate.dateFormattingOnlyHour()
                    if (item.user.avatar150Url?.isEmpty() == false) {
                        imgUser.setStrokeColorResource(R.color.ten_percent_of_black)
                        imgUser.strokeWidth = 2f
                    }
                    Glide.with(context).load(item.user.avatar150Url)
                        .placeholder(R.drawable.ic_avatar_icon_placeholder)
                        .centerCrop()
                        .into(imgUser)

                    val params = lConst.layoutParams as ViewGroup.MarginLayoutParams

                    when {
                        item.prevMsgHaveHeader ->
                            params.bottomMargin = context.dpToPx(R.dimen.space_12)

                        item.isLastMessage || item.prevMsgIsFromOtherUser -> {
                            params.bottomMargin = context.dpToPx(R.dimen.space_16)
                        }

                        else -> params.bottomMargin = context.dpToPx(R.dimen.space_4)
                    }

                    lConst.layoutParams = params

                    if (item.addHeaderToMessage == true && item.submitDate.isTodayOrYesterday(
                            context, item.viewedByGuide
                        ).isNotEmpty()
                    ) {
                        dateOfMessage.makeVisible()
                        dateOfMessage.text =
                            item.submitDate.isTodayOrYesterday(context, item.viewedByGuide)

                        val dateOfMessageParams =
                            dateOfMessage.layoutParams as ViewGroup.MarginLayoutParams

                        if (dateOfMessage.text.toString() == context.getString(R.string.unread_comments)) {
                            dateOfMessage.setBackgroundColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.gray_90
                                )
                            )
                            dateOfMessage.setPadding(
                                0,
                                context.dpToPx(R.dimen.space_5),
                                0,
                                context.dpToPx(R.dimen.space_5)
                            )
                        } else {
                            dateOfMessage.setBackgroundResource(0)
                            dateOfMessage.setPadding(0, 0, 0, 0)
                        }

                        dateOfMessage.layoutParams = dateOfMessageParams

                    } else {
                        dateOfMessage.makeVisibleGone()
                    }

                    if (item.includeContacts) {
                        if (item.ordersRate) {
                            when {
                                !item.comment.contains("*********") -> {
                                    showContacts.visibility = View.GONE
                                }
                                else -> {
                                    if (statusLogic(
                                            context,
                                            ordersStatus,
                                            booleanOfCompleted,
                                            ordersType
                                        )
                                    ) {
                                        showContacts.visibility = View.VISIBLE
                                        showContacts.setOnClickListener { showContact() }
                                    } else showContacts.visibility = View.GONE
                                }
                            }
                        } else if (item.comment.contains("*********")) {
                            seeContactUntilPaymentContainer.visibility = View.VISIBLE
                            howToSeeContact.setOnClickListener { howToSeeContact() }
                        } else seeContactUntilPaymentContainer.visibility = View.GONE
                    }
                }
            }
        }
    }

    private inner class UserItemViewHolder(private val binding: ItemUserTypeBinding) :
        BaseViewHolder<OrderCommentsModel, ViewBinding>(binding) {
        override fun onItemClick(item: OrderCommentsModel) {
            super.onItemClick(item)
            rootClick.invoke()
        }

        override fun bind(item: OrderCommentsModel, context: Context) {
            with(binding) {
                (item as? OrderCommentsHeader)?.run {
                    val isSpecifiedPersonCount = item.memberCountAndPrice?.substringBefore(" ")
                    if (isSpecifiedPersonCount != null && isSpecifiedPersonCount.isDigitsOnly()) {
                        personCount = isSpecifiedPersonCount.toInt()
                    }
                    if (personCount > 0) {
                        personCount =
                            item.memberCountAndPrice?.substringBefore(" ")?.toInt() ?: 0
                        infoUser.text = item.memberCountAndPrice
                    } else {
                        infoUser.text = context.getString(R.string.no_people_count_tour)
                    }
                    if (item.avatar.isNotEmpty()) {
                        imgUser.setStrokeColorResource(R.color.ten_percent_of_black)
                        imgUser.strokeWidth = 2f
                    }
                    Glide.with(context).load(item.avatar)
                        .placeholder(R.drawable.ic_avatar_icon_placeholder)
                        .centerCrop()
                        .into(imgUser)
                    nameUser.text = item.name.shortedSurname()
                }
            }
        }
    }

    private inner class ErrorItemViewHolder(private val binding: ItemInputMessageErrorBinding) :
        BaseViewHolder<OrderCommentsModel, ViewBinding>(binding) {
        override fun bind(item: OrderCommentsModel, context: Context) {
            var commentCount = 1
            with(binding) {
                (item as? OrderCommentsResult)?.run {
                    message.text = item.comment
                    val params = container.layoutParams as ViewGroup.MarginLayoutParams

                    when {
                        item.prevMsgHaveHeader -> params.bottomMargin =
                            context.dpToPx(R.dimen.space_12)

                        item.isLastMessage || item.prevMsgIsFromOtherUser -> {
                            params.bottomMargin = context.dpToPx(R.dimen.space_16)
                        }

                        else -> params.bottomMargin = context.dpToPx(R.dimen.space_4)
                    }

                    container.layoutParams = params

                    binding.error.setOnClickListener {
                        if (commentCount == 1) {
                            itemClickError(
                                message.text.toString(), item.key.toString()
                            )
                            commentCount--
                        }
                    }
                    if (item.addHeaderToMessage == true) {
                        dateOfMessage.makeVisible()
                        dateOfMessage.text =
                            item.submitDate.isTodayOrYesterday(context, item.viewedByGuide)

                    } else {
                        dateOfMessage.makeVisibleGone()
                    }
                }
            }
        }
    }

    private inner class UserTicketInfoViewHolder(private val binding: ItemStandartMessageTypeBinding) :
        BaseViewHolder<OrderCommentsModel, ViewBinding>(binding) {
        override fun onItemClick(item: OrderCommentsModel) {
            super.onItemClick(item)
            rootClick.invoke()
        }

        override fun bind(item: OrderCommentsModel, context: Context) {
            with(binding) {
                (item as? OrderCommentsResult)?.run {
                    hour.text = item.submitDate.dateFormattingOnlyHour()

                    val params = userImage.layoutParams as ViewGroup.MarginLayoutParams

                    when {
                        item.prevMsgHaveHeader -> params.bottomMargin =
                            context.dpToPx(R.dimen.space_12)

                        item.isLastMessage || item.prevMsgIsFromOtherUser -> params.bottomMargin =
                            context.dpToPx(R.dimen.space_16)

                        item.prevMsgIsFromGuide -> params.bottomMargin =
                            context.dpToPx(R.dimen.space_8)

                        else -> params.bottomMargin = context.dpToPx(R.dimen.space_0)
                    }

                    userImage.layoutParams = params

                    if (item.addHeaderToMessage == true && item.submitDate.isTodayOrYesterday(
                            context, item.viewedByGuide
                        ).isNotEmpty()
                    ) {
                        dateOfMessage.makeVisible()
                        dateOfMessage.text =
                            item.submitDate.isTodayOrYesterday(context, item.viewedByGuide)

                        val dateOfMessageParams =
                            dateOfMessage.layoutParams as ViewGroup.MarginLayoutParams

                        if (dateOfMessage.text.toString() == context.getString(R.string.unread_comments)) {
                            dateOfMessage.setBackgroundColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.gray_90
                                )
                            )
                            dateOfMessage.setPadding(
                                0,
                                context.dpToPx(R.dimen.space_5),
                                0,
                                context.dpToPx(R.dimen.space_5)
                            )
                        } else {
                            dateOfMessage.setBackgroundResource(0)
                            dateOfMessage.setPadding(0, 0, 0, 0)
                        }

                        dateOfMessage.layoutParams = dateOfMessageParams
                    } else {
                        dateOfMessage.makeVisibleGone()
                    }

                    val changesValid =
                        item.systemEventData.changes.dateTime.oldValue != item.systemEventData.changes.dateTime.newValue ||
                                item.systemEventData.changes.personsCount.oldValue != item.systemEventData.changes.personsCount.newValue ||
                                item.systemEventData.changes.fullPrice.oldValue != item.systemEventData.changes.fullPrice.newValue

                    when {
                        !item.systemEventData.changes.confirmed.oldValue && item.systemEventData.changes.confirmed.newValue -> {
                            messageDesc.text =
                                context.getString(R.string.order_confirmation_by_guide)
                            info.makeVisibleGone()
                        }

                        changesValid -> {
                            messageDesc.text =
                                if (item.userRole == UserRoleChat.TRAVELER.value) context.getString(
                                    R.string.order_is_processed
                                ) else context.getString(
                                    R.string.organizer_changed_order
                                )
                            context.getString(R.string.organizer_changed_order)
                            info.makeVisible()
                            info.text = when {
                                item.systemEventData.changes.dateTime.newValue.isNotEmpty() && item.systemEventData.changes.personsCount.newValue != 0 && item.systemEventData.changes.fullPrice.newValue != 0.0 ->
                                    context.getString(
                                        R.string.changed_order_info,
                                        item.systemEventData.changes.dateTime.newValue.substringBefore(
                                            " "
                                        ).dateToFullName(),
                                        item.systemEventData.changes.dateTime.newValue.substringAfter(
                                            " "
                                        )
                                    ) + "\n" +
                                            context.getString(
                                                R.string.price,
                                                context.getString(R.string.price_value)
                                                    .setParticipantAndCurrency(
                                                        null,
                                                        item.systemEventData.changes.fullPrice.newValue.toInt(),
                                                        item.currency?.currency()
                                                    )
                                            ) + "\n" + context.getString(
                                        R.string.members_count_dynamic_without_dot,
                                        item.systemEventData.changes.personsCount.newValue
                                    )

                                item.systemEventData.changes.personsCount.newValue != 0 && item.systemEventData.changes.fullPrice.newValue != 0.0 ->
                                    context.getString(
                                        R.string.price,
                                        context.getString(R.string.price_value)
                                            .setParticipantAndCurrency(
                                                null,
                                                item.systemEventData.changes.fullPrice.newValue.toInt(),
                                                item.currency?.currency()
                                            )
                                    ) + "\n" + context.getString(
                                        R.string.members_count_dynamic_without_dot,
                                        item.systemEventData.changes.personsCount.newValue
                                    )

                                item.systemEventData.changes.dateTime.newValue.isNotEmpty() -> context.getString(
                                    R.string.changed_order_info,
                                    item.systemEventData.changes.dateTime.newValue.substringBefore(" ")
                                        .dateToFullName(),
                                    item.systemEventData.changes.dateTime.newValue.substringAfter(" ")
                                )

                                item.systemEventData.changes.personsCount.newValue != 0 -> context.getString(
                                    R.string.members_count_dynamic_without_dot,
                                    item.systemEventData.changes.personsCount.newValue
                                )

                                item.systemEventData.changes.fullPrice.newValue != 0.0 -> context.getString(
                                    R.string.price,
                                    context.getString(R.string.price_value)
                                        .setParticipantAndCurrency(
                                            null,
                                            item.systemEventData.changes.fullPrice.newValue.toInt(),
                                            item.currency?.currency()
                                        )
                                )

                                else -> {
                                    context.getString(R.string.organizer_changed_order)
                                }
                            }
                        }

                        item.systemEventType == UserStatusChat.BOOKED.value -> {
                            messageDesc.text = context.getString(R.string.prepayment_made)
                            info.makeVisible()
                            info.initTextLinks(
                                context.getString(R.string.prepayment_info),
                                context,
                                context.getString(R.string.prepayment_info_colored)
                            ) {
                                openTicketLink()
                            }
                        }

                        item.systemEventType == UserStatusChat.REJECT.value -> {
                            messageDesc.text = context.getString(R.string.order_cancelled)
                            info.text = item.systemEventData.reason
                        }

                        item.systemEventType == UserStatusChat.CREATED.value && item.systemEventData.tickets.isNotEmpty() -> {
                            messageDesc.text =
                                if (item.userRole == UserRoleChat.TRAVELER.value) context.getString(
                                    R.string.order_confirmation_by_tourist
                                ) else context.getString(
                                    R.string.order_confirmation_by_guide
                                )
                            info.makeVisible()
                            info.text = context.getString(
                                R.string.members_count_dynamic,
                                item.systemEventData.numberOfPersons
                            )
                            containerTicketInfo.removeAllViews()
                            item.systemEventData.tickets.forEach {
                                val type = AppCompatTextView(context)
                                type.setTextAppearance(R.style.Text_17_400)
                                type.text =
                                    context.getString(
                                        R.string.ticket_type_count,
                                        it.name,
                                        it.count
                                    )
                                containerTicketInfo.addView(type)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun statusLogic(
        context: Context,
        status: String,
        completed: Boolean,
        type: String
    ): Boolean {
        when (status) {
            Status.CANCELLED.value -> return false
            Status.PAID.value -> return !completed
        }
        return type == context.getString(R.string.private_type)
    }
}