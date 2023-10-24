package com.fiction.me.ui.fragments.loginregistration.registrationproviders

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.fiction.me.BuildConfig
import com.fiction.me.ui.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class GoogleSignInWrapper(
    private val auth: FirebaseAuth
) : GoogleSignInProvider {

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var context: Context
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var userTokenListener: (String) -> Unit

    override fun setResultLauncher(resultLauncher: ActivityResultLauncher<Intent>) {
        this.resultLauncher = resultLauncher
    }

    override fun signIn(context: Context, resultListener: (String) -> Unit) {
        this.context = context
        userTokenListener = resultListener
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.DEFAULT_WEB_CLIENT)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(context, gso)

        googleSignInClient.signOut()
        val signInIntent = googleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    override fun handleSignInResult(result: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(result)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            firebaseAuthWithGoogle(credential)
        } catch (e: ApiException) {
            Toast.makeText(context, "$e", Toast.LENGTH_SHORT).show()
        }
    }

    private fun firebaseAuthWithGoogle(credential: AuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(context as MainActivity) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.uid?.let { uid ->
                        userTokenListener(uid)
                    }
                    //Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                } else {
                    task.exception?.printStackTrace()
                    Toast.makeText(
                        context,
                        "Authentication failed." + task.exception?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}