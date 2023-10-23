package ru.tripster.guide.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import ru.tripster.guide.R
import ru.tripster.guide.databinding.CustomConfirmOrderFromSameTouristBinding

@SuppressLint("CustomViewStyleable")
class CustomConfirmOder @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {
    private lateinit var binding: CustomConfirmOrderFromSameTouristBinding
    private var string: String? = null
    private var radioBtnClick: () -> Unit = {}

    init {
        val attr: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CustomConfirmation, 0, 0)

        string = attr.getString(R.styleable.CustomConfirmation_titleOrder)
        attr.recycle()
        this.createOrder()
    }

    private fun createOrder() {
        binding = CustomConfirmOrderFromSameTouristBinding.inflate(
            LayoutInflater.from(context),
            this,
            false
        )
        with(binding) {
            orderDesc.text = string
            orderRBtn.setOnClickListener { radioBtnClick }
        }
    }
}