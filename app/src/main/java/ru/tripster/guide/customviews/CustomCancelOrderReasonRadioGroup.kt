package ru.tripster.guide.customviews

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import ru.tripster.domain.model.order.orderrejectinfo.DuplicateOrder
import ru.tripster.guide.R
import ru.tripster.guide.databinding.*
import ru.tripster.guide.extensions.dateChange
import ru.tripster.guide.extensions.dpToPx
import ru.tripster.guide.extensions.setSpannable
import ru.tripster.guide.ui.fragments.cancel.model.OrderCancelReasonData
import ru.tripster.guide.ui.fragments.cancel.model.SubDataType

class CustomCancelOrderReasonRadioGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : LinearLayout(context, attrs) {

    private var binding: RadioGroupForCancelReasonBinding
    private lateinit var subItemMT: SubMessageItemOfRadioGroupBinding
    private lateinit var subItemRT: SubRadioItemsOfRadioGroupBinding
    private lateinit var subItemDT: SubDateItemOfRadioGroupBinding
    private lateinit var layoutOfDateType: LayoutForSubtitlesBinding
    private lateinit var layoutOfRadioType: LayoutForSubtitlesBinding
    private lateinit var layoutOfMessageType: LayoutForSubtitlesBinding
    private var gotoOrderFragment: (Int, String) -> Unit = { _, _ -> }
    private var userItemClick: (type: SubDataType) -> Unit = {}
    private var goToCalendar: (() -> Unit) = {}
    private var radioGroupClick: (() -> Unit) = {}
    var duplicateOrders = emptyList<DuplicateOrder>()
    private var notNullDuplicateOrders = 0
    var booleanOfRadioButtonView = true
    var itemCheckedId: Int? = null
    var subItemCheckedId: Int? = null
    var booleanOfRT: Boolean = true
    var booleanOfDT: Boolean = true
    var booleanOfRBClick: Boolean = false
    var errorDateType: ((Int) -> Unit) = {}
    var errorRadioType: ((Int) -> Unit) = {}


    init {
        binding =
            RadioGroupForCancelReasonBinding.inflate(LayoutInflater.from(context), this, false)
        orientation = VERTICAL
        addView(binding.root)
    }

    private fun openSubItem(userItemClick: SubDataType) {
        when (userItemClick) {
            SubDataType.MESSAGE_TYPE -> {
                subItemMT =
                    SubMessageItemOfRadioGroupBinding.inflate(
                        LayoutInflater.from(context),
                        this,
                        false
                    )
                layoutOfMessageType.container.addView(subItemMT.root)
            }
            SubDataType.RADIO_TYPE -> {
                booleanOfDT = true

                if (booleanOfRadioButtonView && duplicateOrders.isNotEmpty()) {
                    booleanOfRT = false
                    subItemRT =
                        SubRadioItemsOfRadioGroupBinding.inflate(
                            LayoutInflater.from(context),
                            this,
                            false
                        )

                    duplicateOrders.forEachIndexed { index, subData ->
                        if (subData.date != "" && subData.time != "") {
                            val radioButton = RadioButton(context)
                            radioButton.id = index
                            setSubRadioButtonParameters(radioButton)
                            radioButton.rootView.layoutParams
                            val textView = TextView(context)
                            setTextViewParameters(textView)
                            val textOfSubtitle = context?.resources?.getString(
                                R.string.cancel_order_sub_title,
                                subData.experienceId,
                                subData.id,
                                subData.travelerName,
                                subData.date.dateChange(),
                                subData.time,
                                subData.experienceTitle
                            )
                            textView.text = textOfSubtitle
                            val coloredText = textOfSubtitle?.substringAfter("на")
                            if (textOfSubtitle != null && coloredText != null) {
                                textView.setSpannable(
                                    textOfSubtitle,
                                    coloredText,
                                    ContextCompat.getColor(context, R.color.blue_500)
                                ) {
                                    gotoOrderFragment(subData.id, subData.expType)
                                }
                            }

                            notNullDuplicateOrders++
                            subItemRT.orderContainer.addView(textView)
                            subItemRT.containerChoice.addView(radioButton)

                        }
                    }
                    if (notNullDuplicateOrders != 0) {
                        layoutOfRadioType.container.addView(subItemRT.root)
                    } else {
                        booleanOfRBClick = false
                    }
                    subItemRT.containerChoice.setOnCheckedChangeListener { _, checkedId ->
                        subItemCheckedId = checkedId
                        booleanOfRT = true
                        subItemRT.selectOrderTitle.setTextAppearance(R.style.Text_17_400)
                    }
                } else {
                    booleanOfRBClick = false
                }
            }
            SubDataType.DATE_TYPE -> {
                booleanOfDT = false
                booleanOfRT = true
                subItemDT =
                    SubDateItemOfRadioGroupBinding.inflate(
                        LayoutInflater.from(context),
                        this,
                        false
                    )
                layoutOfDateType.container.addView(subItemDT.root)
                subItemDT.dateSelection.setOnClickListener {
                    goToCalendar()
                }
            }
        }
    }

