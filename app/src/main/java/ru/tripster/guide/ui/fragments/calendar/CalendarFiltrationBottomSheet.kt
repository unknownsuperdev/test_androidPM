package ru.tripster.guide.ui.fragments.calendar

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.tripster.guide.R
import ru.tripster.guide.appbase.adapter.LoadingState
import ru.tripster.guide.appbase.bottomSheetDialog.BottomSheetDialogFragmentBaseMVVM
import ru.tripster.guide.appbase.utils.viewBinding
import ru.tripster.guide.databinding.BottomSheetCalendarFiltrBinding
import ru.tripster.guide.extensions.*

class CalendarFiltrationBottomSheet :
    BottomSheetDialogFragmentBaseMVVM<CalendarFiltrationBottomSheetViewModel, BottomSheetCalendarFiltrBinding>() {
    override val viewModel: CalendarFiltrationBottomSheetViewModel by viewModel()
    override val binding: BottomSheetCalendarFiltrBinding by viewBinding()
    private val args: CalendarFiltrationBottomSheetArgs by navArgs()
    private val experienceAdapter = ExperienceAdapter {
        when (args.navigationType) {
            context?.getString(R.string.closing_time) -> {
                setFragmentResult(EXPERIENCE_KEY, bundleOf(EXPERIENCE_BUNDLE to it))
            }
            else -> {
                setFragmentResult(
                    ORDER_ID_REQUEST_KEY,
                    bundleOf(ORDER_ID_BUNDLE to it)
                )
            }
        }

        dismiss()
    }

    companion object {
        const val EXPERIENCE_KEY = "EXPERIENCE_KEY"
        const val EXPERIENCE_BUNDLE = "EXPERIENCE_BUNDLE"
        const val ORDER_ID_REQUEST_KEY = "ORDER_ID_REQUEST_KEY"
        const val ORDER_ID_BUNDLE = "ORDER_ID_BUNDLE"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)

        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                behaviour.skipCollapsed = true
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        return dialog
    }

    override fun onView() {
        with(binding) {
            if (args.navigationType == context?.getString(R.string.closing_time)) {
                title.text = context?.getString(R.string.do_not_accept_orders)
                viewModel.isClosingTime = true
            }
            experienceRv.apply {
                context?.let {
                    layoutManager = LinearLayoutManager(it)
                    adapter = experienceAdapter
                }
            }

            experienceAdapter.observeLoadState(viewLifecycleOwner) { state, _ ->
                when (state) {
                    LoadingState.LOADED -> {
                        shimmer.makeVisibleGone()
                        experienceRv.makeVisible()
                    }

                    LoadingState.ERROR -> {

                        context?.getStringRes(if (context?.isOnline() == false) R.string.no_internet_toast else R.string.no_loading_toast)
                            ?.let {
                                Toast(context).showCustomToast(
                                    it,
                                    this@CalendarFiltrationBottomSheet,
                                    false
                                )
                            }
                    }

                    LoadingState.LOADING -> {}

                }
            }
        }
    }

    override fun onViewClick() {
        with(binding) {
            closeBtn.setOnClickListener {
                dismiss()
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.pagingData) {
            experienceAdapter.submitData(lifecycle, it)
        }
    }

}