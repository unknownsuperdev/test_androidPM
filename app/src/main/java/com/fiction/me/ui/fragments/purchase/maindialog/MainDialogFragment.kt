package com.fiction.me.ui.fragments.purchase.maindialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.core.view.isVisible
import com.fiction.me.appbase.DialogFragmentBaseMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.DialogFragmentMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainDialogFragment :
    DialogFragmentBaseMVVM<MainDialogViewModel, DialogFragmentMainBinding>() {
    override val viewModel: MainDialogViewModel by viewModel()
    override val binding: DialogFragmentMainBinding by viewBinding()
    var setPositiveButtonClickListener: () -> Unit = {}
    var setNegativeButtonClickListener: () -> Unit = {}
    var onDismiss: () -> Unit = {}
    var isNegativeBtnVisible: Boolean = false
    var positiveBtnTxt: String = ""

    override fun onView() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        with(binding) {
            titleTxt.text = arguments?.getString(TITLE)
            descriptionTxt.text = arguments?.getString(DESCRIPTION)
            negativeBtn.isVisible = isNegativeBtnVisible
            if (positiveBtnTxt.isNotEmpty()) {
                positiveBtn.text = positiveBtnTxt
            }
            positiveBtn.setOnClickListener {
                setPositiveButtonClickListener()
                dismiss()
            }
            negativeBtn.setOnClickListener {
                setNegativeButtonClickListener()
                dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        onDismiss()
    }
    companion object {

        fun newInstance(title: String, description: String): MainDialogFragment {
            val args = Bundle()
            val mainDialogFragment = MainDialogFragment()
            args.putString(TITLE, title)
            args.putString(DESCRIPTION, description)
            mainDialogFragment.arguments = args
            return mainDialogFragment
        }

        const val TITLE = "TITLE"
        const val DESCRIPTION = "DESCRIPTION"
    }
}