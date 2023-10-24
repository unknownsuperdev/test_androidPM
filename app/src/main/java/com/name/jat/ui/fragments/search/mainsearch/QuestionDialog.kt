package com.name.jat.ui.fragments.search.mainsearch

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.name.jat.databinding.QuestionDialogBinding

class QuestionDialog(val onClearClick: () -> Unit) : DialogFragment() {

    private lateinit var binding: QuestionDialogBinding
    private var questionText = ""
    private var positiveBtnText = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = QuestionDialogBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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