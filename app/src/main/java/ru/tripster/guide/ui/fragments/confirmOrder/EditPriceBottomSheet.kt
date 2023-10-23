package ru.tripster.guide.ui.fragments.confirmOrder

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.tripster.guide.R
import ru.tripster.guide.appbase.bottomSheetDialog.BottomSheetDialogFragmentBaseMVVM
import ru.tripster.guide.appbase.utils.viewBinding
import ru.tripster.guide.databinding.BottomSheetEditPriceBinding
import ru.tripster.guide.extensions.hideKeyboard
import ru.tripster.guide.extensions.onTextChanged
import ru.tripster.guide.extensions.visible
import ru.tripster.guide.extensions.*
import ru.tripster.guide.ui.MainActivity

class EditPriceBottomSheet :
    BottomSheetDialogFragmentBaseMVVM<EditPriceBottomSheetViewModel, BottomSheetEditPriceBinding>() {

    override val viewModel: EditPriceBottomSheetViewModel by viewModel()
    override val binding: BottomSheetEditPriceBinding by viewBinding()
    private val args: EditPriceBottomSheetArgs by navArgs()

    companion object {
        const val NEW_FIXED_PRICE_REQUEST_KEY = "NEW_FIXED_PRICE_REQUEST_KEY"
        const val NEW_FIXED_PRICE_BUNDLE = "NEW_FIXED_PRICE_BUNDLE"
        const val ORDER_GROUP = "orderGroup"
        const val CONFIRM_OR_EDIT = "confirmOrEdit"
        const val EDIT_AMOUNT_KEY = "newAvailableAmountKey"
        const val NEW_AVAILABLE_AMOUNT_KEY = "newAvailableAmountKey"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {

            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

            setOnKeyListener { _: DialogInterface, keyCode: Int, keyEvent: KeyEvent ->
                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_UP) {
                    dismiss()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }
    }

    override fun onView() {
        with(binding) {
            this@EditPriceBottomSheet.args.let {
                when (it.type) {
                    CONFIRM_OR_EDIT -> {
                        price.setText(it.price)
                        currency.text = it.currency
                    }
                    ORDER_GROUP -> {
                        price.setText(if (it.spaceAmount.toInt() != 0) it.spaceAmount else "")
                        currency.visible = false
                        title.text = getString(R.string.space_bottom_title)
                        confirm.text = getString(R.string.space_bottom_button)
                        when (args.progress) {
                            in 0.0f..0.5f -> {
                                dialog?.window?.decorView?.systemUiVisibility =
                                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            }

                            in 0.5f..1.0f -> {
                                dialog?.setFullScreenAndLightStatusBarDialog()
                            }
                        }
                    }
                }
                price.apply {
                    requestFocus()
                    onTextChanged { s ->
                        if (s.length > 9) {
                            this.setText(R.string.max_price)
                        }
                    }

                    doAfterTextChanged { t ->
                        t?.let { nonNullText ->
                            if (
                                checkInputChars(nonNullText.toString())
                            ) setText(
                                text.toString().dropLast(1)
                            )
                        }
                        text?.length?.let { length -> this.setSelection(length) }
                    }
                }
            }
        }
    }
    override fun onViewClick() {

        with(binding) {
            confirm.setOnClickListener {
                when {
                    args.type == ORDER_GROUP && checkFieldValid() -> confirmAmount()
                    args.type == CONFIRM_OR_EDIT && checkFieldValid() ->
                        setFragmentResult(
                            NEW_FIXED_PRICE_REQUEST_KEY,
                            bundleOf(NEW_FIXED_PRICE_BUNDLE to viewModel.totalPrice)
                        )
                }
            }

            close.setOnClickListener {
                price.hideKeyboard()
                dismiss()
            }
        }
    }

    private fun checkInputChars(text: String): Boolean = text.contains(".") ||
            text.contains(",") ||
            text.contains("-") ||
            text.contains(" ") ||
            (text.startsWith("0") && text.length > 1)

    private fun confirmAmount() {
        setFragmentResult(
            EDIT_AMOUNT_KEY,
            bundleOf(NEW_AVAILABLE_AMOUNT_KEY to binding.price.text.toString())
        )
        binding.price.hideKeyboard()
        dismiss()
    }

    private fun checkFieldValid(): Boolean {
        with(binding) {
            return if (price.text.isNullOrEmpty()) {
                price.background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.shape_rectangle_left_error_shadow,
                    null
                )
                false
            } else {
                price.hideKeyboard()
                if (args.type == CONFIRM_OR_EDIT) viewModel.totalPrice =
                    price.text.toString().toInt()
                dismiss()
                true
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        binding.price.hideKeyboard()
        dismiss()
    }
}