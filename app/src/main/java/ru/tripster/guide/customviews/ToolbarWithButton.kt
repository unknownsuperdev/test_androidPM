package ru.tripster.guide.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import ru.tripster.guide.R
import ru.tripster.guide.databinding.ToolbarWithButtonBinding
import ru.tripster.guide.extensions.*

class ToolbarWithButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CustomToolbar(context, attrs) {

    private lateinit var binding: ToolbarWithButtonBinding

    private var buttonClick: () -> Unit = {}

    override fun createToolbar() {
        binding = ToolbarWithButtonBinding.inflate(LayoutInflater.from(context), this, false)
        with(binding) {

            title.text = customTitle
            setTextViewColorInt(title, customTitleColor.correctColor(context, R.color.black))

            button.setOnClickListener {
                buttonClick()
            }
            addView(root)
        }
    }

    override fun setTitleText(text: String) {
        binding.title.text = text
    }

    fun setButtonText(buttonText: String) {
        binding.button.text = buttonText
    }

    fun setOnButtonClickListener(buttonClick: () -> Unit) {
        this.buttonClick = buttonClick
    }

    fun setButtonEnabled() {
        binding.button.isEnabled = true
    }

    fun setButtonDisabled() {
        binding.button.isEnabled = false
    }
}