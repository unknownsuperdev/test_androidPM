package com.fiction.me.ui.fragments.loginregistration.registrationproviders

import android.content.Context
import android.widget.Toast
import com.fiction.me.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider

class AppleSignInWrapper(
    val auth: FirebaseAuth
) : AppleSignInProvider {

    private var provider: OAuthProvider.Builder = OAuthProvider.newBuilder(APPLE_URL)
    private lateinit var context: Context
    private lateinit var userTokenListener: (token: String, isCanceled: Boolean) -> Unit

    init {
        provider.scopes = listOf(EMAIL, NAME)
        provider.addCustomParameter(LOCALE, FR)
    }

    override fun signIn(context: Context, resultListener: (String, Boolean) -> Unit) {
        this.context = context
        userTokenListener = resultListener
        signUpWithApple()
    }

    private fun signUpWithApple() {
        (context as? MainActivity)?.let { mainContext ->
            auth.startActivityForSignInWithProvider(mainContext, provider.build())
                .addOnSuccessListener { authResult ->
                    authResult.user
                    val uid: String? = auth.currentUser?.uid
                    uid?.let {
                        userTokenListener(uid, false)
                    }
                }
                .addOnFailureListener { e ->
                    userTokenListener("", true)
                    Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    companion object {
        const val FR = "fr"
        const val LOCALE = "locale"
        const val APPLE_URL = "apple.com"
        const val USER_DATA_SUCCESS = "Signed in successfully"
        const val EMAIL = "email"
        const val NAME = "name"
    }
}