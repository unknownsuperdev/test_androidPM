package ru.tripster.guide.extensions

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.text.*
import android.text.style.ClickableSpan
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import ru.tripster.guide.R
import ru.tripster.guide.ui.MainActivity


var View.visible: Boolean
    get() = this.visible
    set(value) {
        this.visibility = if (value) View.VISIBLE else View.GONE
    }

fun EditText.onTextChanged(onTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged.invoke(s.toString().trim())
        }

        override fun afterTextChanged(editable: Editable?) {}
    })
}

fun Activity.setLightStatusBar() {
    this.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
}

fun Activity?.setFullScreenAndLightStatusBar() {
    this?.window?.decorView?.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
}

fun Activity?.setStatusBarWhiteTextFullScreen() {
    this?.window?.decorView?.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
}
fun Activity?.setStatusBarWhiteText() {
    this?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
}

fun Dialog?.setFullScreenAndLightStatusBarDialog() {
    this?.window?.decorView?.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
}

fun Activity.setStatusBarColor(color: Int) {
    val window: Window = window
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = color
}

fun View.hideKeyboard() {
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(windowToken, 0)
}


fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeInVisible() {
    visibility = View.INVISIBLE
}

fun View.makeVisibleGone() {
    visibility = View.GONE
}

fun Activity.hideSoftKeyboard(view: View) {
    with(view) {
        isFocusable = false
        isFocusableInTouchMode = false
        val inputMethodManager =
            getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }
}

fun View.setSpannable(
    originalText: String,
    coloredText: String,
    color: Int,
    text: (SpannableStringBuilder) -> Unit,
    action: (() -> Unit)? = null
) {
    if (originalText.isNotEmpty()) {
        val spannableString = SpannableStringBuilder(originalText)
        val index = originalText.indexOf(coloredText)
        if (index == -1) return
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                action?.invoke()
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = color
                ds.isUnderlineText = false
            }
        }
        spannableString.setSpan(
            clickableSpan,
            index,
            index + coloredText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        text(spannableString)
    } else {
        return
    }
}

fun View.setMargins(
    leftMarginDp: Int? = null,
    topMarginDp: Int? = null,
    rightMarginDp: Int? = null,
    bottomMarginDp: Int? = null
) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val params = layoutParams as ViewGroup.MarginLayoutParams
        leftMarginDp?.run { params.leftMargin = context.dpToPx(this) }
        topMarginDp?.run { params.topMargin = context.dpToPx(this) }
        rightMarginDp?.run { params.rightMargin = context.dpToPx(this) }
        bottomMarginDp?.run { params.bottomMargin = context.dpToPx(this) }

        this.layoutParams = params
//        requestLayout()
    }
}

fun View.isHaveNestedScroll(nestedScrollView: NestedScrollView, root: View) {
    val location = IntArray(2)
    getLocationInWindow(location)
    val topMargin = location[1]
    val displayHeight =
        resources.displayMetrics.heightPixels - resources.getDimensionPixelSize(R.dimen.space_56)
    val bounds = Rect()
    getDrawingRect(bounds) // now bounds has the visible drawing coordinates of the view
    nestedScrollView.offsetDescendantRectToMyCoords(
        this,
        bounds
    ) // now bounds has the view's coordinates according to the parentViewGroup
    val isScroll = topMargin <= displayHeight
    nestedScrollView.isNestedScrollingEnabled = isScroll
    if (root is MotionLayout) root.isInteractionEnabled = isScroll
}

fun Activity.showSoftKeyboard(view: View) {
    with(view) {
        isFocusable = true
        isFocusableInTouchMode = true
        post {
            view.requestFocus()
            val inputMethodManager =
                getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }

    }
}


fun Int.setCorrectForm(): String {
    return when (this % 10) {
        1 -> {
            "$this место"
        }
        2, 3, 4 -> {
            "$this места"
        }
        else -> {
            "$this мест"
        }
    }
}

fun View.getLocationOnScreen(): Point {
    val location = IntArray(2)
    this.getLocationOnScreen(location)
    return Point(location[0], location[1])
}

fun View.getLocationInWindow(): Point {
    val location = IntArray(2)
    this.getLocationInWindow(location)
    return Point(location[0], location[1])
}

fun NestedScrollView.isViewVisible(view: View): Boolean {
    val scrollBounds = Rect()
    this.getDrawingRect(scrollBounds)
    val top = view.y
    val bottom = view.height + top
    return scrollBounds.bottom > bottom
}

fun Activity.statusBarHeight(): Int {
    val rectangle = Rect()
    val window = (this as MainActivity).window
    window.decorView.getWindowVisibleDisplayFrame(rectangle)

    return rectangle.top
}