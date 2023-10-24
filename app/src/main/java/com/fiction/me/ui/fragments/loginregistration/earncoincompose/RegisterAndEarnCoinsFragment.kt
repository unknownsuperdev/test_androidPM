package com.fiction.me.ui.fragments.loginregistration.earncoincompose

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fiction.me.R
import com.fiction.me.extention.safeNavigate
import com.fiction.me.signinverification.views.BaseCenterTitle
import com.fiction.me.signinverification.views.DescriptionCenterText
import com.fiction.me.signinverification.views.MainButton
import com.fiction.me.signinverification.views.MainToolbar
import com.fiction.me.theme.AppTheme
import com.fiction.me.theme.MyTypography

class RegisterAndEarnCoinsFragment : Fragment() {

    @Composable
    fun RegisterAndEarnCoinsFragmentContent() {

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 14.dp)
            ) {
                Image(
                    painterResource(id = R.drawable.ic_earn_coins),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 56.dp)
                        .align(Alignment.CenterHorizontally)
                )
                BaseCenterTitle(R.string.earn_free_coins)
                DescriptionCenterText(
                    stringResource(id = R.string.earn_free_coins_description),
                    MyTypography.body2
                )
            }
            MainButton(text = stringResource(id = R.string.continue_)) {
               /* val direction =
                    RegisterAndEarnCoinsFragmentDirections.actionRegisterAndEarnCoinsFragmentToSignUpFragment()
                findNavController().safeNavigate(direction)*/
            }
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.context?.let {
            ComposeView(it).apply {
                setContent {
                    AppTheme(darkTheme = true) {
                        Scaffold(
                            topBar = {
                                MainToolbar("", R.drawable.ic_close) {
                                    findNavController().popBackStack()
                                }
                            }
                        ) {
                            RegisterAndEarnCoinsFragmentContent()
                        }
                    }
                }
            }
        }
    }
}