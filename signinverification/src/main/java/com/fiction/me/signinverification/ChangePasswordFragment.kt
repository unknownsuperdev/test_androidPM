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
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fiction.me.signinverification.dialog.ProgressDialog
import com.fiction.me.signinverification.views.BaseTitle
import com.fiction.me.signinverification.views.MainButton
import com.fiction.me.signinverification.views.MainToolbar
import com.fiction.me.signinverification.views.PasswordBaseTextFieldView
import com.fiction.me.theme.AppTheme
import com.fiction.me.utils.keyboardAsState
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordFragment : Fragment() {

    val viewModel: ChangePasswordViewModel by viewModel()
    val args: ChangePasswordFragmentArgs by navArgs()

    @Composable
    fun ChangePasswordFragmentContent() {
        LaunchedEffect(Unit) {
            viewModel
                .isCodeConfirmed
                .collect {
                    if (it) {
                        setFragmentResult(
                            FROM_CHANGE_PASSWORD_SCREEN,
                            bundleOf(IS_CHANGE_PASSWORD to true)
                        )
                        findNavController().popBackStack(R.id.nav_authorization, true)
                    }
                }
        }
        val focusManager = LocalFocusManager.current
        val progressAnimation = remember {
            mutableStateOf(false)
        }
        val bottomPadding = remember {
            mutableStateOf(16)
        }
        viewModel.run {
            fieldRequired = stringResource(id = R.string.field_required)
            identicalPass = stringResource(id = R.string.identical_pass)
            invalidPass = stringResource(id = R.string.invalid_pass)
            viewModel.getButtonState()
            ProgressDialog(progressAnimation)
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                Column {
                    BaseTitle(R.string.change_password)
                    PasswordBaseTextFieldView(
                        hintPasswordExpanded = stringResource(id = R.string.enter_your_password),
                        hintPasswordCollapsed = stringResource(id = R.string.new_password),
                        password = viewModel.passwordState,
                        helperStatusType = viewModel.passwordHelperStatusType,
                        helperText = viewModel.passwordHelperText,
                        isStatusNotNone = viewModel.isPasswordStatusNotNone
                    ) { password, isFocused ->
                        viewModel.password = password
                        viewModel.getPasswordValidation(isFocused)
                    }
                    PasswordBaseTextFieldView(
                        hintPasswordExpanded = stringResource(id = R.string.repeat_your_password),
                        hintPasswordCollapsed = stringResource(id = R.string.confirm_new_password),
                        password = viewModel.repeatPassState,
                        helperStatusType = viewModel.repeatPassHelperStatusType,
                        helperText = viewModel.repeatPassHelperText,
                        isStatusNotNone = viewModel.isRepeatPassStatusNotNone
                    ) { password, isFocused ->
                        viewModel.repeatPassword = password
                        viewModel.getRepeatPasswordValidation(isFocused)
                    }
                }
                MainButton(
                    isEnable = viewModel.isEnabledConfirmBtn,
                    text = stringResource(id = R.string.change_password),
                    isShowProgressBar = viewModel.isShowProgressBar,
                    bottomPadding = bottomPadding
                ) {
                    focusManager.clearFocus()
                    viewModel.resetPassword(args.token)
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
                            ChangePasswordFragmentContent()
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
            //EmailTextField()
            //EmailTextField()
        }
    }

    companion object {
        const val FROM_CHANGE_PASSWORD_SCREEN = "from change password screen"
        const val IS_CHANGE_PASSWORD = "Is change password"
    }
}
