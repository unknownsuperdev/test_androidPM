package com.name.jat.ui.fragments.reader

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.name.jat.R
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.databinding.FragmentReportBinding
import com.name.jat.ui.fragments.reader.adapters.ReportFieldsAdapter

class ReportFragment : FragmentBaseNCMVVM<ReportViewModel, FragmentReportBinding>() {
    override val binding: FragmentReportBinding by viewBinding()
    override val viewModel: ReportViewModel by viewModels()

    companion object {
        const val DID_SEND_REPORT = "DID_SEND_REPORT"
        const val FROM_REPORT_FRAGMENT = "FROM_REPORT_FRAGMENT"
    }

    private val reportFieldsAdapter = ReportFieldsAdapter { isSelected, id, isOther ->
        if (isOther) {
            viewModel.isOtherField = isSelected
            if (isSelected)
               makeBtnStyleInEnable()
            else if (viewModel.isSelectedReportFieldsListEmpty()) makeBtnStyleInDisable()
        } else {
            viewModel.addReportField(isSelected, id)
            if (!viewModel.isSelectedReportFieldsListEmpty()) {
                if (!binding.sendReportBtn.isEnabled) {
                    makeBtnStyleInEnable()
                }
            } else if (!viewModel.isOtherField) makeBtnStyleInDisable()
        }
    }

    override fun onView() {
        viewModel.getReportsList()
        with(binding) {
            mainToolbar.setTitleText(resources.getString(R.string.report))
            reportFieldsRV.apply {
                context?.let {
                    layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
                    adapter = reportFieldsAdapter
                }
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.reportFields) {
            reportFieldsAdapter.submitList(it)
        }
        with(binding) {
            onEach(viewModel.reportSend) {
                activity?.supportFragmentManager?.setFragmentResult(
                    FROM_REPORT_FRAGMENT,
                    bundleOf(DID_SEND_REPORT to true)
                )
                popBackStack(R.id.readerFragment)
            }
        }
    }

    override fun onViewClick() {
        with(binding) {
            sendReportBtn.setOnClickListener {
                if (viewModel.isOtherField)
                    navigateFragment(R.id.otherReportFragment)
                else {
                    progressCircular.visibility = View.VISIBLE
                    sendReportBtn.text = null
                }
                viewModel.sendReports()
            }
            mainToolbar.setOnBackBtnClickListener {
                popBackStack()
            }
        }
    }

    private fun makeBtnStyleInEnable() {
        with(binding) {
            sendReportBtn.isEnabled = true
            context?.let {
                sendReportBtn.setTextColor(
                    ContextCompat.getColor(
                        it,
                        R.color.white
                    )
                )
            }
        }
    }

    private fun makeBtnStyleInDisable() {
        with(binding) {
            sendReportBtn.isEnabled = false
            context?.let {
                sendReportBtn.setTextColor(
                    ContextCompat.getColor(
                        it,
                        R.color.black_300
                    )
                )
            }
        }
    }
}