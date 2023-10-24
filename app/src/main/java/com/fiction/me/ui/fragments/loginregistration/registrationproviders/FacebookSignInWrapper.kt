package com.fiction.me.ui.fragments.loginregistration.registrationproviders

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth

class FacebookSignInWrapper(
    val auth: FirebaseAuth
) : FacebookSignInProvider {

    private val callbackManager: CallbackManager = CallbackManager.Factory.create()
    private lateinit var userTokenListener: (String, Boolean) -> Unit
    private lateinit var fragment: Fragment
    private val loginManager = LoginManager.getInstance()

    override fun signIn(
        context: Fragment,
        resultListener: (String, Boolean) -> Unit
    ) {
        this.fragment = context
        userTokenListener = resultListener
        signInWithFB()
    }

    private fun signInWithFB() {
        loginManager
            .logInWithReadPermissions(fragment, callbackManager, listOf("public_profile", "email"))

        loginManager
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                    Log.i("FBAuth", "onCancel")
                }

                override fun onError(error: FacebookException) {
                    Log.i("FBAuth", "onError: ${error.message}")
                }

                override fun onSuccess(result: LoginResult) {
                    userTokenListener("", true)
                    Log.i("FBAuth", "onSuccess: ${result.accessToken}")
                    handleFacebookAccessToken(result.accessToken)
                }
            })
    }

    override fun signOut() {
        loginManager.logOut()
        auth.signOut()
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        fragment.activity.let {
            it?.let { it1 ->
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(it1) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser
                            user?.uid?.let { uid ->
                                userTokenListener(uid, false)
                            }
                        } else {
                            Toast.makeText(
                                it, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }
}