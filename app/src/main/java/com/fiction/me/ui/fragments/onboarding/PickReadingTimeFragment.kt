package com.fiction.me.ui.fragments.onboarding

import androidx.activity.addCallback
import androidx.navigation.NavDirections
import androidx.navigation.fragment.navArgs
import com.analytics.api.Events.USER_PROPERTY__FAVOURITE_READING_TIME
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentPickReadingTimeBinding
import com.fiction.me.utils.Events
import com.fiction.me.utils.Events.Companion.AFTERNOON
import com.fiction.me.utils.Events.Companion.ANY_SPARE_TIME
import com.fiction.me.utils.Events.Companion.EVENING
import com.fiction.me.utils.Events.Companion.FAVOURITE_READING_TIME
import com.fiction.me.utils.Events.Companion.MORNING
import com.fiction.me.utils.Events.Companion.READING_TIME_SCREEN_SHOWN
import org.koin.androidx.viewmodel.ext.android.viewModel

class PickReadingTimeFragment :
    FragmentBaseNCMVVM<PickReadingTimeViewModel, FragmentPickReadingTimeBinding>() {
    override val binding: FragmentPickReadingTimeBinding by viewBinding()
    override val viewModel: PickReadingTimeViewModel by viewModel()
    var selectedReadingTimeId: Int? = null
    val args: PickReadingTimeFragmentArgs by navArgs()
    private val morningId = 1
    private val afternoonId = 2
    private val eveningId = 3
    private val anySpareTimeId = 4

    override fun onView() {
        viewModel.run {
            trackEvents(READING_TIME_SCREEN_SHOWN)
            getFavoriteReadingTime()
            getOnBoardingSetting()
        }
        activity?.onBackPressedDispatcher?.addCallback(this) {
            //activity?.finish()
        }
    }

    override fun onViewClick() {
        with(binding) {
            continueBtn.setOnClickListener {
                sendEvent()
                viewModel.updateReadingTimeSelection(selectedReadingTimeId)
                val directions: NavDirections
                if (args.screens.isEmpty()) {
                    directions =
                        PickReadingTimeFragmentDirections.actionPickReadingTimeFragmentToExploreFragment()
                    navigateFragment(directions)
                    return@setOnClickListener
                }
                directions = when (args.screens[0]) {
                    "tags" -> {
                        PickReadingTimeFragmentDirections.actionPickReadingTimeFragmentToChooseFavoriteThemeFragment()
                    }
                    "for_you" -> {
                        PickReadingTimeFragmentDirections.actionPickReadingTimeFragmentToSelectionBooksFragment2()
                    }
                    else -> PickReadingTimeFragmentDirections.actionPickReadingTimeFragmentToExploreFragment()
                }
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

    override fun onEach() {
        onEach(viewModel.readingTime) { readingTimes ->
            with(binding) {
                readingTimes?.let { readingTime ->
                    readingTime.forEach {
                        when (it.id) {
                            morningId -> morningTxt.text = it.name
                            afternoonId -> afternoonTxt.text = it.name
                            eveningId -> eveningTxt.text = it.name
                            anySpareTimeId -> anySpareTimeTxt.text = it.name
                        }
                    }
                }
            }
        }

        onEach(viewModel.selectedReadingTimeId) {
            with(binding) {
                selectedReadingTimeId = it
                when (it) {
                    morningId -> changeReadTimeSelection(isMorning = true)
                    afternoonId -> changeReadTimeSelection(isAfternoon = true)
                    eveningId -> changeReadTimeSelection(isEvening = true)
                    anySpareTimeId -> changeReadTimeSelection(isAnySpareTime = true)

                }
            }
        }
    }

    private fun selectReadTime(readTime: ReadTime) {
        when (readTime) {
            ReadTime.MORNING -> {
                changeReadTimeSelection(isMorning = true)
                viewModel.insertOnBoardingSetting(morningId)
                selectedReadingTimeId = morningId
            }
            ReadTime.AFTERNOON -> {
                changeReadTimeSelection(isAfternoon = true)
                viewModel.insertOnBoardingSetting(afternoonId)
                selectedReadingTimeId = afternoonId
            }
            ReadTime.EVENING -> {
                changeReadTimeSelection(isEvening = true)
                viewModel.insertOnBoardingSetting(eveningId)
                selectedReadingTimeId = eveningId
            }
            ReadTime.ANY_SPARE_TIME -> {
                changeReadTimeSelection(isAnySpareTime = true)
                viewModel.insertOnBoardingSetting(anySpareTimeId)
                selectedReadingTimeId = anySpareTimeId
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

    private fun sendEvent() {
        val selectedReadingTime = when (selectedReadingTimeId) {
            morningId -> MORNING
            afternoonId -> AFTERNOON
            eveningId -> EVENING
            else -> ANY_SPARE_TIME
        }
        viewModel.setUserPropertyEvent(
            mapOf(
                USER_PROPERTY__FAVOURITE_READING_TIME to selectedReadingTime
            )
        )
        viewModel.trackEvents(
            Events.READING_TIME_SCREEN_CONTINUE_BUTTON_CLICKED, hashMapOf(
                FAVOURITE_READING_TIME to selectedReadingTime
            )
        )
    }
}