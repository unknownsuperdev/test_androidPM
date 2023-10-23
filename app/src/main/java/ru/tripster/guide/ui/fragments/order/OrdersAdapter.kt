package ru.tripster.guide.ui.fragments.order

import android.content.Context
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import ru.tripster.domain.model.events.*
import ru.tripster.guide.R
import ru.tripster.guide.appbase.adapter.BasePagingAdapter
import ru.tripster.guide.appbase.adapter.BaseViewHolder
import ru.tripster.guide.databinding.ItemGroupTourBinding
import ru.tripster.guide.databinding.ItemPrivateTourBinding
import ru.tripster.guide.extensions.*


class OrdersAdapter(
    val itemClickIndividual: (IndividualOrder) -> Unit,
    val itemClickGroupTour: (EventResults) -> Unit,
    val itemClickConfirmOrder: (Int, String, String, String, String, String) -> Unit,
    val itemClickMessage: (OrderDetailsForChat, String, String) -> Unit,
    val itemClickComment: (OrderDetailsForChat, String, String) -> Unit,
    val itemPhoneClick: (String, String, Int, String, String) -> Unit
) :
    BasePagingAdapter<ViewBinding, EventResults, BaseViewHolder<EventResults, ViewBinding>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<EventResults, ViewBinding> {
        return when (viewType) {
            VIEW_TYPE_PRIVATE_TOUR -> {
                ViewHolderPrivateTour(
                    ItemPrivateTourBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                ViewHolderGroupTour(
                    ItemGroupTourBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    inner class ViewHolderPrivateTour(private val binding: ItemPrivateTourBinding) :
        BaseViewHolder<EventResults, ViewBinding>(binding) {
        private val orders = mutableListOf<Order>()
        override fun onItemClick(item: EventResults) {
            itemClickIndividual(
                IndividualOrder(
                    status = item.status,
                    orderId = orders[0].id,
                    eventId = item.id,
                    awareStartDt = item.awareStartDt,
                    type = item.experience.type,
                    initiator = orders[0].reject.initiator
                )
            )
        }

        override fun bind(item: EventResults, context: Context) {

            with(binding.containerLocation) {

                item.status.statusType(
                    status,
                    context,
                    item.awareStartDt,
                    "",
                    false,
                    context.getString(R.string.orders), 1.0f
                )
                orders.clear()

                item.orders.cancelled.forEach {
                    item.status.statusType(
                        status,
                        context,
                        item.awareStartDt,
                        it.reject.initiator,
                        item.guideLastVisitDate.isVisitedOrder(item.lastModifiedDate),
                        context.getString(R.string.orders),
                        1.0f
                    )
                }
                when {
                    item.orders.paid.isNotEmpty() -> item.orders.paid[0].let {
                        orders.add(it)
                    }
                    item.orders.pendingPayment.isNotEmpty() -> item.orders.pendingPayment[0].let {
                        orders.add(it)
                    }
                    item.orders.confirmation.isNotEmpty() -> item.orders.confirmation[0]
                        .let {
                            orders.add(it)
                        }
                    item.orders.cancelled.isNotEmpty() -> item.orders.cancelled[0]
                        .let {
                            orders.add(it)
                        }
                    item.orders.messaging.isNotEmpty() -> item.orders.messaging[0].let {
                        orders.add(it)
                    }

                    else -> item.orders.empty[0].let {
                        orders.add(it)
                    }
                }

                if (orders.isNotEmpty()) {
                    orders[0].id.let { number.text = context.getString(R.string.number, it) }
                }
                location.text = item.experience.title

                when {
                    item.date.isNotEmpty() && item.time.isEmpty() -> {
                        date.text =
                            item.date.dateFormattingOnlyDate()
                    }
                    item.date.isEmpty() && item.time.isEmpty() -> {
                        date.text = context.getString(R.string.no_date_and_time)
                        date.setTextColor(ContextCompat.getColor(context, R.color.gray))
                    }
                    else -> {
                        date.text = (item.date + 'T' + item.time).dateFormattingWithHour(
                            item.experience.duration
                        )
                        date.setTextColor(ContextCompat.getColor(context, R.color.gray_20))
                    }
                }
            }

            with(binding.orderUser) {

                when {
                    item.status == Status.PAID.value && item.awareStartDt.check24Hours() -> phone.isVisible =
                        false
                    item.status == Status.CANCELLED.value -> phone.isVisible = false
                    else -> phone.isVisible = true
                }

                confirmOrderUser.isVisible =
                    orders[0].status == Status.CONFIRM.value || orders[0].status == Status.MESSAGING.value
                if (orders[0].traveler.avatar150Url.isNotEmpty()) {
                    avatar.setStrokeColorResource(R.color.ten_percent_of_black)
                    avatar.strokeWidth = 2f
                }

                Glide.with(context).load(orders[0].traveler.avatar150Url)
                    .placeholder(R.drawable.ic_avatar_icon_placeholder).centerCrop()
                    .into(avatar)

                name.text = orders[0].traveler.name.shortedSurname()

                if (orders[0].newMessagesCount != 0) {
                    messageCount.text = orders[0].newMessagesCount.toString()
                    messageCount.isVisible = true
                } else {
                    messageCount.isVisible = false
                }

                if (orders[0].lastMessage.comment.isNotEmpty()) {
                    val listOfWord = orders[0].lastMessage.comment.split(" ").toMutableList()
                    listOfWord.forEachIndexed { index, word ->
                        word.replace("-", "")
                        if (checkPhone(word)) {
                            hideContacts(listOfWord, word, index, true).let {
                                messageContent.setResizableText(
                                    it,
                                    3,
                                    true
                                )
                            }
                            messageContent.makeVisible()
                            exclude.makeVisible()
                        } else {
                            hideContacts(listOfWord, word, index, false).let {
                                messageContent.setResizableText(
                                    it,
                                    3,
                                    true
                                )
                            }
                            messageContent.makeVisible()
                            exclude.makeVisible()
                        }
                    }


                } else {
                    messageContent.makeVisibleGone()
                    exclude.makeVisibleGone()

                }

                val price =
                    if (orders[0].fullPrice.toInt() != 0) orders[0].fullPrice.toInt() else item.experience.price.value.toInt()

                val detailText = context.getString(
                    R.string.details_member_price,
                    orders[0].personsCount,
                    price,
                    item.experience.price.currency.currency()
                )

                val detailTextFull = context.getString(
                    R.string.details
                ).setParticipantAndCurrency(
                    context.resources.getQuantityString(
                        R.plurals.people_count_tour,
                        orders[0].personsCount,
                        orders[0].personsCount
                    ),
                    price, item.experience.price.currency.currency()
                )

                if (orders[0].personsCount != 0) {
                    details.text = detailText
                    details.setTextColor(ContextCompat.getColor(context, R.color.gray_20))
                } else {
                    details.makeVisible()
                    details.text = context.getString(R.string.no_person_count)
                    details.setTextColor(ContextCompat.getColor(context, R.color.gray))
                }

                phone.setOnClickListener {
                    itemPhoneClick(
                        orders[0].traveler.phone,
                        orders[0].status,
                        orders[0].id,
                        item.awareStartDt,
                        orders[0].reject.initiator
                    )
                }
                confirmOrderUser.setOnClickListener {
                    itemClickConfirmOrder(
                        orders[0].id,
                        item.experience.type,
                        item.experience.title,
                        item.status,
                        item.awareStartDt,
                        orders[0].reject.initiator
                    )
                }

                val chatInfo = OrderDetailsForChat(
                    orders[0].id,
                    orders[0].status,
                    item.experience.title,
                    "private",
                    binding.containerLocation.date.text.toString(),
                    item.awareStartDt.check24Hours(),
                    orders[0].traveler.phone,
                    orders[0].traveler.name,
                    orders[0].traveler.avatar150Url,
                    if (orders[0].personsCount != 0) detailTextFull else context.getString(
                        R.string.no_people_count_tour
                    ),
                    item.experience.price.currency,
                    orders[0].newMessagesCount != 0
                )

                message.setOnClickListener {
                    itemClickMessage(chatInfo, item.awareStartDt, orders[0].reject.initiator)
                }

                messageContent.setOnClickListener {
                    itemClickComment(chatInfo, item.awareStartDt, orders[0].reject.initiator)
                }
            }

        }
    }

    inner class ViewHolderGroupTour(private val binding: ItemGroupTourBinding) :
        BaseViewHolder<EventResults, ViewBinding>(binding) {
        private var orders = listOf<Order>()

        override fun onItemClick(item: EventResults) {
            itemClickGroupTour(item)
        }

        override fun bind(item: EventResults, context: Context) {
            orders =
                item.orders.paid + item.orders.pendingPayment + item.orders.confirmation + item.orders.cancelled + item.orders.messaging
            val ordersOnlyStatus = orders.map { it.status }
            val countConfirmationOrders = item.orders.confirmation.size

            with(binding) {

                containerGroup.confirmOrder.makeVisibleGone()
                if (item.experience.type == "group") {
                    containerGroup.type.text = context.getStringRes(R.string.group)
                    when {
                        item.date.isNotEmpty() && item.time.isEmpty() -> {
                            containerLocation.date.text = item.date.dateFormattingOnlyDate()
                        }

                        item.date.isEmpty() && item.time.isEmpty() -> {
                            containerLocation.date.text =
                                context.getString(R.string.no_date_and_time)
                            containerLocation.date.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.gray
                                )
                            )
                        }
                        else -> {
                            containerLocation.date.text =
                                (item.date + 'T' + item.time).dateFormattingWithHour(
                                    item.experience.duration
                                )
                            containerLocation.date.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.gray_20
                                )
                            )
                        }
                    }
                } else {
                    containerGroup.confirmOrder.isVisible =
                        ordersOnlyStatus.contains(Status.CONFIRM.value) || ordersOnlyStatus.contains(
                            Status.MESSAGING.value
                        )
                    containerGroup.type.text = context.getStringRes(R.string.tour)
                    if (countConfirmationOrders > 1)
                        containerGroup.confirmOrder.text =
                            context.getString(R.string.confirm_orders) else containerGroup.confirmOrder.text =
                        context.getString(R.string.confirm_order)
                    containerLocation.date.text = item.date.dateFormattingOnlyDate()
                }
            }

            with(binding.containerLocation) {
                if (item.status == Status.PAID.value && !item.awareStartDt.check24Hours()) {
                    status.background =
                        ContextCompat.getDrawable(context, R.drawable.shape_green_rectangle_4)
                    status.setTextColor(ContextCompat.getColor(context, R.color.white))
                } else {
                    status.background =
                        ContextCompat.getDrawable(context, R.drawable.shape_gray_rectangle_4)
                    status.setTextColor(ContextCompat.getColor(context, R.color.gray_20))
                }
                number.visibility = View.GONE
                location.text = item.experience.title
            }

            with(binding.containerGroup) {
                var pendingPayment = 0
                var paid = 0
                var waitingForConfirm = 0

                item.orders.messaging.forEach {
                    waitingForConfirm += it.personsCount
                }

                item.orders.confirmation.forEach {
                    waitingForConfirm += it.personsCount
                }

                item.orders.paid.forEach {
                    paid += it.personsCount
                }

                item.orders.pendingPayment.forEach {
                    pendingPayment += it.personsCount
                }

                if (pendingPayment != 0) {
                    notPayed.visibility = View.VISIBLE
                    countNotPaid.visibility = View.VISIBLE
                    countNotPaid.text = pendingPayment.toString()
                } else {
                    notPayed.visibility = View.GONE
                    countNotPaid.visibility = View.GONE
                }

                if (waitingForConfirm != 0) {
                    people.visibility = View.VISIBLE
                    countPeople.visibility = View.VISIBLE
                    countPeople.text = waitingForConfirm.toString()
                    binding.containerGroup.confirmOrder.setOnClickListener {
                        onItemClick(item)
                    }
                } else {
                    people.visibility = View.GONE
                    countPeople.visibility = View.GONE
                }

                if (paid != 0) {
                    alreadyPayed.visibility = View.VISIBLE
                    countPaid.visibility = View.VISIBLE
                    countPaid.text = context.getString(
                        R.string.details_group
                    ).setParticipantAndCurrency(
                        item.numberOfPersonsPaid.toString(),
                        item.experience.price.value.toInt(),
                        item.experience.price.currency.currency()
                    )
                } else {
                    alreadyPayed.visibility = View.GONE
                    countPaid.visibility = View.GONE
                }

                if (paid == 0 && pendingPayment == 0 && waitingForConfirm == 0) {
                    noPerson.visibility = View.VISIBLE
                    container.visibility = View.GONE
                } else {
                    noPerson.visibility = View.GONE
                    container.visibility = View.VISIBLE
                }
                binding.containerLocation.status.text =
                    when {
                        item.status == Status.CANCELLED.value -> context.getString(R.string.canceled_event)
                        item.awareStartDt.check24Hours() -> context.getString(R.string.ended)
                        item.status == Status.PAID.value -> context.getString(R.string.booking_is_closed)
                        else -> context.getString(
                            R.string.available_places,
                            item.numberOfPersonsAvail,
                            item.maxPersons
                        )
                    }

                var count = 0
                orders.forEach {
                    if (it.newMessagesCount != 0)
                        count++

                }

                if (count != 0) {
                    messageCount.text = count.toString()
                    messageCount.visibility = View.VISIBLE
                } else {
                    messageCount.visibility = View.GONE
                }
            }
        }
    }

    private fun checkPhone(phoneNumber: String): Boolean =
        Patterns.PHONE.matcher(phoneNumber)
            .matches() && phoneNumber.length >= 10

    private fun hideContacts(
        listOfWord: MutableList<String>,
        word: String,
        index: Int,
        booleanOfContacts: Boolean
    ): String {
        var closedWord = ""
        var string = ""
        var lengthOfWord = word.length
        if (booleanOfContacts) {
            while (lengthOfWord != 0) {

                closedWord = "$closedWord*"
                lengthOfWord--
            }
            listOfWord[index] = closedWord
        }

        listOfWord.forEach {
            string += "$it "
        }
        return string
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)?.experience?.type) {
            "private" -> VIEW_TYPE_PRIVATE_TOUR
            "group" -> VIEW_TYPE_GROUP_TOUR
            else -> VIEW_TYPE_TOUR
        }
    }

    companion object {
        const val VIEW_TYPE_PRIVATE_TOUR = 0
        const val VIEW_TYPE_GROUP_TOUR = 1
        const val VIEW_TYPE_TOUR = 2
    }
}