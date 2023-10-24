package com.fiction.me.ui.fragments.reader

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentReportBinding
import com.fiction.me.ui.fragments.reader.adapters.ReportFieldsAdapter
import com.fiction.me.utils.Events.Companion.REASON
import com.fiction.me.utils.Events.Companion.REPORT_SENT
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReportFragment : FragmentBaseNCMVVM<ReportViewModel, FragmentReportBinding>() {
    override val binding: FragmentReportBinding by viewBinding()
    override val viewModel: ReportViewModel by viewModel()
    private val args: ReportFragmentArgs by navArgs()

    companion object {
        const val DID_SEND_REPORT = "DID_SEND_REPORT"
        const val FROM_REPORT_FRAGMENT = "FROM_REPORT_FRAGMENT"
    }

    private val reportFieldsAdapter = ReportFieldsAdapter { id, isOther ->
        viewModel.run {
            isOtherField = isOther
            updateReportSelection(id)
            makeBtnStyleInEnable()
        }
    }

    override fun onView() {
        viewModel.getReportsList()
        with(binding) {
            mainToolbar.run {
                setTitleText(resources.getString(R.string.report))
                setBackBtnIcon(R.drawable.ic_back)
            }
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

        onEach(viewModel.reportSend) {
            activity?.supportFragmentManager?.setFragmentResult(
                FROM_REPORT_FRAGMENT,
                bundleOf(DID_SEND_REPORT to true)
            )
            popBackStack(R.id.readerFragment)
        }

    }

    override fun onViewClick() {
        with(binding) {
            sendReportBtn.setOnClickListener {
                if (viewModel.isOtherField) {
                    val directions =
                        ReportFragmentDirections.actionReportFragmentToOtherReportFragment(
                            args.chapterId,
                            args.bookId,
                            args.readiedPercent,
                            args.bookName
                        )
                    navigateFragment(directions)
                } else {
                    progressCircular.visibility = View.VISIBLE
                    sendReportBtn.text = null
                    viewModel.sendReports(args.bookId, args.chapterId, args.readiedPercent)
                }
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

}