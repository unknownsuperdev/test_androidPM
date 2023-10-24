package com.fiction.me.ui.fragments.loginregistration.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fiction.me.R
import com.fiction.me.baseui.theme.SecondaryPurpleDark600
import com.fiction.me.signinverification.views.EmailBaseTextFieldView
import com.fiction.me.signinverification.views.MainButton
import com.fiction.me.signinverification.views.PasswordBaseTextFieldView
import com.fiction.me.theme.MyTypography
import com.fiction.me.ui.MainActivity
import com.fiction.me.ui.fragments.loginregistration.ClickableTextView
import com.fiction.me.ui.fragments.loginregistration.LoginRegisterTypes
import com.fiction.me.ui.fragments.loginregistration.signin.collapsingtoolbar.CollapsingToolbar
import com.fiction.me.ui.fragments.loginregistration.signin.collapsingtoolbar.ExitUntilCollapsedState
import com.fiction.me.ui.fragments.loginregistration.signin.collapsingtoolbar.ToolbarState
import com.fiction.me.utils.Keyboard
import com.fiction.me.utils.keyboardAsState

private val MinToolbarHeight = 48.dp
private val MaxToolbarHeight = 108.dp

@Composable
private fun rememberToolbarState(toolbarHeightRange: IntRange): ToolbarState {
    return rememberSaveable(saver = ExitUntilCollapsedState.Saver) {
        ExitUntilCollapsedState(toolbarHeightRange)
    }
}

@Composable
fun SignInWithEmailScreen(navController: NavController, viewModel: SignInWithMailViewModel) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel
            .signedIn
            .collect {
                val activity = context as? MainActivity
                activity?.setIsClearBackStackValue(true)
                navController.popBackStack(R.id.profileFragment, true)
            }
    }
    val focusManager = LocalFocusManager.current
    val bottomPadding = remember {
        mutableStateOf(16)
    }
    viewModel.run {
        fieldRequired = stringResource(id = R.string.field_required)
        enterCorrectEmail = stringResource(id = R.string.enter_correct_email)
        invalidPass = stringResource(id = R.string.enter_correct_pass)
        getButtonState()
    }
    val toolbarHeightRange = with(LocalDensity.current) {
        MinToolbarHeight.roundToPx()..MaxToolbarHeight.roundToPx()
    }
    val toolbarState = rememberToolbarState(toolbarHeightRange)
    val scrollState = rememberScrollState()

    toolbarState.scrollValue = scrollState.value

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 96.dp)
                    .padding(bottom = 200.dp)
            ) {
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
                    hintPasswordExpanded = stringResource(id = R.string.enter_your_password),
                    hintPasswordCollapsed = stringResource(id = R.string.password),
                    password = viewModel.passwordState,
                    helperStatusType = viewModel.passwordHelperStatusType,
                    helperText = viewModel.passwordHelperText,
                    isStatusNotNone = viewModel.isPasswordStatusNotNone
                ) { password, isFocused ->
                    viewModel.password = password
                    viewModel.getPasswordValidation(isFocused)
                }
                ForgotPasswordText {
                    navController.navigate(R.id.nav_authorization)
                }
            }


        }
        CollapsingToolbar(
            progress = toolbarState.progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(with(LocalDensity.current) { toolbarState.height.toDp() })
                .graphicsLayer { translationY = toolbarState.offset }
        ) {
            navController.popBackStack()
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
    Box(
        modifier = Modifier.height(120.dp),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Column(modifier = Modifier.background(MaterialTheme.colors.background)) {
            ClickableTextView(
                stringResource(id = R.string.sign_up),
                stringResource(id = R.string.dont_have_an_account),
                paddingTop = 16
            ) {
                val directions =
                    SignInWithMailFragmentDirections.actionSignInWithMailFragmentToSignInOrSignUpFragment(
                        LoginRegisterTypes.SIGN_UP
                    )
                navController.navigate(directions)
            }

            MainButton(
                isEnable = viewModel.isEnabledConfirmBtn,
                text = stringResource(id = R.string.sign_in),
                viewModel.isShowProgressBar,
                bottomPadding
            ) {
                focusManager.clearFocus()
                viewModel.signIn()
            }
        }
    }
}

@Composable
fun ForgotPasswordText(
    onclickListener: () -> Unit
) {
    TextButton(
        onClick = {
            onclickListener()
        }
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.forgot_password),
            style = MyTypography.h3.copy(color = SecondaryPurpleDark600, fontSize = 16.sp)
        )
    }
}