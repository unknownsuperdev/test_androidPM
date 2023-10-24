package com.fiction.me.ui.fragments.loginregistration.signin

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.fiction.me.R
import com.fiction.me.baseui.theme.Black300
import com.fiction.me.signinverification.ChangePasswordFragment.Companion.FROM_CHANGE_PASSWORD_SCREEN
import com.fiction.me.signinverification.ChangePasswordFragment.Companion.IS_CHANGE_PASSWORD
import com.fiction.me.signinverification.views.DefaultSnackbar
import com.fiction.me.theme.AppTheme
import com.fiction.me.theme.Shape
import org.koin.androidx.compose.koinViewModel

class SignInWithMailFragment : Fragment() {

    lateinit var viewModel: SignInWithMailViewModel
    val iconDrawable = mutableStateOf(R.drawable.ic_success)
    @OptIn(ExperimentalMaterialApi::class)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return container?.context?.let {
            ComposeView(it).apply {
                setContent {
                    val scaffoldState = rememberScaffoldState()
                    val label = stringResource(id = R.string.close)
                    viewModel = koinViewModel()
                    AppTheme(darkTheme = true) {
                        val navController = findNavController()
                        SignInWithEmailScreen(navController, viewModel)
                        val message = stringResource(id = R.string.password_changed)
                        LaunchedEffect(Unit) {
                            viewModel
                                .isShowSnackbar
                                .collect {
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        message = message,
                                        actionLabel = label,
                                    )
                                }
                        }
                        Box {
                            DefaultSnackbar(
                                snackbarHostState = scaffoldState.snackbarHostState,
                                onDismiss = {
                                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                                },
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp, top = 56.dp)
                                    .align(Alignment.TopCenter)
                                    .background(
                                        Black300, shape = Shape.medium
                                    ),
                                icDrawable = iconDrawable
                            )
                        }
                        getChangePassFragmentData()
                    }
                }
            }
        }
    }

    private fun getChangePassFragmentData() {
        setFragmentResultListener(FROM_CHANGE_PASSWORD_SCREEN) { requestKey, bundle ->
            val isChangedPass = bundle.getBoolean(IS_CHANGE_PASSWORD)
            if (isChangedPass) viewModel.showSnackBar()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearStates()
    }
}