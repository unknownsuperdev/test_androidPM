package com.fiction.me.ui.fragments.reader

import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.navArgs
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentOtherReportBinding
import com.fiction.me.extensions.hideKeyboard
import com.fiction.me.extensions.setMargins
import org.koin.androidx.viewmodel.ext.android.viewModel

class OtherReportFragment :
    FragmentBaseNCMVVM<OtherReportViewModel, FragmentOtherReportBinding>() {
    override val binding: FragmentOtherReportBinding by viewBinding()
    override val viewModel: OtherReportViewModel by viewModel()
    private val args: OtherReportFragmentArgs by navArgs()

    companion object {
        const val DID_SEND_REPORT = "DID_SEND_REPORT"
        const val FROM_OTHER_REPORT_FRAGMENT = "FROM_OTHER_REPORT_FRAGMENT"
    }

    override fun onView() {
        with(binding) {
            mainToolbar.run {
                setTitleText(resources.getString(R.string.report))
                setBackBtnIcon(R.drawable.ic_back)
            }
            rootView.viewTreeObserver.addOnGlobalLayoutListener {
                val r = Rect()
                rootView.getWindowVisibleDisplayFrame(r)
                val screenHeight: Int = rootView.rootView.height
                val keypadHeight: Int = screenHeight - r.bottom
                if (keypadHeight > screenHeight * 0.15)
                    sendReportBtn.setMargins(bottomMarginDp = 8)
                else
                    sendReportBtn.setMargins(bottomMarginDp = 24)
            }

            inputReportLayout.editText?.doOnTextChanged { inputText, _, _, _ ->
                inputText?.let { inText ->
                    if (inText.isNotEmpty()) {
                        makeBtnStyleEnable()
                    } else makeBtnStyleDisable()

                    context?.let {
                        inputReportLayout.counterTextColor =
                            if (inText.length == 500) ContextCompat.getColorStateList(
                                it,
                                R.color.orange_500
                            )
                            else ContextCompat.getColorStateList(it, R.color.black_600)
                    }
                }
            }
        }
    }

    override fun onEach() {
        with(binding) {
            onEach(viewModel.reportSend) { isSend ->
                progressCircular.visibility = View.INVISIBLE
                sendReportBtn.isClickable = true
                sendReportBtn.setText(R.string.send_a_report)
                inputReportLayout.editText?.run {
                    isFocusable = true
                    isFocusableInTouchMode = true
                }
                if (isSend) {
                    activity?.supportFragmentManager?.setFragmentResult(
                        FROM_OTHER_REPORT_FRAGMENT,
                        bundleOf(DID_SEND_REPORT to true)
                    )
                    popBackStack(R.id.readerFragment)
                }
            }
        }
    }

    override fun onViewClick() {
        with(binding) {
            sendReportBtn.setOnClickListener {
                progressCircular.visibility = View.VISIBLE
                sendReportBtn.text = null
                sendReportBtn.isClickable = false
                inputReportLayout.editText?.isFocusable = false
                val reportText = inputReportLayout.editText?.text.toString()
                viewModel.sendingReport(
                    args.bookId,
                    reportText,
                    args.chapterId,
                    args.readiedPercent
                )
                it.hideKeyboard()
            }
            mainToolbar.setOnBackBtnClickListener {
                popBackStack()
            }
        }
    }

    private fun makeBtnStyleEnable() {
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

    private fun makeBtnStyleDisable() {
        with(binding) {
            sendReportBtn.isEnabled = false
            context?.let {
                sendReportBtn.setTextColor(
                    ContextCompat.getColor(
                        it,
                        R.color.black_600
                    )
                )
            }
        }
    }
}