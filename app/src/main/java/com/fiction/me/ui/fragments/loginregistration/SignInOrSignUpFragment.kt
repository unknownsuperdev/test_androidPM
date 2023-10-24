package com.fiction.me.ui.fragments.loginregistration

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentSigninOrSignupBinding
import com.fiction.me.extensions.makeLinks
import com.fiction.me.ui.MainActivity
import com.fiction.me.ui.fragments.loginregistration.registrationproviders.AppleSignInProvider
import com.fiction.me.ui.fragments.loginregistration.registrationproviders.FacebookSignInProvider
import com.fiction.me.ui.fragments.loginregistration.registrationproviders.GoogleSignInProvider
import com.fiction.me.ui.fragments.reader.custom_compose_dialog.ProgressDialog
import com.fiction.me.utils.Constants
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInOrSignUpFragment :
    FragmentBaseNCMVVM<SignInOrSignUpViewModel, FragmentSigninOrSignupBinding>() {
    override val binding: FragmentSigninOrSignupBinding by viewBinding()
    override val viewModel: SignInOrSignUpViewModel by viewModel()
    private val args: SignInOrSignUpFragmentArgs by navArgs()
    private val fbSignInProvider: FacebookSignInProvider by inject()
    private val appleSignInProvider: AppleSignInProvider by inject()
    private val googleSignInProvider: GoogleSignInProvider by inject()

    var name: String? = null
    var email: String? = null
    var activity: Activity? = null

    private val getResultForGoogle =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                viewModel.progressState.value = true
                googleSignInProvider.handleSignInResult(it.data)
            }
        }

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
            toolbar.run {
                setBackBtnIcon(R.drawable.ic_back)
            }
            titleAuth.text = when (args.type) {
                LoginRegisterTypes.SIGN_UP -> context?.getString(R.string.sign_up_screen_title)
                else -> context?.getString(R.string.sign_in_screen_title)
            }
            binding.progressDialog.setContent {
                ProgressDialog(viewModel.progressState)
            }
        }
    }

    override fun onViewClick() {
        with(binding) {
            signInWithEmailBtn.setOnClickListener {
                val directions = when (args.type) {
                    LoginRegisterTypes.SIGN_UP -> SignInOrSignUpFragmentDirections.actionSignInOrSignUpFragmentToSignUpWithEmailFragment()
                    else -> SignInOrSignUpFragmentDirections.actionSignInOrSignUpFragmentToSignInWithMailFragment()
                }
                navigateFragment(directions)
            }
            signInWithFacebookBtn.setOnClickListener {
                fbSignInProvider.signIn(
                    this@SignInOrSignUpFragment
                ) { identifier, isShowProgress ->
                    if (isShowProgress) viewModel.progressState.value = true
                    else viewModel.getFbRegisterToken(identifier)
                }
            }
            signInWithGoogleBtn.setOnClickListener {
                if (context is MainActivity)
                    context?.let {
                        googleSignInProvider.setResultLauncher(getResultForGoogle)
                        googleSignInProvider.signIn(it) { identifier ->
                            viewModel.getGoogleRegisterToken(identifier)
                        }
                    }
            }
            signInWithAppleBtn.setOnClickListener {
                viewModel.progressState.value = true
                context?.let {
                    appleSignInProvider.signIn(it) { token, isCancel ->
                        if (isCancel)
                            viewModel.progressState.value = false
                        else
                            viewModel.getAppleRegisterToken(token)
                    }
                }
            }
            toolbar.setOnBackBtnClickListener {
                popBackStack()
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.loginToken) {
            (context as? MainActivity)?.setIsClearBackStackValue(true)
            findNavController().popBackStack(R.id.profileFragment, true)
        }
    }

}