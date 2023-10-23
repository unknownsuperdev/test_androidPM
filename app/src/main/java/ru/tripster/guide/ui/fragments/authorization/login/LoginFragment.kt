package ru.tripster.guide.ui.fragments.authorization.login

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.text.InputType
import android.util.Patterns
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.tripster.domain.Constants.Companion.AUTH_NOT_ACTIVE
import ru.tripster.guide.App
import ru.tripster.guide.enums.DeepLinkPrefix
import ru.tripster.guide.R
import ru.tripster.guide.appbase.FragmentBaseNCMVVM
import ru.tripster.guide.appbase.utils.viewBinding
import ru.tripster.guide.databinding.FragmentLoginBinding
import ru.tripster.guide.extensions.dpToPx
import ru.tripster.guide.extensions.getStringRes
import ru.tripster.guide.extensions.isOnline
import ru.tripster.guide.extensions.makeVisible
import ru.tripster.guide.extensions.makeVisibleGone
import ru.tripster.guide.extensions.onTextChanged
import ru.tripster.guide.extensions.setLightStatusBar
import ru.tripster.guide.extensions.showCustomToast

class LoginFragment : FragmentBaseNCMVVM<LoginViewModel, FragmentLoginBinding>() {
    override val viewModel: LoginViewModel by viewModel()
    override val binding: FragmentLoginBinding by viewBinding()
    private val args: LoginFragmentArgs by navArgs()

    override fun onView() {
        App.refreshScope()
//        viewModel.saveUser = args.isSavedUser
//        if (args.userInfo.isNotEmpty()) {
//        }
//        val loginAnsPass = args.userInfo.split(" ")
//        binding.username.setText(loginAnsPass[0])
//        binding.password.setText(loginAnsPass[1])
        binding.password.onTextChanged {
            binding.showHideBtn.isClickable = it.isNotEmpty()
        }
    }

