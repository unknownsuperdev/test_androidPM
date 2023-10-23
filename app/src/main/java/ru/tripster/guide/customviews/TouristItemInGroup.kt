package ru.tripster.guide.customviews

import android.content.Context
import android.util.AttributeSet
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.TextViewCompat
import com.bumptech.glide.Glide
import ru.tripster.domain.model.events.*
import ru.tripster.guide.R
import ru.tripster.guide.analytics.AnalyticsConstants.GROUP_EXP_CHAT_ICON
import ru.tripster.guide.analytics.AnalyticsConstants.GROUP_EXP_CHAT_PREVIEW
import ru.tripster.guide.databinding.IncludeOrderUserBinding
import ru.tripster.guide.extensions.*


class TouristItemInGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var userItemClick: (id: Int, touristId: Int, orderNumber: Int) -> Unit = { _, _, _ -> }
    private var confirmOrderClick: (id: Int, touristId: Int, orderNUmber: Int) -> Unit =
        { _, _, _ -> }
    private var messageClick: (Int, String, String, Traveler, String, Int) -> Unit =
        { _, _, _, _, _, _ -> }
    private var phoneClick: ((String, String, Int, Int) -> Unit) = { _, _, _, _ -> }
    private var awareStartDtValue = ""
    var ordersRateValue: Int = 0

    init {
        orientation = VERTICAL
    }

    fun setData(
        orders: SortedOrders,
        currency: String,
        type: String,
        awareStartDt: String,
        numberOfPaidPersons: Int
    ) {
        removeAllViews()
        awareStartDtValue = awareStartDt
        if (orders.confirmation.isNotEmpty() || orders.messaging.isNotEmpty()) messagingLogic(
            orders.confirmation + orders.messaging,
            currency,
            type
        )
        if (orders.pendingPayment.isNotEmpty()) pendingPaymentLogic(orders, currency)
        if (orders.paid.isNotEmpty()) paidLogic(orders, currency, numberOfPaidPersons)
        if (orders.cancelled.isNotEmpty()) cancelledLogic(orders, currency)
    }

    private fun pendingPaymentLogic(orders: SortedOrders, currency: String) {

        val pendingPaymentTextView = AppCompatTextView(context)
        var countOfTraveler = 0

        val params = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        params.setMargins(context.dpToPx(R.dimen.space_20), context.dpToPx(R.dimen.space_24), 0, 0)
        pendingPaymentTextView.layoutParams = params

        pendingPaymentTextView.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_credit_card_off,
            0,
            0,
            0
        )

        orders.pendingPayment.forEach {
            countOfTraveler += it.personsCount
        }
        pendingPaymentTextView.compoundDrawablePadding = context.dpToPx(R.dimen.space_8)
        TextViewCompat.setTextAppearance(pendingPaymentTextView, R.style.Text_17_700)
        pendingPaymentTextView.text = context?.getString(
            R.string.pending_payment_places,
            countOfTraveler.setCorrectForm()
        )
        addView(pendingPaymentTextView)
        orders.pendingPayment.forEach {
            userItemLogic(it, currency)
        }

    }

    private fun paidLogic(orders: SortedOrders, currency: String, numberOfPaidPersons: Int) {
        val paidTextView = AppCompatTextView(context)
        val params = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        var totalPrice = 0.0

        orders.paid.forEach {
            totalPrice += it.fullPrice
        }

        params.setMargins(context.dpToPx(R.dimen.space_20), context.dpToPx(R.dimen.space_24), 0, 0)
        paidTextView.layoutParams = params

        paidTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_icon, 0, 0, 0)
        paidTextView.compoundDrawablePadding = context.dpToPx(R.dimen.space_8)
        TextViewCompat.setTextAppearance(paidTextView, R.style.Text_17_700)
        paidTextView.text =
            context?.getString(
                R.string.paid_places
            )?.setParticipantAndCurrency(
                context.resources.getQuantityString(
                    R.plurals.places_count_tour,
                    numberOfPaidPersons,
                    numberOfPaidPersons
                ),
                totalPrice.toInt(),
                currency.currency()
            )
        addView(paidTextView)

        orders.paid.forEach {
            userItemLogic(it, currency)
        }
    }

    private fun cancelledLogic(orders: SortedOrders, currency: String) {
        val cancelledTextView = AppCompatTextView(context)

        val params = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        params.setMargins(context.dpToPx(R.dimen.space_20), context.dpToPx(R.dimen.space_24), 0, 0)
        cancelledTextView.layoutParams = params

        TextViewCompat.setTextAppearance(cancelledTextView, R.style.Text_17_700)
        cancelledTextView.text = context?.getString(R.string.canceled_title)
        addView(cancelledTextView)

        orders.cancelled.forEach {
            userItemLogic(it, currency)
        }
    }

    private fun messagingLogic(orders: List<Order>, currency: String, type: String) {
        val messagingTextView = AppCompatTextView(context)
        var countOfTraveler = 0
        val params = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )

        params.setMargins(context.dpToPx(R.dimen.space_20), context.dpToPx(R.dimen.space_24), 0, 0)
        messagingTextView.layoutParams = params

        messagingTextView.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_emoji_people,
            0,
            0,
            0
        )
        orders.forEach {
            countOfTraveler += it.personsCount
        }
        messagingTextView.compoundDrawablePadding = context.dpToPx(R.dimen.space_8)
        TextViewCompat.setTextAppearance(messagingTextView, R.style.Text_17_700)
        messagingTextView.text = if (orders.size > 1) context?.getString(
            R.string.waiting_for_confirm_places,
            countOfTraveler.setCorrectForm(),
        ) else context?.getString(
            R.string.waiting_for_confirmation_tour,
            countOfTraveler.setCorrectForm(),
        )
        addView(messagingTextView)

        orders.forEach {
            userItemLogic(it, currency, type)
        }

    }

    private fun userItemLogic(order: Order, currency: String, type: String? = null) {
        val userItemBinding: IncludeOrderUserBinding =
            IncludeOrderUserBinding.inflate(LayoutInflater.from(context), this, false)

        val params = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, context.dpToPx(R.dimen.space_14), 0, 0)
        userItemBinding.root.layoutParams = params

        with(userItemBinding) {
            if (order.traveler.avatar150Url.isNotEmpty()) {
                avatar.setStrokeColorResource(R.color.ten_percent_of_black)
                avatar.strokeWidth = 2f
            }
            name.text = order.traveler.name.shortedSurname()
            Glide.with(context).load(order.traveler.avatar150Url)
                .placeholder(R.drawable.ic_avatar_icon_placeholder)
                .centerCrop()
                .into(avatar)

            val detailTextFull = context.getString(
                R.string.details
            ).setParticipantAndCurrency(
                context.resources.getQuantityString(
                    R.plurals.people_count_tour,
                    order.personsCount,
                    order.personsCount
                ),
                order.fullPrice.toInt(), currency.currency()
            )


            message.setOnClickListener {
                messageClick(
                    order.id,
                    order.status,
                    if (order.personsCount != 0) detailTextFull else context.getString(R.string.no_people_count_tour),
                    order.traveler,
                    GROUP_EXP_CHAT_ICON,
                    order.id
                )
            }

            messageContent.setOnClickListener {
                messageClick(
                    order.id,
                    order.status,
                    if (order.personsCount != 0) detailTextFull else context.getString(R.string.no_people_count_tour),
                    order.traveler,
                    GROUP_EXP_CHAT_PREVIEW,
                    order.id
                )
            }

            if (order.lastMessage.comment.isNotEmpty()) {
                val listOfWord = order.lastMessage.comment.split(" ").toMutableList()
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

            if (order.newMessagesCount > 0)
                messageCount.apply {
                    text = order.newMessagesCount.toString()
                    makeVisible()
                }

            if ((order.status == Status.MESSAGING.value || order.status == Status.CONFIRM.value) && (type == EventTypes.TOUR.type || type == EventTypes.TICKET.type)) {
                confirmOrderUser.apply {
                    setOnClickListener {
                        confirmOrderClick(order.id, order.traveler.id, order.id)
                    }
                    makeVisible()
                }
            } else {
                confirmOrderUser.makeVisibleGone()
            }

            when {
                order.status == Status.PAID.value && awareStartDtValue.check24Hours() -> phone.isVisible =
                    false
                order.status == Status.CANCELLED.value -> phone.isVisible = false
                else -> phone.isVisible = true
            }

            phone.setOnClickListener {
                phoneClick(order.status, order.traveler.phone, order.traveler.id, order.id)
            }

            if (order.personsCount != 0) {
                val detailText =
                    if (order.status == Status.CANCELLED.value)
                        context.getString(
                            R.string.details_member_price_shortened,
                            order.personsCount
                        )
                    else context.getString(
                        R.string.details_member_price,
                        order.personsCount,
                        order.fullPrice.toInt(),
                        currency.currency()
                    )

                details.text = detailText

                details.setTextColor(ContextCompat.getColor(context, R.color.gray_20))
            } else {
                details.text = context.getString(R.string.no_person_count)
                details.setTextColor(ContextCompat.getColor(context, R.color.gray))
            }
        }

        userItemBinding.root.setOnClickListener {
            userItemClick(order.id, order.traveler.id, order.id)
        }

        addView(userItemBinding.root)
    }

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

    private fun checkPhone(phoneNumber: String): Boolean =
        Patterns.PHONE.matcher(phoneNumber)
            .matches() && phoneNumber.length >= 10

    fun setOnUserItemClickListener(userItemClick: (id: Int, touristId: Int, orderNumber: Int) -> Unit) {
        this.userItemClick = userItemClick
    }

    fun setOnConfirmClickListener(confirmOrderClick: (id: Int, touristId: Int, orderNumber: Int) -> Unit) {
        this.confirmOrderClick = confirmOrderClick
    }

    fun setOnMessageClickListener(messageClick: (id: Int, status: String, details: String, traveler: Traveler, amplitudeId: String, orderNumber: Int) -> Unit) {
        this.messageClick = messageClick
    }

    fun setOnPhoneClickListener(phoneClick: (String, String, Int, Int) -> Unit) {
        this.phoneClick = phoneClick
    }
}