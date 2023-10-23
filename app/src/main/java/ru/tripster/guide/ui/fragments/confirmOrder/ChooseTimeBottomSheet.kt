package ru.tripster.guide.ui.fragments.confirmOrder

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.tripster.guide.R
import ru.tripster.guide.appbase.bottomSheetDialog.BottomSheetDialogFragmentBaseMVVM
import ru.tripster.guide.appbase.utils.viewBinding
import ru.tripster.guide.databinding.BottomSheetChooseTimeBinding

class ChooseTimeBottomSheet :
    BottomSheetDialogFragmentBaseMVVM<ChooseTimeBottomSheetViewModel, BottomSheetChooseTimeBinding>() {
    override val viewModel: ChooseTimeBottomSheetViewModel by viewModel()
    override val binding: BottomSheetChooseTimeBinding by viewBinding()
    private val args: ChooseTimeBottomSheetArgs by navArgs()

    private val chooseTimeAdapter = ChooseTimeAdapter {
        activity?.supportFragmentManager?.setFragmentResult(CHOOSE_TIME_REQUEST_KEY, bundleOf(CHOOSE_TIME_BUNDLE to it))
        dismiss()
    }

    companion object {
        const val CHOOSE_TIME_REQUEST_KEY = "CHOOSE_TIME_REQUEST_KEY"
        const val CHOOSE_TIME_BUNDLE = "CHOOSE_TIME_BUNDLE"
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

        val listStartTime = args.listStartTime.replace("=","")
        val time = args.time.replace("=","")
        val startTime = args.startTime.replace("=","")
        val endTime = args.endTime.replace("=","")

        if (args.isFromCloseTime) {
            viewModel.getTimeListForCloseTime(
                time,
                listStartTime,
                startTime,
                endTime
            )
            context?.let {
                binding.title.text = it.getString(R.string.specify_the_time)
            }
        } else {
            viewModel.getTimeList(time, listStartTime)
            context?.let {
                binding.title.text = it.getString(R.string.confirm_time)
            }
        }

        binding.timeRv.apply {
            context?.let {
                layoutManager = LinearLayoutManager(it)
                setHasFixedSize(true)
                adapter = chooseTimeAdapter
            }
        }
    }

    override fun onViewClick() {
        binding.closeBtn.setOnClickListener {
            dismiss()
        }
    }

    override fun onEach() {
        onEach(viewModel.timeList) {
            chooseTimeAdapter.submitList(it)
        }
    }

}