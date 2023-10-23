package ru.tripster.guide.extensions

import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import ru.tripster.guide.R

fun Toast.showCustomToast(message: String, fragment: Fragment, showCheckMark: Boolean): Toast {
    val layout = fragment.layoutInflater.inflate(
        R.layout.toast_issue, null
    )

    // set the text of the TextView of the message
    val textView = layout.findViewById<TextView>(R.id.issueText)
    textView.text = message

    val checkMark = layout.findViewById<AppCompatImageView>(R.id.checkMark)
    checkMark.isVisible = showCheckMark

    // use the application extension function
        return this.apply {
            setGravity(Gravity.BOTTOM, 0, 40)
            duration = Toast.LENGTH_LONG
            view = layout
            show()
        }
}