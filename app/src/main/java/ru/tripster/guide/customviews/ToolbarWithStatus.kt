package ru.tripster.guide.customviews

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import ru.tripster.guide.R
import ru.tripster.guide.databinding.ToolbarWithStatusBinding
import ru.tripster.guide.extensions.statusType
import ru.tripster.guide.extensions.statusTypeGroupTour


@SuppressLint("CustomViewStyleable")
open class ToolbarWithStatus @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CustomToolbar(context, attrs) {

    private lateinit var binding: ToolbarWithStatusBinding

    private var backButtonClick: () -> Unit = {}

    init {
        val attr: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CustomToolbar, 0, 0)

        attr.recycle()
        this.createToolbar()
    }

    override fun createToolbar() {
        binding = ToolbarWithStatusBinding.inflate(LayoutInflater.from(context), this, false)

        with(binding) {
            backBtn.setOnClickListener { backButtonClick() }
            addView(root)
        }
    }

    fun setStatus(
        string: String,
        awareStartDt: String?,
        imageView: AppCompatImageView,
        initiator: String
    ) {
        string.statusType(binding.status, context, awareStartDt ?: "", initiator, true, "", 1.0f) {
            if (it) imageView.visibility = View.VISIBLE
        }
    }

    fun setStatusGroupTour(
        string: String,
        awareStartDt: String?
    ) {
        string.statusTypeGroupTour(binding.status, context, awareStartDt ?: "", 1.0f)
    }

    fun backButtonClicked(backButtonClicked: () -> Unit) {
        this.backButtonClick = backButtonClicked

    }

    fun setNumber(number: Int) {
        binding.number.text = context?.getString(R.string.number, number)
    }

    fun setTitle(text: String) {
        binding.title.text = text
    }

}