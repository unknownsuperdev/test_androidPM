package ru.tripster.guide.ui.fragments.chat

import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.tripster.guide.R
import ru.tripster.guide.appbase.bottomSheetDialog.BottomSheetDialogFragmentBaseMVVM
import ru.tripster.guide.appbase.utils.viewBinding
import ru.tripster.guide.appbase.viewmodel.BaseViewModel
import ru.tripster.guide.databinding.BottomSheetSeeContactWarningBinding
import ru.tripster.guide.ui.MainActivity
import ru.tripster.guide.ui.MainFragment

class SeeContactWarningBottomSheet :
    BottomSheetDialogFragmentBaseMVVM<BaseViewModel, BottomSheetSeeContactWarningBinding>() {
    override val viewModel: BaseViewModel by viewModel()
    override val binding: BottomSheetSeeContactWarningBinding by viewBinding()
    private val args: SeeContactWarningBottomSheetArgs by navArgs()
    override fun onView() {
        with(binding) {
            hideBottomNavBar()
            when (args.type) {
                context?.getString(R.string.chat_type) -> {
                    title.text =
                        context?.resources?.getString(R.string.carry_out_more_than_60_percent_of_orders)
                    seeContactDesc.text = context?.resources?.getString(
                        R.string.can_see_contact_desc,
                        "60%",
                        "${args.statisticData.ordersRateValue}%",
                        "${args.statisticData.bookingRateValue}%",
                        "${args.statisticData.confirmRateValue}%"
                    )
                }
                context?.getString(R.string.group_type) -> {
                    title.text = context?.resources?.getString(R.string.can_call_only_after_payment)
                    seeContactDesc.text =
                        context?.resources?.getString(R.string.how_to_call)
                }
                context?.getString(R.string.tour_phone_type) -> {
                    title.text = context?.resources?.getString(R.string.can_call_only_after_payment)
                    seeContactDesc.text =
                        context?.resources?.getString(R.string.how_to_call)
                }
                context?.getString(R.string.tour_type) -> {
                    title.text = context?.resources?.getString(R.string.view_contact_after_payment)
                    seeContactDesc.text =
                        context?.resources?.getString(R.string.how_to_see_contacts)
                }
                context?.getString(R.string.private_type) -> {
                    title.text =
                        context?.resources?.getString(R.string.increase_conversion_up_to_60_for_contacts)
                    seeContactDesc.text =
                        context?.resources?.getString(R.string.increase_conversion_up_to_60_for_contacts_for_calls)
                }
                else -> {
                    title.text =
                        context?.resources?.getString(R.string.increase_conversion_up_to_60_for_call)
                    seeContactDesc.text =
                        context?.resources?.getString(R.string.increase_conversion_up_to_60_for_contacts_for_calls)
                }
            }

        }
    }

    private fun hideBottomNavBar() {
        if (activity is MainActivity && args.type == context?.getString(R.string.chat_type)) {
            MainFragment().bottomNavBarVisibility(false)
//            (activity as MainActivity).bottomNavBarVisibility(false)
        }
    }

    override fun onViewClick() {
        with(binding) {
            close.setOnClickListener { dismiss() }
        }
    }
}
