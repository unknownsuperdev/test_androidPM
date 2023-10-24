package com.fiction.me.signinverification

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fiction.me.signinverification.views.BaseTitle
import com.fiction.me.baseui.theme.Black300
import com.fiction.me.baseui.theme.Gray
import com.fiction.me.baseui.theme.PrimaryWhite
import com.fiction.me.baseui.theme.SecondaryPurpleDark600
import com.fiction.me.extention.safeNavigate
import com.fiction.me.signinverification.dialog.ProgressDialog
import com.fiction.me.signinverification.views.*
import com.fiction.me.theme.AppTheme
import com.fiction.me.theme.Shape
import com.fiction.me.theme.robotoFontFamily
import org.koin.androidx.viewmodel.ext.android.viewModel


class RecoveryEmailFragment : Fragment() {

    val viewModel: RecoveryEmailViewModel by viewModel()
    private val pinCodeLength = 6
    private var startMode: Int? = null
    private val snackBarIcon = mutableStateOf(R.drawable.ic_success)
    val args: RecoveryEmailFragmentArgs by navArgs()

    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun RecoveryEmailFragmentContent(snackbarHostState: ScaffoldState) {
        val label = stringResource(id = R.string.close)

        LaunchedEffect(Unit) {
            viewModel
                .isCodeConfirmed
                .collect { token ->
                    token?.let {
                        val direction =
                            RecoveryEmailFragmentDirections.actionRecoveryEmailFragmentToChangePasswordFragment(
                                it
                            )
                        findNavController().safeNavigate(
                            direction
                        )
                    }
                }
        }
        LaunchedEffect(Unit) {
            viewModel
                .isShowSnackbar
                .collect { message ->
                    snackBarIcon.value =
                        if (viewModel.isError) R.drawable.ic_error else R.drawable.ic_success
                    snackbarHostState.snackbarHostState.showSnackbar(
                        message = message ?: message,
                        actionLabel = label,
                    )
                }
        }

        ProgressDialog(viewModel.progressAnimation)

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Column {
                BaseTitle(R.string.recovery_email)
                val annotatedString = buildAnnotatedString {
                    append(resources.getString(R.string.recovery_email_description_start))
                    withStyle(style = SpanStyle(SecondaryPurpleDark600)) {
                        append(" ${args.forgotPass.email}")
                    }
                    append(resources.getString(R.string.recovery_email_description_end))
                }
                DescriptionText(annotatedString, PrimaryWhite)
                RecoverCodeText()
                PinCodeView()
                ResendCodeText()
            }

            MainButton(viewModel.isButtonEnable) {
                viewModel.checkResetCode(args.forgotPass.email)
                /* val direction =
                     RecoveryEmailFragmentDirections.actionRecoveryEmailFragmentToChangePasswordFragment()
                 findNavController().safeNavigate(
                     direction
                 )*/
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        startMode = activity?.window?.attributes?.softInputMode
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        // Inflate the layout for this fragment
        return container?.context?.let {
            ComposeView(it).apply {
                setContent {
                    val scaffoldState = rememberScaffoldState()
                    AppTheme(darkTheme = true) {
                        Scaffold(
                            topBar = {
                                MainToolbar("", R.drawable.ic_back) {
                                    findNavController().popBackStack()
                                }
                            },
                            scaffoldState = scaffoldState,
                            snackbarHost = {
                                scaffoldState.snackbarHostState
                            }
                        ) {
                            RecoveryEmailFragmentContent(scaffoldState)
                            val message = stringResource(id = R.string.new_code_to_email)
                            args.forgotPass.let { viewModel.showSnackBar(it.message ?: message) }
                            Box {
                                DefaultSnackbar(
                                    snackbarHostState = scaffoldState.snackbarHostState,
                                    onDismiss = {
                                        scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                                    },
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .align(Alignment.TopCenter)
                                        .background(
                                            Black300, shape = Shape.medium
                                        ),
                                    icDrawable = snackBarIcon
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ResendCodeText() {
        if (viewModel.isResendCodeShown.value) {
            TextButton(
                //modifier = Modifier.padding(start = 8.dp, end = 48.dp),
                onClick = {
                    viewModel.run {
                        isResendCodeShown.value = false
                        sendEmailForGetCode(args.forgotPass.email)
                    }
                }
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    textAlign = TextAlign.Center,
                    text = stringResource(id = R.string.resend_code),
                    style = TextStyle(
                        color = SecondaryPurpleDark600,
                        fontSize = 16.sp,
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight.Medium
                    )
                )

            }
        }
    }

    @Composable
    fun RecoverCodeText() {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 16.dp, bottom = 8.dp),
            text = stringResource(id = R.string.recovery_code),
            style = TextStyle(
                color = Gray,
                fontSize = 11.sp,
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Medium
            )
        )
    }

    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun PinCodeView() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)

        ) {
            SmsCodeView(
                smsCodeLength = pinCodeLength,
                textFieldColors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color.Transparent,
                    focusedIndicatorColor = PrimaryWhite,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = Black300
                ),
                textStyle = TextStyle(
                    color = PrimaryWhite,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = robotoFontFamily,
                    fontWeight = FontWeight.Normal
                ),
                smsFulled = {
                    viewModel.smsCodeNumber.value = it
                    viewModel.isButtonEnable.value = it.length == pinCodeLength
                    if (it.length == pinCodeLength)
                        viewModel.isResendCodeShown.value = true
                },
                getEachPinItemWidth(),
                viewModel.isErrorStatus
            )
        }
    }

    @Composable
    fun getEachPinItemWidth(): Float {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp
        val spaceAroundPins = 72.dp
        return (screenWidth - spaceAroundPins) / pinCodeLength.dp
    }

    override fun onDestroyView() {
        super.onDestroyView()
        startMode?.let { activity?.window?.setSoftInputMode(it) }
        viewModel.isResendCodeShown.value = false
        viewModel.isButtonEnable.value = false
    }
}
