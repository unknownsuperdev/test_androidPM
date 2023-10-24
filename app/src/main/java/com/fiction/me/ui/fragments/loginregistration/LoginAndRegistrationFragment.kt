package com.fiction.me.ui.fragments.loginregistration

import android.content.Intent
import android.net.Uri
import android.view.View
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.appbase.viewmodel.BaseViewModel
import com.fiction.me.databinding.FragmentLoginAndRegistrationBinding
import com.fiction.me.extensions.makeLinks
import com.fiction.me.utils.Constants
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginAndRegistrationFragment :
    FragmentBaseNCMVVM<BaseViewModel, FragmentLoginAndRegistrationBinding>() {
    override val binding: FragmentLoginAndRegistrationBinding by viewBinding()
    override val viewModel: BaseViewModel by viewModel()

    override fun onView() {
        with(binding) {
            agreeTermsAndPolicy.makeLinks(
                Pair(resources.getString(R.string.terms_of_use), View.OnClickListener {
                    with(Intent(Intent.ACTION_VIEW)) {
                        data = Uri.parse(Constants.TERMS_OF_USE)
                        startActivity(this)
                    }
                }),
                Pair(resources.getString(R.string.privacy_policy), View.OnClickListener {
                    with(Intent(Intent.ACTION_VIEW)) {
                        data = Uri.parse(Constants.PRIVACY_POLICY)
                        startActivity(this)
                    }
                })
            )
        }
    }

    override fun onViewClick() {
        super.onViewClick()
        with(binding) {
            signUp.setOnClickListener {
                val directions =
                    LoginAndRegistrationFragmentDirections.actionLoginAndRegistrationFragmentToSignInOrSignUpFragment(
                        LoginRegisterTypes.SIGN_UP
                    )
                navigateFragment(directions)
            }
            signIn.setOnClickListener {
                val directions =
                    LoginAndRegistrationFragmentDirections.actionLoginAndRegistrationFragmentToSignInOrSignUpFragment(
                        LoginRegisterTypes.SIGN_IN
                    )
                navigateFragment(directions)
            }
            closeBtn.setOnClickListener {
              popBackStack()
            }
        }
    }
}