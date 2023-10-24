package com.fiction.me.ui.fragments.loginregistration

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fiction.me.R
import com.fiction.me.signinverification.dialog.ProgressDialog
import com.fiction.me.signinverification.views.BaseTitle
import com.fiction.me.signinverification.views.EmailBaseTextFieldView
import com.fiction.me.signinverification.views.MainButton
import com.fiction.me.signinverification.views.MainToolbar
import com.fiction.me.signinverification.views.PasswordBaseTextFieldView
import com.fiction.me.theme.AppTheme
import com.fiction.me.utils.Keyboard
import com.fiction.me.utils.keyboardAsState
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpWithEmailFragment : Fragment() {

    val viewModel: SignUpWithEmailViewModel by viewModel()

    @Composable
    fun SignUpWithEmailFragmentContent() {
        LaunchedEffect(Unit) {
            viewModel
                .signedUp
                .collect {
                    val directions = SignUpWithEmailFragmentDirections
                        .actionSignUpWithEmailFragmentToVerifyEmailAddressFragment(viewModel.email)
                    findNavController().navigate(directions)
                }
        }
        val focusManager = LocalFocusManager.current
        val progressAnimation = remember {
            mutableStateOf(false)
        }
        val bottomPadding = remember {
            mutableStateOf(16)
        }
        viewModel.fieldRequired = stringResource(id = R.string.field_required)
        viewModel.enterCorrectEmail = stringResource(id = R.string.enter_correct_email)
        viewModel.invalidPass = stringResource(id = R.string.invalid_pass)
        viewModel.getButtonState()
        ProgressDialog(progressAnimation)
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                BaseTitle(R.string.create_an_account)
                EmailBaseTextFieldView(
                    paddingTop = 32,
                    email = viewModel.emailState,
                    helperStatusType = viewModel.emailHelperStatusType,
                    helperText = viewModel.emailHelperText,
                    isStatusNotNone = viewModel.isEmailStatusNotNone,
                ) { email, isFocused ->
                    viewModel.email = email
                    viewModel.getMailValidation(isFocused)
                }

                PasswordBaseTextFieldView(
                    hintPasswordExpanded = stringResource(id = R.string.create_your_password),
                    hintPasswordCollapsed = stringResource(id = R.string.create_your_password),
                    password = viewModel.passwordState,
                    helperStatusType = viewModel.passwordHelperStatusType,
                    helperText = viewModel.passwordHelperText,
                    isStatusNotNone = viewModel.isPasswordStatusNotNone
                ) { password, isFocused ->
                    viewModel.password = password
                    viewModel.getPasswordValidation(isFocused)
                }
                ClickableTextView(
                    stringResource(id = R.string.sign_in),
                    stringResource(id = R.string.already_have_an_account)
                ) {
                    val directions =
                        SignUpWithEmailFragmentDirections.actionSignUpWithEmailFragmentToSignInOrSignUpFragment(
                            LoginRegisterTypes.SIGN_IN
                        )
                    findNavController().navigate(directions)
                }
            }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter,
        ) {
            MainButton(
                isEnable = viewModel.isEnabledConfirmBtn,
                text = stringResource(id = R.string.continue_),
                viewModel.isShowProgressBar,
                bottomPadding
            ) {
                focusManager.clearFocus()
                viewModel.signUp()
            }
        }
        when (keyboardAsState().value) {
            Keyboard.Closed -> {
                focusManager.clearFocus()
                bottomPadding.value = 16
            }

            else -> {
                bottomPadding.value = 8
            }
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return container?.context?.let {
            ComposeView(it).apply {
                setContent {
                    AppTheme(darkTheme = true) {
                        Scaffold(
                            topBar = {
                                MainToolbar("", R.drawable.ic_back) {
                                    findNavController().popBackStack()
                                }
                            }
                        ) {
                            SignUpWithEmailFragmentContent()
                        }
                    }
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearStates()
    }
    @Preview(showSystemUi = true, showBackground = true)
    @Composable
    fun PreviewEmailTextField() {
        Column {
            //EmailTextField()
            //EmailTextField()
        }
    }
}