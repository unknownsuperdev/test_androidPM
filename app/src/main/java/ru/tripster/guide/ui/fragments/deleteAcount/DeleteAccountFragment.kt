package ru.tripster.guide.ui.fragments.deleteAcount

import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.tripster.guide.R
import ru.tripster.guide.appbase.FragmentBaseNCMVVM
import ru.tripster.guide.appbase.utils.bottomNavBarVisibility
import ru.tripster.guide.appbase.utils.viewBinding
import ru.tripster.guide.databinding.FragmentDeleteAccountBinding
import ru.tripster.guide.extensions.makeVisible
import ru.tripster.guide.extensions.makeVisibleGone
import ru.tripster.guide.extensions.showCustomToast
import ru.tripster.guide.extensions.showSoftKeyboard
import ru.tripster.guide.ui.MainFragment

class DeleteAccountFragment :
    FragmentBaseNCMVVM<DeleteAccountViewModel, FragmentDeleteAccountBinding>() {
    override val viewModel: DeleteAccountViewModel by viewModel()
    override val binding: FragmentDeleteAccountBinding by viewBinding()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.onBackPressedDispatcher?.addCallback(this) {
            popBackStack()
        }
    }
    override fun onView() {
        activity?.bottomNavBarVisibility(false)

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        with(binding) {
            context?.let {
                toolbar.setTitle(it.getString(R.string.deleting_account))
            }
            container.viewTreeObserver.addOnGlobalLayoutListener {
                if (isAdded) {
                    val rect = Rect()
                    container.getWindowVisibleDisplayFrame(rect)
                    val screenHeight: Int = container.rootView.height
                    val keypadHeight = screenHeight - rect.bottom
                    if (keypadHeight > screenHeight * 0.15) {
                        deleteAccountField.setBackgroundResource(R.drawable.shape_stroke_orange_80)
                        if (Build.VERSION.SDK_INT <= 28) deleteAccountField.text?.let {
                            deleteAccountField.setSelection(
                                it.length
                            )
                        }
                        deleteAccountField.isCursorVisible = true
                    } else {
                        deleteAccountField.setBackgroundResource(R.drawable.shape_stroke_gray)
                        deleteAccountField.isCursorVisible = false
                    }
                }
            }
            deleteAccountFieldClickLogic()
        }
    }

    override fun onViewClick() {

        with(binding) {

            deleteAccount.setOnClickListener {

                if (checkValidation()) {
                    viewModel.deleteAccount()
                    progress.makeVisible()
                    context?.let {
                        deleteAccount.setTextColor(ContextCompat.getColor(it, R.color.tomat))
                    }

                } else {
                    notValidLogic()
                }
            }

            cancel.setOnClickListener {
                popBackStack()
            }
        }

    }

    override fun onEach() {

        onEach(viewModel.deleteAccount) { accountDeleted ->
            if (accountDeleted) context?.let {

                binding.progress.makeVisibleGone()
                binding.deleteAccount.setTextColor(ContextCompat.getColor(it, R.color.white))

                navigateFragment(
                    DeleteAccountFragmentDirections.actionDeleteAccountFragmentToLoginFragment(
                        0, "", false
                    )
                )

                Toast(it).showCustomToast(
                    it.getString(R.string.delete_account_toast),
                    this@DeleteAccountFragment,
                    false
                )

            }
        }

        onEach(viewModel.deleteAccountError) {
            binding.progress.makeVisibleGone()
            context?.let {
                binding.deleteAccount.setTextColor(ContextCompat.getColor(it, R.color.white))

                Toast(context).showCustomToast(
                    it.getString(R.string.call_error_message),
                    this@DeleteAccountFragment,
                    false
                )
            }

        }
    }

    private fun deleteAccountFieldClickLogic() {

        with(binding) {
            container.viewTreeObserver.addOnGlobalLayoutListener {
                if (isAdded) {
                    val rect = Rect()
                    container.getWindowVisibleDisplayFrame(rect)
                    val screenHeight: Int = container.rootView.height
                    val keypadHeight = screenHeight - rect.bottom
                    if (keypadHeight > screenHeight * 0.15) {
                        deleteAccountField.setBackgroundResource(R.drawable.shape_stroke_orange_80)
                        if (Build.VERSION.SDK_INT <= 28) deleteAccountField.text?.let {
                            deleteAccountField.setSelection(
                                it.length
                            )
                        }
                        deleteAccountField.isCursorVisible = true
                    } else {
                        deleteAccountField.setBackgroundResource(R.drawable.shape_stroke_gray)
                        deleteAccountField.isCursorVisible = false
                    }
                }
            }
        }
    }

    private fun checkValidation(): Boolean {
        return binding.deleteAccountField.text.toString()
            .lowercase() == context?.getString(R.string.delete_account)?.lowercase()
    }

    private fun notValidLogic() {

        with(binding) {
            context?.let { context ->
                deleteAccountCondition.setTextColor(ContextCompat.getColor(context, R.color.tomat))

                deleteAccountField.setBackgroundResource(R.drawable.shape_stroke_tomat)

            }
        }
    }

}
