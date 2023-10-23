package ru.tripster.guide.ui.fragments.confirmOrder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.tripster.domain.model.calendar.DateScheduleItem
import ru.tripster.domain.model.confirmOrEdit.OrderConfirmOrEditData
import ru.tripster.domain.model.confirmOrEdit.Ticket
import ru.tripster.domain.model.events.Status
import ru.tripster.domain.model.order.ChooseTime
import ru.tripster.domain.model.order.PerTicketOrderDetail
import ru.tripster.guide.Constants
import ru.tripster.guide.R
import ru.tripster.guide.Screen
import ru.tripster.guide.analytics.AnalyticsConstants.USER_TAP_ORDER_CHANGE_SUBMIT_BUTTON
import ru.tripster.guide.analytics.AnalyticsConstants.USER_TAP_ORDER_CONFIRM_SUBMIT_BUTTON
import ru.tripster.guide.appbase.FragmentBaseNCMVVM
import ru.tripster.guide.appbase.utils.bottomNavBarVisibility
import ru.tripster.guide.appbase.utils.viewBinding
import ru.tripster.guide.databinding.FragmentOrderConfirmationBinding
import ru.tripster.guide.extensions.*
import ru.tripster.guide.ui.MainActivity
import ru.tripster.guide.ui.calendar.CalendarBottomSheet
import ru.tripster.guide.ui.fragments.confirmOrder.EditPriceBottomSheet.Companion.CONFIRM_OR_EDIT


