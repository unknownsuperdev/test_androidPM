package com.fiction.me.ui.fragments.search.mainsearch.questiondialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.fiction.me.appbase.DialogFragmentBaseMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.QuestionDialogBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class QuestionDialog(val onClearClick: () -> Unit) :
    DialogFragmentBaseMVVM<QuestionDialogViewModel, QuestionDialogBinding>() {
    override val viewModel: QuestionDialogViewModel by viewModel()
    override val binding: QuestionDialogBinding by viewBinding()
    private var questionText = ""
    private var positiveBtnText = ""

    override fun onView() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        with(binding) {
            negativeState.setOnClickListener {
                dismiss()
            }

            positiveState.setOnClickListener {
                onClearClick()
                dismiss()
            }
            binding.positiveState.text = positiveBtnText
            binding.question.text = questionText
        }
    }

    fun setQuestionText(text: String) {
        questionText = text
    }

    fun setNegativeBtnText(text: String) {
        binding.negativeState.text = text
    }

    fun setPositiveBtnText(text: String) {
        positiveBtnText = text
    }
}