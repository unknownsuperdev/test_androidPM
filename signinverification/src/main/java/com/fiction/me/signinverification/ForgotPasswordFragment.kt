package com.fiction.me.signinverification

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.fiction.me.signinverification.views.BaseTitle
import com.fiction.me.extention.isValidEmail
import com.fiction.me.extention.safeNavigate
import com.fiction.me.signinverification.dialog.ProgressDialog
import com.fiction.me.signinverification.views.*
import com.fiction.me.theme.AppTheme
import com.fiction.me.utils.keyboardAsState
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForgotPasswordFragment : Fragment() {

    val viewModel: ForgotPasswordViewModel by viewModel()

    @Composable
    fun ForgotPasswordFragmentContent() {
        LaunchedEffect(Unit) {
            viewModel
                .forgotPassword
                .collect { data ->
                    val direction =
                        ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToRecoveryEmailFragment(
                            data
                        )
                    findNavController().safeNavigate(
                        direction
                    )
                }
        }
        val focusManager = LocalFocusManager.current
        val bottomPadding = remember {
            mutableStateOf(16)
        }
        viewModel.fieldRequired = stringResource(id = R.string.field_required)
        viewModel.enterCorrectEmail = stringResource(id = R.string.enter_correct_email)
        viewModel.getButtonState()
        ProgressDialog(viewModel.progressAnimation)
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Column {
                BaseTitle(R.string.forgot_password)
                DescriptionText(R.string.forgot_password_description)
                EmailBaseTextFieldView(
                    paddingTop = 32,
                    email = viewModel.emailState,
                    helperStatusType = viewModel.emailHelperStatusType,
                    helperText = viewModel.emailHelperText,
                    isStatusNotNone = viewModel.isEmailStatusNotNone,
                ){ email, isFocused ->
                    viewModel.email = email
                    viewModel.getMailValidation(isFocused)
                }
                when (keyboardAsState().value) {
                    com.fiction.me.utils.Keyboard.Closed -> {
                        focusManager.clearFocus()
                        bottomPadding.value = 16
                    }
                    else -> {
                        bottomPadding.value = 8
                    }
                }
            }

            MainButton(isEnable = viewModel.isEnableButton) {
                viewModel.sendEmailForGetCode()
            }
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
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
                            ForgotPasswordFragmentContent()
                        }
                    }
                }
            }
        }
    }

    @Preview(showSystemUi = true, showBackground = true)
    @Composable
    fun PreviewEmailTextField() {
        Column {
            // EmailTextField()
            //EmailTextField()
        }
    }
}