class ConfirmOrEditOrderFragment :
    FragmentBaseNCMVVM<ConfirmOrEditOrderViewModel, FragmentOrderConfirmationBinding>() {
    override val viewModel: ConfirmOrEditOrderViewModel by viewModel()
    override val binding: FragmentOrderConfirmationBinding by viewBinding()
    private val args: ConfirmOrEditOrderFragmentArgs by navArgs()
    private val ticketAdapter = TicketsAdapter { item, isAdded ->
        viewModel.modifyTickets(item, isAdded)
        if (!viewModel.hasCustomPrice && viewModel.pricingModel != PricingModel.PER_GROUP.pricingModel) countingPrice(
            true
        )
    }

    companion object {
        const val CONFIRM_ORDER_REQUEST_KEY = "CONFIRM_ORDER_REQUEST_KEY"
        const val CONFIRM_ORDER_BUNDLE = "CONFIRM_ORDER_BUNDLE"
        const val IS_CONFIRMED_KEY = "IS_CONFIRMED_KEY"
        const val IS_CONFIRMED_BUNDLE = "IS_CONFIRMED_BUNDLE"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.onBackPressedDispatcher?.addCallback(this) {
            popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.emailError = true
        viewModel.phoneError = true
        viewModel.meetingPlaceError = true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onView() {
        activity?.bottomNavBarVisibility(false)

        with(activity) {
            this?.setLightStatusBar()
            this?.window?.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
            )
        }
        context?.getString(
            R.string.order_number_and_place,
            args.orderId,
            args.experienceTitle
        )
            ?.let { binding.toolbar.setSubTitle(it) }

        if (context?.isOnline() == true) {
            viewModel.getOrderDetails(args.orderId)
        } else {
            errorMessage(false)
        }

        with(binding) {
            tripDate.doOnTextChanged { text, _, _, _ ->
                checkDateAndTime(text.toString(), tripHour.text.toString())
            }
            tripHour.doOnTextChanged { text, _, _, _ ->
                checkDateAndTime(tripDate.text.toString(), text.toString())
            }

            setFragmentResultListener(CalendarBottomSheet.CHOOSE_DATE_BUNDLE) { _, bundle ->
                viewModel.selectedDate =
                    bundle.getString(CalendarBottomSheet.CHOOSE_DATE_REQUEST_KEY) ?: ""
                viewModel.modifyTickets(null, false)
                if (!viewModel.hasCustomPrice) countingPrice(true)

                tripDate.text = viewModel.selectedDate.dateFormattingOnlyDate()
                tripDate.setBackgroundResource(R.drawable.shape_stroke_gray_left_8)
                viewModel.closedTimes =
                    bundle.get(CalendarBottomSheet.CLOSED_TIME_REQUEST_KEY) as List<DateScheduleItem>
            }

            setFragmentResultListener(EditPriceBottomSheet.NEW_FIXED_PRICE_REQUEST_KEY) { _, bundle ->
                val result = bundle.getInt(EditPriceBottomSheet.NEW_FIXED_PRICE_BUNDLE)
                viewModel.hasCustomPrice = true
                viewModel.modifyTickets(null, false, result.toDouble())
            }

            activity?.supportFragmentManager?.setFragmentResultListener(
                ChooseTimeBottomSheet.CHOOSE_TIME_REQUEST_KEY,
                viewLifecycleOwner
            ) { _, bundle ->
                val result =
                    bundle.getParcelable<ChooseTime>(ChooseTimeBottomSheet.CHOOSE_TIME_BUNDLE)
                viewModel.tourTime = result?.time.toString()
                viewModel.modifyTickets(null, false, time = viewModel.tourTime)
                if (!viewModel.hasCustomPrice) countingPrice(true)

                binding.tripHour.text = viewModel.tourTime
                binding.tripHour.setBackgroundResource(R.drawable.shape_stroke_gray_right_8)
            }

            if (!args.isConfirm) {
                editDesc.apply {
                    setMargins(R.dimen.space_16, R.dimen.space_8, R.dimen.space_16, null)
                    makeVisible()
                }
                context?.getString(R.string.editing_order)?.let { toolbar.setTitleText(it) }
                context?.dpToPx(R.dimen.space_18)?.let { hideOptionsContainer(it) }
                confirm.text = context?.getString(R.string.save_edits)
                dateAndTime.setMargins(R.dimen.space_16, R.dimen.space_32, R.dimen.space_16, null)
            } else if (args.type == context?.getString(R.string.tour_type)) {
                context?.dpToPx(R.dimen.space_18)?.let { hideOptionsContainer(it) }
            }

            container.viewTreeObserver.addOnGlobalLayoutListener {
                if (isAdded) {
                    val rect = Rect()
                    container.getWindowVisibleDisplayFrame(rect)
                    val screenHeight: Int = container.rootView.height
                    if (!viewModel.isVisibleKeyBoard) {
                        viewModel.keypadHeight = screenHeight - rect.bottom
                    }
                    if (viewModel.keypadHeight < screenHeight * 0.15) {
                        meetingPlace.isFocusable = false
//                        email.isFocusable = false
//                        phoneNumber.isFocusable = false

//                        Log.i("Dasff", "onView: ${viewModel.emailError} ${viewModel.phoneValidError}")
//                        if (viewModel.emailError) {
//                            email.elevation = phoneNumber.elevation + 1
//                        } else if (viewModel.phoneValidError) phoneNumber.elevation =
//                            email.elevation + 1
                    } else {
                        viewModel.isVisibleKeyBoard = false
                    }
                }
            }
        }

        binding.optionsContainer.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                1 -> {
                    binding.timeOfExcursionRB.isChecked = true
                    viewModel.autoSetDayBusy = false
                }
                2 -> {
                    binding.timeOfExcursionRB.isChecked = false
                    viewModel.autoSetDayBusy = true
                }
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.orderDetails) { orderDetails ->
            viewModel.pricingModel = orderDetails?.experience?.pricingModel ?: ""
            viewModel.hasCustomPrice = orderDetails?.hasCustomPrice ?: false
            viewModel.priceValue = orderDetails?.price?.value ?: 0.0
            viewModel.selectedDate = orderDetails?.event?.date ?: ""
            viewModel.status = orderDetails?.status ?: ""
            viewModel.currency = orderDetails?.price?.currency.toString().currency()

            checkType(args.type, orderDetails?.status ?: "")

            with(binding) {
                shimmer.makeVisibleGone()
                nestedScrollView.makeVisible()

                viewModel.meetingPlace = orderDetails?.event?.meetingPoint?.description.toString()
                meetingPlace.setText(viewModel.meetingPlace)

                viewModel.email = orderDetails?.event?.email.toString()
                email.setText(viewModel.email)

                viewModel.phone = orderDetails?.event?.phone.toString()
                phoneNumber.setText(viewModel.phone)

                membersCount.text =
                    context?.getString(R.string.max_person_count, orderDetails?.event?.maxPersons)
                if (!orderDetails?.event?.date.isNullOrEmpty()) {
                    tripDate.text = orderDetails?.event?.date?.dateFormattingOnlyDate()
                } else {
                    tripDate.text = orderDetails?.event?.date
                }

                setPriceValues(
                    orderDetails?.price?.value ?: 0.0,
                    orderDetails?.price?.prePay ?: 0.0,
                    orderDetails?.price?.paymentToGuide ?: 0.0
                )

                viewModel.tourTime = orderDetails?.event?.time.toString()
                tripHour.text = viewModel.tourTime
                edit.isVisible = orderDetails?.status != Status.PAID.value
                viewModel.isPaid = orderDetails?.status == Status.PAID.value
            }
        }

        onEach(viewModel.orderDetailsError) {
            errorMessage(true)
        }

        onEach(viewModel.changedExperience) {
            setPriceValues(it?.value ?: 0.0, it?.prePay ?: 0.0, it?.paymentToGuide ?: 0.0)
            countingPrice(false)
            viewModel.priceValue = it?.value ?: 0.0
        }

        onEach(viewModel.totalCount) {
            binding.containerPrice.isVisible = it != 0
            viewModel.totalMembersCount = it
        }

        onEach(viewModel.selectedTickets) { list ->

            if (list != null) {
                viewModel.selectedTicketsList = list
            }

            with(binding) {
                rvTickets.apply {
                    context?.let {
                        layoutManager = LinearLayoutManager(it)
                        itemAnimator = null
                        ticketAdapter.submitList(list)
                        adapter = ticketAdapter
                    }
                }
            }
        }

        onEach(viewModel.confirmOrder) {
            setFragmentResult(
                CONFIRM_ORDER_REQUEST_KEY,
                bundleOf(CONFIRM_ORDER_BUNDLE to (it?.apply { isConfirm = args.isConfirm }))
            )
            setFragmentResult(
                IS_CONFIRMED_KEY, bundleOf(
                    IS_CONFIRMED_BUNDLE to true
                )
            )
            popBackStack()
        }
        onEach(viewModel.confirmOrderError) {

            if (it?.contains("Укажите телефон в международном формате") == true) {
                viewModel.phoneValidError = true
                phoneLogic()
            } else {
                context?.getStringRes(R.string.try_again_later)
                    ?.let { text ->
                        Toast(context).showCustomToast(
                            text,
                            this@ConfirmOrEditOrderFragment,
                            false
                        )
                    }
            }

            with(binding) {
                progressBar.makeVisibleGone()
                confirm.isClickable = true
                confirm.text =
                    if (!args.isConfirm) context?.getString(R.string.save_edits) else context?.getString(
                        R.string.confirm_order
                    )
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewClick() {

        with(binding) {
            meetingPlace.setOnTouchListener { view, event ->
                view.parent.requestDisallowInterceptTouchEvent(true)
                if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                    view.parent.requestDisallowInterceptTouchEvent(false)
                }
                return@setOnTouchListener false
            }
            container.setOnClickListener {
                activity?.hideSoftKeyboard(meetingPlace)
                activity?.hideSoftKeyboard(email)
                activity?.hideSoftKeyboard(phoneNumber)
            }
            meetingPlace.setOnClickListener {
                activity?.showSoftKeyboard(meetingPlace)
            }
            email.setOnClickListener {
                activity?.showSoftKeyboard(email)
            }
            phoneNumber.setOnClickListener {
                activity?.showSoftKeyboard(phoneNumber)
            }
            meetingPlace.setOnFocusChangeListener { view, isFocused ->
                when {
                    isFocused -> {
                        if (viewModel.meetingPlaceError) {
                            view.setBackgroundResource(R.drawable.shape_rectangle_click_shadow)
                        } else {
                            view.setBackgroundResource(
                                R.drawable.shape_rectangle_click_error_shadow
                            )
                        }
                    }
                    else -> {
                        if (viewModel.meetingPlaceError) {
                            view.setBackgroundResource(R.drawable.shape_stroke_gray)
                        } else {
                            view.setBackgroundResource(R.drawable.shape_stroke_tomat)
                        }
                    }
                }
            }
            email.setOnFocusChangeListener { view, isFocused ->
                with(email) {
                    when {
                        isFocused && viewModel.emailError -> {
                            viewModel.keypadHeight = 944
                            viewModel.isVisibleKeyBoard = true
                            elevation = if (viewModel.phoneError) {
                                setBorders(
                                    R.drawable.shape_rectangle_top_click_shadow,
                                    R.drawable.shape_stroke_bottom_radius
                                )
                                phoneNumber.elevation + 1
                            } else {
                                setBorders(
                                    R.drawable.shape_rectangle_top_click_shadow,
                                    R.drawable.shape_bottom_radius_password_error
                                )
                                phoneNumber.elevation + 1
                            }
                        }
                        isFocused && !viewModel.emailError -> {
                            elevation = if (viewModel.phoneError) {
                                setBorders(
                                    R.drawable.shape_username_tomat_80_shadow,
                                    R.drawable.shape_stroke_bottom_radius
                                )
                                phoneNumber.elevation + 1
                            } else {
                                setBorders(
                                    R.drawable.shape_username_tomat_80_shadow,
                                    R.drawable.shape_bottom_radius_password_error
                                )
                                phoneNumber.elevation + 1
                            }
                        }
                        else -> {
                            viewModel.isVisibleKeyBoard = false
                            if (viewModel.emailError)
                                view.setBackgroundResource(R.drawable.shape_stroke_top_radius)
                            else view.setBackgroundResource(R.drawable.shape_stroke_top_radius_error)
                        }
                    }
                }

            }
            phoneNumber.setOnFocusChangeListener { view, isFocused ->
                with(phoneNumber) {
                    when {
                        isFocused && viewModel.phoneError -> {
                            viewModel.isVisibleKeyBoard = true
                            viewModel.keypadHeight = 944
                            elevation = if (viewModel.emailError) {
                                setBorders(
                                    R.drawable.shape_stroke_top_radius,
                                    R.drawable.shape_rectangle_bottom_click_shadow
                                )
                                email.elevation + 1

                            } else {
                                setBorders(
                                    R.drawable.shape_top_radius_username_error,
                                    R.drawable.shape_rectangle_bottom_click_shadow
                                )
                                email.elevation + 1
                            }
                        }
                        isFocused && !viewModel.phoneError -> {
                            elevation = if (viewModel.emailError) {
                                setBorders(
                                    R.drawable.shape_stroke_top_radius,
                                    R.drawable.shape_password_tomat_80_shadow
                                )
                                email.elevation + 1
                            } else {
                                setBorders(
                                    R.drawable.shape_top_radius_username_error,
                                    R.drawable.shape_password_tomat_80_shadow
                                )
                                email.elevation + 1
                            }
                        }
                        else -> {
                            viewModel.isVisibleKeyBoard = false
                            if (viewModel.phoneError)
                                view.setBackgroundResource(R.drawable.shape_stroke_bottom_radius)
                            else view.setBackgroundResource(R.drawable.shape_stroke_bottom_radius_error)

                        }
                    }
                }
            }

            tripDate.setOnClickListener {
                navigateFragment(
                    ConfirmOrEditOrderFragmentDirections.actionConfirmOrderFragmentToCalendarFragment(
                        emptyArray(), "confirmOrEdit", viewModel.selectedDate
                    )
                )
            }

            tripHour.setOnClickListener {
                (activity as MainActivity).navController.navigate(
                    Screen.ChooseTime.show(
                        viewModel.tourTime,
                        false,
                        "08:00",
                        "", ""
                    )
                )
            }

            confirm.setOnClickListener {
                context?.let {
                    viewModel.confirmOrEditButtonClicked(
                        if (args.isConfirm) USER_TAP_ORDER_CONFIRM_SUBMIT_BUTTON else USER_TAP_ORDER_CHANGE_SUBMIT_BUTTON,
                        it,
                        args.type
                    )
                }

                if (context?.isOnline() == true)
                    confirmOrEditOrder()
                else {
                    context?.getStringRes(R.string.no_internet_toast)
                        ?.let {
                            Toast(context).showCustomToast(
                                it,
                                this@ConfirmOrEditOrderFragment,
                                false
                            )
                        }
                }

                activity?.hideSoftKeyboard(meetingPlace)
            }

            edit.setOnClickListener {
                editPriceBottomSheet(viewModel.currency)
            }

            error.update.setOnClickListener {
                isOnlineAgain()
            }
        }
    }


    private fun setPriceValues(totalPriceValue: Double, prePay: Double, paymentToGuide: Double) {
        with(binding) {
            totalPrice.text = context?.getString(
                R.string.price_currency_value
            )?.setParticipantAndCurrency(
                null,
                totalPriceValue.toInt(), viewModel.currency
            )

            onTripsterPrice.text =
                context?.getString(R.string.price_currency_value)
                    ?.setParticipantAndCurrency(
                        null,
                        prePay.toInt(),
                        viewModel.currency
                    )

            payingYouPrice.text =
                context?.getString(R.string.price_currency_value)?.setParticipantAndCurrency(
                    null,
                    paymentToGuide.toInt(), viewModel.currency
                )
        }

        viewModel.totalPriceValue = totalPriceValue.toInt()
    }

    private fun countingPrice(isLoading: Boolean) {
        with(binding) {
            edit.isVisible = !isLoading && viewModel.status != Status.PAID.value
            totalPrice.isVisible = !isLoading
            shimmerTotalPrice.isVisible = isLoading

            onTripsterPrice.isVisible = !isLoading
            checkOnTripsterPrice.isVisible = !isLoading && !args.isConfirm
            shimmerOnTripster.isVisible = isLoading

            payingYouPrice.isVisible = !isLoading
            shimmerPayingYou.isVisible = isLoading
        }
    }

    private fun confirmOrEditOrder() {
        viewModel.emailError = true
        viewModel.phoneError = true
        viewModel.meetingPlaceError = true
        binding.emailPhoneContentError.makeVisibleGone()

        if (validFieldsLogic()) {
            val tickets = mutableListOf<Ticket>()
            var personCount = 0
            viewModel.selectedTicketsList.forEach {
                tickets.add(Ticket(it.count, it.id))
                personCount += it.count
            }

            val totalPrice = when {
                viewModel.status == Status.PAID.value -> null
                viewModel.hasCustomPrice || viewModel.pricingModel == context?.getStringRes(
                    R.string.per_group
                ) -> viewModel.totalPriceValue
                else -> null
            }

            val orderConfirmData = OrderConfirmOrEditData(
                autoSetDayBusy = viewModel.autoSetDayBusy,
                customPrice = totalPrice,
                dateExact = viewModel.selectedDate,
                email = viewModel.email,
                meetingPoint = viewModel.meetingPlace,
                personsCount = personCount,
                phone = viewModel.phone,
                tickets = tickets.filter {
                    it.count != 0
                },
                timeStart = viewModel.tourTime,
                false
            )

            with(binding) {
                progressBar.makeVisible()
                confirm.isClickable = false
                confirm.text = ""
            }
            viewModel.confirmOrder(args.orderId, orderConfirmData, args.isConfirm)

        } else {
            binding.progressBar.makeVisibleGone()
            with(binding) {
                phoneNumber.clearFocus()
                email.clearFocus()
                meetingPlace.clearFocus()
                emailPhoneContentError.isVisible = !emailLogic() || !phoneLogic()

                val errorView = when {
                    !tripDateHourLogic() -> tripDate
                    !checkTicketCount(viewModel.selectedTicketsList) -> membersCount
                    !meetingPlaceLogic() -> meetingPlace
                    !emailLogic() -> email
                    !phoneLogic() -> phoneNumber
                    else -> dateAndTime
                }

                if (!emailLogic() && !phoneLogic()) {
                    emailPhoneContentError.text = context?.getStringRes(R.string.email_phone_error)
                }

                nestedScrollView.smoothScrollTo(0, errorView.top - 100, 1000)
            }
        }
    }

    private fun setBorders(usernameDrawable: Int, passwordDrawable: Int) {
        with(binding) {
            email.background = context?.let { ContextCompat.getDrawable(it, usernameDrawable) }
            phoneNumber.background =
                context?.let { ContextCompat.getDrawable(it, passwordDrawable) }
        }
    }

    private fun checkType(type: String, status: String) {
        var text =
            getString(R.string.confirm_order_condition_txt)
        var underLinedText = getString(R.string.confirm_order_condition_txt_underLined)

        with(binding) {
            when (type) {
                context?.getStringRes(R.string.private_type) -> {
                    tripHour.makeVisible()
                    afterPaymentTitle.makeVisible()
                    timeOfExcursionRB.makeVisible()
                    onTisDayRB.makeVisible()
                    if (!args.isConfirm) {
                        text = getString(R.string.confirm_order_condition_txt_individual)
                        afterPaymentTitle.makeVisibleGone()
                        context?.let { context ->
                            conditions.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.gray_20
                                )
                            )
                        }
                        underLinedText =
                            getString(R.string.confirm_order_condition_txt_individual_underLined)
                        checkOnTripsterPrice.makeVisible()
                    }
                }
                else -> {
                    tripDate.setBackgroundResource(R.drawable.shape_stroke_gray_without_border)
                    tripDate.setMargins(
                        null,
                        null,
                        R.dimen.space_0,
                        null
                    )
                }
            }

            conditions.isVisible = viewModel.status != Status.PENDING_PAYMENT.value

            conditions.text = text
            conditions.setSpannable(text, underLinedText, conditions.currentTextColor, true) {
                if (args.isConfirm) {
                    startActivity(Constants.ABOUT_TERMS.openLinkInBrowser())
                } else {
                    startActivity(underLinedText.openMail())
                }
            }
        }
    }

    private fun hideOptionsContainer(height: Int) {
        with(binding) {
            afterPaymentTitle.visibility = View.GONE
            optionsContainer.visibility = View.GONE
            val newLayoutParams =
                containerOfAfterPayment.layoutParams as ConstraintLayout.LayoutParams
            newLayoutParams.topMargin = 0
            newLayoutParams.height = height
            containerOfAfterPayment.layoutParams = newLayoutParams
        }
    }

//    private fun setHeight(height: Int, layout: View) {
//        with(binding) {
//            if (layout.id == email.id) {
//                val params =
//                    email.layoutParams as ConstraintLayout.LayoutParams
//                params.height = height
//                email.layoutParams = params
//            } else {
//                val params =
//                    phoneNumber.layoutParams as ConstraintLayout.LayoutParams
//                params.height = height
//                phoneNumber.layoutParams = params
//            }
//        }
//    }

    private fun editPriceBottomSheet(currency: String) {
        navigateFragment(
            ConfirmOrEditOrderFragmentDirections.actionConfirmOrderFragmentToEditPriceBottomSheet(
                currency,
                viewModel.priceValue.toInt().toString(),
                CONFIRM_OR_EDIT,
                "",
                1.0f
            )
        )
    }

    private fun meetingPlaceLogic(): Boolean {
        with(binding) {
            return if (meetingPlace.text?.isEmpty() == true) {
                meetingPlace.setBackgroundResource(R.drawable.shape_stroke_tomat)
                viewModel.meetingPlaceError = false
                false
            } else {
                meetingPlace.setBackgroundResource(R.drawable.shape_stroke_gray)
                viewModel.meetingPlace = meetingPlace.text.toString()
                true
            }
        }
    }

    private fun tripDateHourLogic(): Boolean {
        with(binding) {
            when {
                tripDate.text.isEmpty() && tripHour.text.isEmpty() -> {
                    tripDate.setBackgroundResource(R.drawable.shape_stroke_tomat_left_8)
                    tripHour.setBackgroundResource(R.drawable.shape_stroke_tomat_right_8)
                    return false
                }
                tripDate.text.isEmpty() && tripHour.text.isNotEmpty() -> {
                    tripDate.setBackgroundResource(R.drawable.shape_stroke_border_tomat_left_8)
                    tripHour.setBackgroundResource(R.drawable.shape_stroke_gray_right_8)
                    return false
                }
                tripDate.text.isNotEmpty() && tripHour.text.isEmpty() -> {
                    tripDate.setBackgroundResource(R.drawable.shape_stroke_gray_left_8)
                    tripHour.setBackgroundResource(R.drawable.shape_stroke_tomat_right_8)
                    return false
                }
                else -> {
                    tripDate.setBackgroundResource(R.drawable.shape_stroke_gray_left_8)
                    tripHour.setBackgroundResource(R.drawable.shape_stroke_gray_right_8)
                    return true
                }

            }
        }
    }

    private fun emailLogic(): Boolean {
        with(binding) {
            when {
                !email.text.isNullOrEmpty() && !checkEmail(email.text.toString()) -> {
                    emailPhoneContentError.makeVisible()
                    emailPhoneContentError.text =
                        context?.getStringRes(R.string.email_content_error)
                    email.setBackgroundResource(R.drawable.shape_stroke_top_radius_error)
                    email.elevation = phoneNumber.elevation + 1
                    viewModel.emailError = false
                    return false
                }

                email.text.isNullOrEmpty() -> {
                    email.setBackgroundResource(R.drawable.shape_stroke_top_radius_error)
                    email.elevation = phoneNumber.elevation + 1
                    emailPhoneContentError.makeVisibleGone()
                    viewModel.emailError = false
                    return false
                }
                else -> {
                    email.setBackgroundResource(R.drawable.shape_stroke_top_radius)
                    viewModel.email = email.text.toString()
                    return true
                }
            }
        }
    }

    private fun phoneLogic(): Boolean {
        with(binding) {
            when {

                viewModel.phoneValidError -> {
                    emailPhoneContentError.makeVisible()
                    phoneNumber.setBackgroundResource(R.drawable.shape_stroke_bottom_radius_error)
                    emailPhoneContentError.text =
                        context?.getStringRes(R.string.phone_content_error)
                    phoneNumber.elevation = email.elevation + 1
                    viewModel.phoneError = false
                    viewModel.phoneValidError = false
                    return false
                }

                phoneNumber.text.isNullOrEmpty() -> {
                    emailPhoneContentError.makeVisibleGone()
                    phoneNumber.setBackgroundResource(R.drawable.shape_stroke_bottom_radius_error)
                    phoneNumber.elevation = email.elevation + 1
                    viewModel.phoneError = false
                    return false
                }

                else -> {
                    phoneNumber.setBackgroundResource(R.drawable.shape_stroke_bottom_radius)
                    viewModel.phone = phoneNumber.text.toString()
                    return true
                }
            }
        }
    }

    private fun validFieldsLogic() =
        meetingPlaceLogic() &&
                tripDateHourLogic() &&
                emailLogic()
                &&
                phoneLogic()
                &&
                checkTicketCount(viewModel.selectedTicketsList)

    private fun checkEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()


    private fun checkPhone(phoneNumber: String): Boolean =
        !viewModel.phoneValidError && phoneNumber.isNotEmpty()

    private fun checkDateAndTime(date: String, time: String) {
        var notify = false
        viewModel.closedTimes.map {
            val times = mutableListOf<String>()
            times.add(it.time)
            for (duration in 0..(it.duration / 60)) {
                val hour = it.time.substring(0..1)
                val hourPlus = (hour.toInt() + duration).toString()
                val minutes = it.time.substring(3..4)
                times.add(it.time.replace(hour, hourPlus))
                if (minutes.toInt() == 0) {
                    times.add(it.time.replace(minutes, "30"))
                } else {
                    times.add(it.time.replace(minutes, "0"))
                }
            }
            times
        }.forEach {
            it.forEach { item ->
                if (item.contains(time))
                    notify = true
            }
        }

        with(binding) {
            if (notify && time.isNotEmpty() && date.isNotEmpty()) {
                tripNotify.text = getString(
                    R.string.date_time_notify,
                    date,
                    time
                )
                tripNotify.makeVisible()
                icDateNotify.makeVisible()
            } else {
                tripNotify.makeVisibleGone()
                icDateNotify.makeVisibleGone()
            }
        }
    }

    private fun checkTicketCount(tickets: List<PerTicketOrderDetail>): Boolean {
        var count = 0
        tickets.forEach {
            count += it.count
            viewModel.isAtLeastOne = count > 0
        }
        with(binding) {
            if (viewModel.isAtLeastOne) {
                membersCountError.makeVisibleGone()
            } else {
                membersCountError.makeVisible()
            }
        }
        return viewModel.isAtLeastOne
    }

    private fun errorMessage(isOnline: Boolean) {
        with(binding) {
            shimmer.makeVisibleGone()
            containerError.makeVisible()
            error.title.text =
                if (isOnline) context?.getStringRes(R.string.call_error_title) else context?.getString(
                    R.string.no_internet_title
                )
            error.message.text =
                if (isOnline) context?.getStringRes(R.string.call_error_message) else context?.getString(
                    R.string.no_internet_message
                )
        }
    }

    private fun isOnlineAgain() {
        with(binding) {
            if (context?.isOnline() == true) {
                containerError.makeVisibleGone()
                shimmer.makeVisible()
                viewModel.getOrderDetails(args.orderId)
            }
        }
    }
}