    fun setRadioButtons(list: List<OrderCancelReasonData>) {
        list.forEachIndexed { index, orderCancelReasonData ->
            val radioButton = RadioButton(context)
            radioButton.id = index
            val subString = orderCancelReasonData.text.substringAfter("\n")
            radioButton.setSpannable(
                orderCancelReasonData.text,
                subString,
                ContextCompat.getColor(context, R.color.gray),
            ) {
                radioButton.text
            }
            radioButton.setTextAppearance(R.style.Text_17_400)
            setRadioButtonParameters(radioButton)
            if (index != 4)
                binding.radioGroup.addView(radioButton) else if (booleanOfRadioButtonView) binding.radioGroup.addView(
                radioButton
            )
            when (index) {
                3 -> {
                    layoutOfDateType =
                        LayoutForSubtitlesBinding.inflate(LayoutInflater.from(context), this, false)
                    binding.radioGroup.addView(layoutOfDateType.root)
                }
                4 -> {
                    layoutOfRadioType =
                        LayoutForSubtitlesBinding.inflate(LayoutInflater.from(context), this, false)
                    binding.radioGroup.addView(layoutOfRadioType.root)
                }
                5 -> {
                    layoutOfMessageType =
                        LayoutForSubtitlesBinding.inflate(LayoutInflater.from(context), this, false)
                    binding.radioGroup.addView(layoutOfMessageType.root)
                }
            }
        }
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            itemCheckedId = checkedId + 11
            booleanOfRBClick = true
            booleanOfRT = true
            booleanOfDT = true
            layoutOfMessageType.container.removeAllViews()
            layoutOfRadioType.container.removeAllViews()
            layoutOfDateType.container.removeAllViews()
            radioGroupClick()
            if (checkedId == 3 || checkedId == 4 || checkedId == 5) {
                list[checkedId].let { item ->
                    item.type?.let { type ->
                        this.userItemClick(type)
                        openSubItem(type)
                    }
                }
            }
        }
    }

    private fun setRadioButtonParameters(radioButton: RadioButton) {
        val parameters = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        parameters.setMargins(
            0,
            context.dpToPx(R.dimen.space_12),
            0,
            context.dpToPx(R.dimen.space_12)
        )
        with(radioButton) {
            gravity = top
            layoutDirection = View.LAYOUT_DIRECTION_RTL
            setBackgroundResource(R.color.transparent)
            setButtonDrawable(R.drawable.selector_radio_button)
            highlightColor = ContextCompat.getColor(context, R.color.white)
            radioButton.setLineSpacing(12f, 1f)
            rootView.layoutParams = parameters
        }

    }

    private fun setSubRadioButtonParameters(radioButton: RadioButton) {
        val parameters = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        parameters.setMargins(
            context.dpToPx(R.dimen.space_14),
            0,
            0,
            context.dpToPx(R.dimen.space_72)
        )
        with(radioButton) {
            gravity = top
            setBackgroundResource(R.color.transparent)
            setButtonDrawable(R.drawable.selector_radio_button)
            highlightColor = ContextCompat.getColor(context, R.color.white)
            rootView.layoutParams = parameters
        }
    }

    private fun setTextViewParameters(textView: TextView) {
        textView.setTextAppearance(R.style.Text_17_400)
        val parameters = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        parameters.setMargins(
            0,
            0,
            0,
            context.dpToPx(R.dimen.space_24)
        )
        with(textView) {
            gravity = top
            rootView.layoutParams = parameters
        }

    }

    private fun getRadioButtonColor(): ColorStateList {
        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_checked)
        )
        val colors = intArrayOf(
            ContextCompat.getColor(context, R.color.gray_80),
            ContextCompat.getColor(context, R.color.teal_500)
        )
        return ColorStateList(states, colors)
    }

    fun setTitleColor(color: Int) {
        errorRadioType(layoutOfRadioType.container.top)
        subItemRT.selectOrderTitle.setTextColor(color)
    }

    fun setDateColor() {
        errorDateType(layoutOfDateType.container.top)
        subItemDT.dateSelection.background =
            ContextCompat.getDrawable(context, R.drawable.shape_edt_tomat_rectangle_8)
    }

    fun setOnCalendarClickListener(goToCalendar: () -> Unit) {
        this.goToCalendar = goToCalendar
    }

    fun radioGroupClick(radioGroupClick: () -> Unit) {
        this.radioGroupClick = radioGroupClick
    }

    fun setClosedDayData(date: String) {
        subItemDT.dateSelection.text = date
        subItemDT.dateSelection.setTextAppearance(R.style.Text_17_400)
        subItemDT.dateSelection.background =
            ContextCompat.getDrawable(context, R.drawable.shape_gray80_rectangle_8)
        booleanOfDT = true
    }

    fun goToOrderFragment(goToOrderFragment: (Int, String) -> Unit) {
        this.gotoOrderFragment = goToOrderFragment
    }
}