    override fun onEach() {
        onEach(viewModel.loginSuccess) {
            when (args.deeplinkPrefix) {
                DeepLinkPrefix.DELETE_ACCOUNT.prefix -> navigateFragment(
                    LoginFragmentDirections.actionLoginFragmentToProfileFragment()
                )

                else -> navigateFragment(
                    LoginFragmentDirections.actionLoginFragmentToMainFragment()
                        .setDeepLinkOrderId(args.deepLinkOrderId)
                )
            }
        }

        onEach(viewModel.loginError) {
            with(binding) {
                progress.makeVisibleGone()
                login.isClickable = true
                login.text = context?.getStringRes(R.string.login)

                if (context?.isOnline() == true) {
                    if (it == AUTH_NOT_ACTIVE) {
                        authNotActive()
                    } else showDialog(
                        titleText = resources.getString(R.string.loginDialogTitle),
                        bodyText = resources.getString(R.string.loginDialogText),
                        positiveBtnTxt = resources.getString(R.string.ok)
                    )

                } else {
                    context?.getStringRes(R.string.no_internet_toast)
                        ?.let { Toast(context).showCustomToast(it, this@LoginFragment, false) }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.usernameError = true
        viewModel.passwordError = true
        viewModel.eyeState = true
        viewModel.clickCount = 0
    }

    override fun onViewClick() {
        with(binding) {
            activity?.setLightStatusBar()
            containerOfLogin.setOnClickListener {
                viewModel.clickCount = 0
            }
            logo.setOnClickListener {
                viewModel.clickCount++
                if (viewModel.clickCount == 5) {
                    if (ru.tripster.guide.BuildConfig.DEBUG)
                        navigateToDebugMenu()
                    viewModel.clickCount = 0
                }
            }

            password.setOnEditorActionListener { _, actionId, _ ->
                clearInputFocusFromKeyboard(actionId)
            }

            login.setOnClickListener {
                viewModel.clickCount = 0
                username.clearFocus()
                password.clearFocus()
                viewModel.usernameError = true
                viewModel.passwordError = true
                when {
                    username.text?.isEmpty() == true && password.text?.isEmpty() == true -> {
                        error.makeVisibleGone()
                        setBorders(
                            R.drawable.shape_stroke_top_radius_error,
                            R.drawable.shape_bottom_radius_password_error
                        )
                        viewModel.usernameError = false
                        viewModel.passwordError = false
                    }

                    username.text?.isEmpty() == true -> {
                        error.makeVisibleGone()
                        username.elevation = passwordContainer.elevation + 1
                        setBorders(
                            R.drawable.shape_stroke_top_radius_error,
                            R.drawable.shape_stroke_bottom_radius
                        )
                        viewModel.usernameError = false
                    }

                    password.text?.isEmpty() == true -> {
                        error.makeVisibleGone()
                        passwordContainer.elevation = username.elevation + 1
                        setBorders(
                            R.drawable.shape_stroke_top_radius,
                            R.drawable.shape_stroke_bottom_radius_error
                        )
                        viewModel.passwordError = false
                    }

                    !checkEmail(username.text.toString()) -> {
                        error.makeVisible()
                        error.text = resources.getString(R.string.loginContentError)
                        username.elevation = passwordContainer.elevation + 1

                        setBorders(
                            R.drawable.shape_username_tomat_80_shadow,
                            R.drawable.shape_stroke_bottom_radius
                        )
                        viewModel.usernameError = false

                    }

                    else -> {
                        error.makeVisibleGone()
                        context?.let { context ->
                            viewModel.login(
                                username.text.toString(), password.text.toString(),
                                context
                            )
                        }
                        login.text = ""
                        login.isClickable = false
                        progress.makeVisible()
                        setBorders(
                            R.drawable.shape_stroke_top_radius,
                            R.drawable.shape_stroke_bottom_radius
                        )
                    }
                }
            }
            username.setOnFocusChangeListener { _, focused ->
                viewModel.clickCount = 0
                with(username) {
                    when {
                        focused && viewModel.usernameError -> {
                            elevation = if (viewModel.passwordError) {
                                setBorders(
                                    R.drawable.shape_rectangle_top_click_shadow,
                                    R.drawable.shape_stroke_bottom_radius
                                )
                                passwordContainer.elevation + 1
                            } else {
                                setBorders(
                                    R.drawable.shape_rectangle_top_click_shadow,
                                    R.drawable.shape_bottom_radius_password_error
                                )
                                passwordContainer.elevation + 1
                            }
                        }

                        else -> {
                            elevation = if (viewModel.passwordError) {
                                setBorders(
                                    R.drawable.shape_username_tomat_80_shadow,
                                    R.drawable.shape_stroke_bottom_radius
                                )
                                passwordContainer.elevation + 1
                            } else {
                                setBorders(
                                    R.drawable.shape_username_tomat_80_shadow,
                                    R.drawable.shape_bottom_radius_password_error
                                )
                                passwordContainer.elevation + 1
                            }
                        }
                    }
                }
            }

            password.setOnFocusChangeListener { _, focused ->
                viewModel.clickCount = 0
                with(passwordContainer) {
                    elevation = if (focused) {
                        if (!viewModel.passwordError) {
                            if (viewModel.usernameError) {
                                setBorders(
                                    R.drawable.shape_stroke_top_radius,
                                    R.drawable.shape_password_tomat_80_shadow
                                )
                                username.elevation + 1
                            } else {
                                setBorders(
                                    R.drawable.shape_top_radius_username_error,
                                    R.drawable.shape_password_tomat_80_shadow
                                )
                                username.elevation + 1
                            }
                        } else {
                            if (viewModel.usernameError) {
                                setBorders(
                                    R.drawable.shape_stroke_top_radius,
                                    R.drawable.shape_rectangle_bottom_click_shadow
                                )
                                username.elevation + 1
                            } else {
                                setBorders(
                                    R.drawable.shape_top_radius_username_error,
                                    R.drawable.shape_rectangle_bottom_click_shadow
                                )
                                username.elevation + 1
                            }
                        }

                    } else {
                        when {
                            viewModel.passwordError && viewModel.usernameError -> {
                                setBorders(
                                    R.drawable.shape_stroke_top_radius,
                                    R.drawable.shape_stroke_bottom_radius
                                )
                                username.elevation
                            }
                            viewModel.usernameError -> {
                                setBorders(
                                    R.drawable.shape_stroke_top_radius,
                                    R.drawable.shape_stroke_bottom_radius_error
                                )
                                username.elevation + 1
                            }

                            else -> {
                                setBorders(
                                    R.drawable.shape_stroke_top_radius_error,
                                    R.drawable.shape_stroke_bottom_radius
                                )
                                username.elevation - 1
                            }
                        }
                    }
                }
            }
            showHideBtn.setOnClickListener {
                viewModel.clickCount = 0
                viewModel.eyeState = if (viewModel.eyeState) {
                    password.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    context?.let { context ->
                        showHideBtn.setPadding(
                            context.dpToPx(R.dimen.space_13),
                            context.dpToPx(R.dimen.space_15),
                            context.dpToPx(R.dimen.space_13),
                            context.dpToPx(R.dimen.space_14)
                        )
                    }
                    showHideBtn.setImageResource(R.drawable.ic_visibility_off)
                    false
                } else {
                    password.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    context?.let { context ->
                        showHideBtn.setPadding(
                            context.dpToPx(R.dimen.space_13),
                            context.dpToPx(R.dimen.space_16),
                            context.dpToPx(R.dimen.space_13),
                            context.dpToPx(R.dimen.space_17)
                        )
                    }
                    showHideBtn.setImageResource(R.drawable.ic_eye)
                    true
                }
                val cursorPosition = password.text?.length ?: 0
                password.setSelection(cursorPosition)
            }

            remindPasswordBtn.setOnClickListener {
                viewModel.clickCount = 0
                intentLinkRecover()
            }
        }
    }

    private fun setBorders(usernameDrawable: Int, passwordDrawable: Int) {
        with(binding) {
            username.background = context?.let { ContextCompat.getDrawable(it, usernameDrawable) }
            passwordContainer.background =
                context?.let { ContextCompat.getDrawable(it, passwordDrawable) }
        }
    }

    private fun showDialog(
        titleText: String,
        bodyText: String,
        positiveBtnTxt: String
    ) {
        val builder = AlertDialog.Builder(context, R.style.Background_transparent).create()
        val view = layoutInflater.inflate(R.layout.dialog_login, null)
        val body = view.findViewById(R.id.bodyTv) as TextView
        val title = view.findViewById(R.id.titleTv) as TextView
        val okBtn = view.findViewById(R.id.okBtn) as TextView
        val cancelBtn = view.findViewById(R.id.cancelBtn) as TextView
        with(builder) {
            setView(view)
            cancelBtn.makeVisibleGone()
            body.text = bodyText
            title.text = titleText
            okBtn.text = positiveBtnTxt
            okBtn.setOnClickListener {
                dismiss()
            }
            setCanceledOnTouchOutside(false)
            show()
        }
    }

    private fun authNotActive() {
        binding.error.makeVisible()
        binding.error.text = context?.getString(R.string.account_not_active)
        binding.username.elevation = binding.passwordContainer.elevation + 1
        setBorders(
            R.drawable.shape_stroke_top_radius_error,
            R.drawable.shape_stroke_bottom_radius
        )
        viewModel.usernameError = false
    }

    private fun navigateToDebugMenu() {
        navigateFragment(LoginFragmentDirections.actionLoginFragmentToMenuDebugFragment())
    }

    private fun checkEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun clearInputFocusFromKeyboard(actionId: Int): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            binding.username.clearFocus()
            binding.password.clearFocus()
        }
        return false
    }

    private fun intentLinkRegister() {
        val uriSignUp: Uri = Uri.parse("https://experience.tripster.ru/registration/?next=/")
        val intentSignUp = Intent(Intent.ACTION_VIEW, uriSignUp)
        startActivity(intentSignUp)
    }

    private fun intentLinkRecover() {
        val uriRemindPassword: Uri = Uri.parse("https://experience.tripster.ru/password/recover/")
        val intentRemindPassword = Intent(Intent.ACTION_VIEW, uriRemindPassword)
        startActivity(intentRemindPassword)
    }
}