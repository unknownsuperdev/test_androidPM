package ru.tripster.guide.ui.fragments.calendar.orders

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import ru.tripster.domain.model.calendar.DateOrder
import ru.tripster.domain.model.calendar.DateScheduleItem
import ru.tripster.domain.model.calendar.filtering.EventScheduleType
import ru.tripster.domain.model.events.EventTypes
import ru.tripster.domain.model.events.OrderDetailsForChat
import ru.tripster.domain.model.events.Status
import ru.tripster.guide.R
import ru.tripster.guide.appbase.adapter.BaseAdapter
import ru.tripster.guide.appbase.adapter.BaseViewHolder
import ru.tripster.guide.databinding.ItemCalendarDateOrderTypeBinding
import ru.tripster.guide.databinding.ItemGroupTourBinding
import ru.tripster.guide.databinding.ItemPrivateTourBinding
import ru.tripster.guide.extensions.*

class DateOrderAdapter(
    val itemClickIndividual: (DateScheduleItem) -> Unit,
    val itemClickGroupTour: (DateScheduleItem) -> Unit,
    val itemClickConfirmOrder: (Int, String, String) -> Unit,
    val itemClickMessage: (OrderDetailsForChat, String) -> Unit,
    val itemClickComment: (OrderDetailsForChat) -> Unit,
    val itemPhoneClick: (String, String, String, Int) -> Unit,
    val itemClickToDelete: (Int) -> Unit,
) :
    BaseAdapter<ViewBinding, DateScheduleItem, BaseViewHolder<DateScheduleItem, ViewBinding>>() {

    val date: (String, Int) -> Unit = { date, rateValue ->
        dateOfBusyType = date
        ordersRateValue = rateValue
    }
    private var dateOfBusyType: String = ""
    private var ordersRateValue: Int = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<DateScheduleItem, ViewBinding> {
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
            VIEW_TYPE_GROUP_TOUR -> {
                ViewHolderGroupTour(
                    ItemGroupTourBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                ViewHolderBusy(
                    ItemCalendarDateOrderTypeBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    inner class ViewHolderPrivateTour(private val binding: ItemPrivateTourBinding) :
        BaseViewHolder<DateScheduleItem, ViewBinding>(binding) {
        @SuppressLint("UseCompatLoadingForDrawables")
        private var booleanOfContacts = false
        override fun onItemClick(item: DateScheduleItem) {
            itemClickIndividual(
                item
            )
        }

        override fun bind(item: DateScheduleItem, context: Context) {
            with(binding.containerLocation) {
                item.eventData.status.statusType(
                    status,
                    context,
                    item.eventData.awareStartDt,
                    "",
                    false,
                    context.getString(R.string.date_order_type), 1.0f
                )
                location.text = item.eventData.experience.title
                if (item.eventData.orders.isNotEmpty()) {
                    item.eventData.orders[0].id.let {
                        number.text = context.getString(R.string.number, it)
                    }
                }
                when {
                    item.eventData.date.isNotEmpty() && item.eventData.time.isEmpty() -> {
                        date.text =
                            item.eventData.date.dateFormattingOnlyDate()
                    }
                    item.eventData.date.isEmpty() && item.time.isEmpty() -> {
                        date.text = context.getString(R.string.no_date_and_time)
                        date.setTextColor(ContextCompat.getColor(context, R.color.gray))
                    }
                    else -> {
                        if (item.eventData.date.isNotEmpty()) date.text =
                            (item.eventData.date + 'T' + item.time).dateFormattingWithHour(
                                item.eventData.experience.duration
                            )
                        date.setTextColor(ContextCompat.getColor(context, R.color.gray_20))
                    }
                }


            }
            with(binding.orderUser) {
                if (item.eventData.orders.isNotEmpty()) {
                    when {
                        item.eventData.status == Status.PAID.value && item.eventData.awareStartDt.check24Hours() -> phone.isVisible =
                            false
                        item.eventData.orders[0].traveler.phone.isNotEmpty() -> phone.isVisible =
                            true
                        item.eventData.orders[0].traveler.phone.isEmpty() -> phone.isVisible = false
                    }
                    confirmOrderUser.isVisible =
                        item.eventData.orders[0].status == Status.CONFIRM.value || item.eventData.orders[0].status == Status.MESSAGING.value

                    if (item.eventData.orders[0].traveler.avatar150Url.isNotEmpty()) {
                        avatar.setStrokeColorResource(R.color.ten_percent_of_black)
                        avatar.strokeWidth = 2f
                    }

                    Glide.with(context).load(item.eventData.orders[0].traveler.avatar150Url)
                        .placeholder(R.drawable.ic_avatar_icon_placeholder).centerCrop()
                        .into(avatar)

                    name.text = item.eventData.orders[0].traveler.name.shortedSurname()

                    if (item.eventData.orders[0].newMessagesCount != 0) {
                        messageCount.text = item.eventData.orders[0].newMessagesCount.toString()
                        messageCount.isVisible = true
                    } else {
                        messageCount.isVisible = false
                    }

                    if (item.eventData.orders[0].lastMessage.comment.isNotEmpty()) {
                        if (ordersRateValue < 60) {
                            val listOfWord = item.eventData.orders[0].lastMessage.comment.split(" ")
                                .toMutableList()
                            listOfWord.forEachIndexed { index, word ->
                                word.replace("-", "")
                                if (checkPhone(word)) {
                                    booleanOfContacts = true
                                    hideContacts(listOfWord, word, index, booleanOfContacts).let {
                                        messageContent.setResizableText(
                                            it,
                                            3,
                                            true
                                        )
                                    }
                                    messageContent.makeVisible()
                                    exclude.makeVisible()
                                } else {
                                    booleanOfContacts = false
                                    hideContacts(listOfWord, word, index, booleanOfContacts).let {
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
                            item.eventData.orders[0].lastMessage.comment.let {
                                messageContent.setResizableText(
                                    it,
                                    3,
                                    true
                                )
                            }
                            messageContent.makeVisible()
                            exclude.makeVisible()
                        }

                    } else {
                        messageContent.makeVisibleGone()
                        exclude.makeVisibleGone()

                    }

                    val price =
                        if (item.eventData.orders[0].fullPrice != 0) item.eventData.orders[0].fullPrice else item.eventData.experience.price.value.toInt()

                    val detailText = context.getString(
                        R.string.details_member_price,
                        item.eventData.orders[0].personsCount,
                        price,
                        item.eventData.experience.price.currency.currency()
                    )

                    val detailTextFull = context.getString(
                        R.string.details
                    ).setParticipantAndCurrency(
                        context.resources.getQuantityString(
                            R.plurals.people_count_tour,
                            item.eventData.orders[0].personsCount,
                            item.eventData.orders[0].personsCount
                        ),
                        price, item.eventData.experience.price.currency.currency()
                    )

                    if (item.eventData.orders[0].personsCount != 0) {
                        details.text = detailText
                        details.setTextColor(ContextCompat.getColor(context, R.color.gray_20))
                    } else {
                        details.makeVisible()
                        details.text = context.getString(R.string.no_person_count)
                        details.setTextColor(ContextCompat.getColor(context, R.color.gray))
                    }
                    confirmOrderUser.setOnClickListener {
                        itemClickConfirmOrder(
                            item.eventData.orders[0].id,
                            item.eventData.experience.type,
                            item.eventData.experience.title
                        )
                    }
                    val chatInfo = OrderDetailsForChat(
                        item.eventData.orders[0].id,
                        item.eventData.orders[0].status,
                        item.eventData.experience.title,
                        "private",
                        binding.containerLocation.date.text.toString(),
                        item.eventData.awareStartDt.check24Hours(),
                        item.eventData.orders[0].traveler.phone,
                        item.eventData.orders[0].traveler.name,
                        item.eventData.orders[0].traveler.avatar150Url,
                        if (item.eventData.orders[0].personsCount != 0) detailTextFull else context.getString(
                            R.string.no_people_count_tour
                        ),
                        item.eventData.experience.price.currency,
                        item.eventData.orders[0].newMessagesCount != 0
                    )

                    message.setOnClickListener {
                        itemClickMessage(chatInfo, item.eventData.awareStartDt)
                    }
                    messageContent.setOnClickListener {
                        itemClickComment(chatInfo)
                    }
                    phone.setOnClickListener {
                        itemPhoneClick(
                            item.eventData.orders[0].traveler.phone,
                            item.eventData.orders[0].status,
                            item.eventData.awareStartDt,
                            item.eventData.orders[0].id
                        )
                    }
                }
            }

        }

    }

    inner class ViewHolderGroupTour(private val binding: ItemGroupTourBinding) :
        BaseViewHolder<DateScheduleItem, ViewBinding>(binding) {
        override fun onItemClick(item: DateScheduleItem) {
            itemClickGroupTour(item)
        }

        private var orders = listOf<DateOrder>()
        override fun bind(item: DateScheduleItem, context: Context) {
            val paid = item.eventData.orders.filter { it.status == Status.PAID.value }
            val confirm = item.eventData.orders.filter { it.status == Status.CONFIRM.value }
            val pendingPayment =
                item.eventData.orders.filter { it.status == Status.PENDING_PAYMENT.value }
            val cancelled = item.eventData.orders.filter { it.status == Status.CANCELLED.value }
            val messaging = item.eventData.orders.filter { it.status == Status.MESSAGING.value }

            orders = paid + pendingPayment + confirm + cancelled + messaging

            val ordersOnlyStatus = orders.map { it.status }
            val countConfirmationOrders = confirm.size

            with(binding) {

                containerGroup.confirmOrder.makeVisibleGone()
                if (item.eventData.experience.type == "group") {
                    containerGroup.type.text = context.getStringRes(R.string.group)
                    when {
                        item.eventData.date.isNotEmpty() && item.time.isEmpty() -> {
                            containerLocation.date.text =
                                item.eventData.date.dateFormattingOnlyDate()
                        }

                        item.eventData.date.isEmpty() && item.time.isEmpty() -> {
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
                                (item.eventData.date + 'T' + item.time).dateFormattingWithHour(
                                    item.eventData.experience.duration
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
                    containerLocation.date.text = item.eventData.date.dateFormattingOnlyDate()
                }
            }

            with(binding.containerLocation) {
                if (item.eventData.status == Status.PAID.value && !item.eventData.awareStartDt.check24Hours()) {
                    status.background =
                        ContextCompat.getDrawable(context, R.drawable.shape_green_rectangle_4)
                    status.setTextColor(ContextCompat.getColor(context, R.color.white))
                } else {
                    status.background =
                        ContextCompat.getDrawable(context, R.drawable.shape_gray_rectangle_4)
                    status.setTextColor(ContextCompat.getColor(context, R.color.gray_20))
                }
                number.visibility = View.GONE
                location.text = item.eventData.experience.title
            }

            with(binding.containerGroup) {
                var pendingPaymentCountOrders = 0
                var paidCountOrders = 0
                var waitingForConfirm = 0

                messaging.forEach {
                    waitingForConfirm += it.personsCount
                }

                confirm.forEach {
                    waitingForConfirm += it.personsCount
                }

                paid.forEach {
                    paidCountOrders += it.personsCount
                }

                pendingPayment.forEach {
                    pendingPaymentCountOrders += it.personsCount
                }

                if (pendingPaymentCountOrders != 0) {
                    notPayed.visibility = View.VISIBLE
                    countNotPaid.visibility = View.VISIBLE
                    countNotPaid.text = pendingPaymentCountOrders.toString()
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

                if (paidCountOrders != 0) {
                    alreadyPayed.visibility = View.VISIBLE
                    countPaid.visibility = View.VISIBLE
                    countPaid.text = context.getString(
                        R.string.details_group
                    ).setParticipantAndCurrency(
                        item.eventData.numberOfPersonsPaid.toString(),
                        item.eventData.experience.price.value.toInt(),
                        item.eventData.experience.price.currency.currency()
                    )
                } else {
                    alreadyPayed.visibility = View.GONE
                    countPaid.visibility = View.GONE
                }

                if (paidCountOrders == 0 && pendingPaymentCountOrders == 0 && waitingForConfirm == 0) {
                    noPerson.visibility = View.VISIBLE
                    container.visibility = View.GONE
                } else {
                    noPerson.visibility = View.GONE
                    container.visibility = View.VISIBLE
                }
                binding.containerLocation.status.text =
                    when {
                        item.eventData.status == Status.CANCELLED.value -> context.getString(R.string.canceled_event)
                        item.eventData.awareStartDt.check24Hours() -> context.getString(R.string.ended)
                        item.eventData.status == Status.PAID.value -> context.getString(R.string.booking_is_closed)
                        else -> context.getString(
                            R.string.available_places,
                            item.eventData.numberOfPersonsAvail,
                            item.eventData.maxPersons
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

    inner class ViewHolderBusy(private val binding: ItemCalendarDateOrderTypeBinding) :
        BaseViewHolder<DateScheduleItem, ViewBinding>(binding) {
        override fun bind(item: DateScheduleItem, context: Context) {
            with(binding) {

                dateOfBusyType = item.date.ifEmpty { dateOfBusyType }

                when {
                    dateOfBusyType.isNotEmpty() && item.time.isEmpty() -> {
                        date.text = dateOfBusyType
                    }
                    dateOfBusyType.isEmpty() && item.time.isEmpty() -> {
                        date.text = context.getString(R.string.no_date_and_time)
                    }
                    else -> {
                        when (item.duration) {
                            1439 -> {
                                date.text =
                                    (dateOfBusyType + 'T' + item.time).dateFormattingWithHour(
                                        item.duration.toDouble() + 0.99
                                    )
                            }
                            1440 -> {
                                date.text =
                                    (dateOfBusyType + 'T' + item.time).dateFormattingWithHour(
                                        item.duration.toDouble() - 0.01
                                    )
                            }
                            else -> {
                                date.text =
                                    (dateOfBusyType + 'T' + item.time).fromDurationToHour(
                                        item.duration
                                    )
                            }

                        }
                    }
                }

                descClosedTime.text =
                    if (item.experienceName.isEmpty()) context.getString(R.string.closed_time_for_all_offers) else context.getString(
                        R.string.closed_time_for_offer,
                        item.experienceName
                    )

                context.getDrawable(R.drawable.ic_lock_container)
                    ?.let {
                        title.setIconAndText(
                            it, item.comment.ifEmpty {
                                context.getString(R.string.time_is_closed)
                            }
                        )
                    }

                icDelete.isVisible = item.date.isEmpty()

                icDelete.setOnClickListener {
                    itemClickToDelete(item.id.toInt())
                    icDelete.isVisible = false
                    progress.isVisible = true
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
        return when {
            getItem(position)?.type == EventScheduleType.BUSY.type -> BUSY_TYPE
            getItem(position)?.eventData?.isForGroups == false -> VIEW_TYPE_PRIVATE_TOUR
            else -> VIEW_TYPE_GROUP_TOUR
        }
    }


    companion object {
        const val VIEW_TYPE_PRIVATE_TOUR = 0
        const val VIEW_TYPE_GROUP_TOUR = 1
        const val BUSY_TYPE = 2
    }
}