package com.name.jat.ui.fragments.onboarding

import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.databinding.FragmentPickReadingTimeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PickReadingTimeFragment :
    FragmentBaseNCMVVM<PickReadingTimeViewModel, FragmentPickReadingTimeBinding>() {
    override val binding: FragmentPickReadingTimeBinding by viewBinding()
    override val viewModel: PickReadingTimeViewModel by viewModel()

    override fun onViewClick() {
        with(binding) {
            continueBtn.setOnClickListener {
                val directions =
                    PickReadingTimeFragmentDirections.actionPickReadingTimeFragmentToChooseFavoriteThemeFragment()
                navigateFragment(directions)
            }
            morning.setOnClickListener {
                selectReadTime(ReadTime.MORNING)
            }
            afternoon.setOnClickListener {
                selectReadTime(ReadTime.AFTERNOON)
            }
            evening.setOnClickListener {
                selectReadTime(ReadTime.EVENING)
            }
            anySpareTime.setOnClickListener {
                selectReadTime(ReadTime.ANY_SPARE_TIME)
            }
        }
    }

    private fun selectReadTime(readTime: ReadTime) {
        when (readTime) {
            ReadTime.MORNING -> {
                changeReadTimeSelection(isMorning = true)
            }
            ReadTime.AFTERNOON -> {
                changeReadTimeSelection(isAfternoon = true)
            }
            ReadTime.EVENING -> {
                changeReadTimeSelection(isEvening = true)
            }
            ReadTime.ANY_SPARE_TIME -> {
                changeReadTimeSelection(isAnySpareTime = true)
            }
        }
    }

    private fun changeReadTimeSelection(
        isMorning: Boolean = false,
        isAfternoon: Boolean = false,
        isEvening: Boolean = false,
        isAnySpareTime: Boolean = false
    ) {
        with(binding) {
            morning.isSelected = isMorning
            afternoon.isSelected = isAfternoon
            evening.isSelected = isEvening
            anySpareTime.isSelected = isAnySpareTime

            if (!continueBtn.isEnabled) {
                continueBtn.isEnabled = true
            }
        }
    }
}