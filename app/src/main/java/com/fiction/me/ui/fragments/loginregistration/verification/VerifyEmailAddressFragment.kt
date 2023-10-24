package com.fiction.me.ui.fragments.loginregistration.verification

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fiction.me.R
import com.fiction.me.baseui.theme.*
import com.fiction.me.signinverification.views.BaseTitle
import com.fiction.me.signinverification.views.DefaultSnackbar
import com.fiction.me.signinverification.views.DescriptionText
import com.fiction.me.signinverification.views.MainToolbar
import com.fiction.me.theme.AppTheme
import com.fiction.me.theme.Shape
import com.fiction.me.theme.robotoFontFamily
import com.fiction.me.ui.MainActivity
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel

class VerifyEmailAddressFragment : Fragment() {

    val viewModel: VerifyEmailAddressViewModel by viewModel()
    val args: VerifyEmailAddressFragmentArgs by navArgs()
    val snackBarIcon = mutableStateOf(R.drawable.ic_success)

    @Composable
    fun VerifyEmailAddressFragmentContent(snackbarHostState: ScaffoldState) {
        val label = stringResource(id = R.string.close)

        val resendEmailTxtColor = remember {
            mutableStateOf(SecondaryPurpleDark600)
        }

        LaunchedEffect(Unit) {
            viewModel
                .resendEmail
                .collect { message ->
                    message?.let {
                        snackBarIcon.value =
                            if (viewModel.isError) R.drawable.ic_error else R.drawable.ic_success
                        snackbarHostState.snackbarHostState.showSnackbar(
                            message = it,
                            actionLabel = label,
                        )
                        delay(10000)
                        resendEmailTxtColor.value = SecondaryPurpleDark600
                        viewModel.isResendEmail = true
                    }
                }
        }

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Column {
                Image(
                    painterResource(id = R.drawable.ic_message_to),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 16.dp),
                    //tint = Color.White
                )
                BaseTitle(R.string.verify_email_address)
                val annotatedString = buildAnnotatedString {
                    append(resources.getString(R.string.verify_email_description_start))
                    withStyle(
                        style = SpanStyle(
                            color = PrimaryWhite,
                            fontWeight = FontWeight.Medium
                        )
                    ) {
                        append(" ${args.email} ")// TODO ${args.forgotPass.email}
                    }
                    append(resources.getString(R.string.verify_email_description_end))
                }
                DescriptionText(annotatedString, Gray)
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 92.dp, bottom = 20.dp),
                    text = stringResource(id = R.string.verify_email_address),
                    style = TextStyle(
                        color = PrimaryWhite,
                        fontSize = 17.sp,
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight.Medium
                    )
                )

                val resendEmail = stringResource(id = R.string.resend_email)
                val annotatedClickableString = buildAnnotatedString {
                    append(resources.getString(R.string.check_your_spam))
                    withStyle(style = SpanStyle(color = resendEmailTxtColor.value)) {
                        pushStringAnnotation(tag = resendEmail, annotation = resendEmail)
                        append(" $resendEmail")
                    }
                    append(".")
                }
                ClickableText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    text = annotatedClickableString,
                    style = TextStyle(
                        color = Gray,
                        fontSize = 15.sp,
                        letterSpacing = 0.15.sp,
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight.Normal
                    ),
                    onClick = { offset ->
                        annotatedClickableString.getStringAnnotations(offset, offset)
                            .firstOrNull()?.let { span ->
                                Log.i("ClickableText", "Clicked on ${span.item}")
                                if (span.item == resendEmail && viewModel.isResendEmail) {
                                    viewModel.resendEmailForVerify()
                                    resendEmailTxtColor.value = SecondaryDark400
                                }
                            }
                    })
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
        return container?.context?.let {
            ComposeView(it).apply {
                setContent {
                    val scaffoldState = rememberScaffoldState()
                    AppTheme(darkTheme = true) {
                        Scaffold(
                            topBar = {
                                MainToolbar("", R.drawable.ic_close) {
                                    /*val direction = VerifyEmailAddressFragmentDirections.actionVerifyEmailAddressFragmentToExploreFragment()
                                    findNavController().navigate(direction)*/
                                    reopenApp()
                                }
                            },
                            scaffoldState = scaffoldState,
                            snackbarHost = {
                                scaffoldState.snackbarHostState
                            }
                        ) {
                            VerifyEmailAddressFragmentContent(scaffoldState)
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
                            BackHandler {
                                reopenApp()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun reopenApp() {
        (activity as? MainActivity)?.setIsClearBackStackValue(true)
        findNavController().popBackStack(R.id.profileFragment, true)
    }
}