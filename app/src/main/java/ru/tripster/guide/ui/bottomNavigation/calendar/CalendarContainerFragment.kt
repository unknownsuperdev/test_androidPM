package ru.tripster.guide.ui.bottomNavigation.calendar

import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.tripster.guide.appbase.FragmentBaseNCMVVM
import ru.tripster.guide.appbase.utils.viewBinding
import ru.tripster.guide.databinding.FragmentCalendarContainerBinding

class CalendarContainerFragment : FragmentBaseNCMVVM<CalendarContainerViewModel, FragmentCalendarContainerBinding>() {

    override val viewModel: CalendarContainerViewModel by viewModel()
    override val binding: FragmentCalendarContainerBinding by viewBinding()

}