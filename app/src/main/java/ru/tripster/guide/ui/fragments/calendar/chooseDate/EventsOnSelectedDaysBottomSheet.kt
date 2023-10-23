package ru.tripster.guide.ui.fragments.calendar.chooseDate

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.tripster.domain.model.calendar.DateScheduleItem
import ru.tripster.domain.model.calendar.GuidesSchedule
import ru.tripster.domain.model.calendar.StartEndDate
import ru.tripster.domain.model.events.OrderDetailsForChat
import ru.tripster.guide.R
import ru.tripster.guide.appbase.utils.DividerItemDecorator
import ru.tripster.guide.databinding.BottomSheetEventsOnSelectedDaysBinding
import ru.tripster.guide.extensions.dateFormattingOnlyDate
import ru.tripster.guide.extensions.dpToPx
import ru.tripster.guide.extensions.selectedDateFormat
import ru.tripster.guide.ui.fragments.calendar.chooseDate.CalendarChooseDateFragment.Companion.DATES
import ru.tripster.guide.ui.fragments.calendar.chooseDate.CalendarChooseDateFragment.Companion.GUIDE_SCHEDULE
import ru.tripster.guide.ui.fragments.calendar.orders.DateOrderAdapter

class EventsOnSelectedDaysBottomSheet : BottomSheetDialogFragment() {

    lateinit var binding: BottomSheetEventsOnSelectedDaysBinding

    private var individualItemClick: (Int, Int, String) -> Unit? = { _, _, _ -> }
    private var groupItemClick: (DateScheduleItem) -> Unit? = { _ -> }
    private var confirmOrderClick: (Int, String, String) -> Unit? = { _, _, _ -> }
    private var messageClick: (OrderDetailsForChat) -> Unit? = { _ -> }

    private val list = mutableListOf<DateScheduleItem>()
    private val dateOrdersAdapter =
        DateOrderAdapter(itemClickIndividual = { item ->
            individualItemClick(
                item.eventData.orders[0].id,
                item.id.toInt(),
                item.eventData.experience.type
            )
            dismiss()
        }, itemClickGroupTour = { item ->
            groupItemClick(item)
            dismiss()
        }, itemClickConfirmOrder = { id, type, experienceTitle ->
            confirmOrderClick(id, type, experienceTitle)
        }, itemClickMessage = { orderDetails, _ ->
            messageClick(orderDetails)
            dismiss()
        }, itemClickComment = {
            messageClick(it)
            dismiss()
        }, itemClickToDelete = {}, itemPhoneClick = { _, _, _, _ -> })


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)

        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                behaviour.skipCollapsed = true
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetEventsOnSelectedDaysBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val guidSchedule = arguments?.getParcelableArrayList<GuidesSchedule>(GUIDE_SCHEDULE)
        val dates = arguments?.getParcelable<StartEndDate?>(DATES)

        guidSchedule?.forEach {
            it.guideSchedule.forEach { dateSchedule ->
                list.add(dateSchedule)
            }
        }

        with(binding) {
            closeBtn.setOnClickListener { dismiss() }

            if (dates?.endDate != null) {
                title.text = context?.let { context ->
                    dates.startDate.selectedDateFormat(
                        context,
                        dates.endDate,
                        null,
                        null, null,
                        false
                    )
                }
            } else {
                title.text = dates?.startDate.toString().dateFormattingOnlyDate()
            }

            rvOrders.apply {
                context?.let {
                    layoutManager = LinearLayoutManager(it)
                    adapter = dateOrdersAdapter
                    val dividerItemDecoration: DividerItemDecorator? =
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.line_devider
                        )?.let { it1 ->
                            DividerItemDecorator(
                                it1, context.dpToPx(R.dimen.space_16)
                            )
                        }
                    if (dividerItemDecoration != null) {
                        addItemDecoration(dividerItemDecoration)
                    }
                }

                setOnScrollChangeListener { _, _, _, _, _ ->
                    val layoutManager = this.layoutManager as LinearLayoutManager
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    val firstVisibleItemView =
                        layoutManager.findViewByPosition(firstVisibleItemPosition)
                    val isPartiallyVisible =
                        firstVisibleItemView != null && firstVisibleItemView.top != 0

                    line.isVisible = isPartiallyVisible
                }
            }

            dateOrdersAdapter.submitList(list)
        }
    }

    fun onIndividualItemClick(individualItemClick: (Int, Int, String) -> Unit) {
        this.individualItemClick = individualItemClick
    }

    fun onGroupItemClick(groupItemClick: (DateScheduleItem) -> Unit) {
        this.groupItemClick = groupItemClick
    }

    fun onConfirmOrderClick(confirmOrderClick: (Int, String, String) -> Unit) {
        this.confirmOrderClick = confirmOrderClick
    }

    fun onMessageClick(messageClick: (OrderDetailsForChat) -> Unit) {
        this.messageClick = messageClick
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }
}