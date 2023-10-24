package com.fiction.me.di

import com.fiction.me.signinverification.ChangePasswordViewModel
import com.fiction.me.signinverification.ForgotPasswordViewModel
import com.fiction.me.signinverification.RecoveryEmailViewModel
//import com.fiction.me.ui.fragments.loginregistration.SignUpWithEmailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModuleAuthorization = module {
    viewModel { RecoveryEmailViewModel(get(), get()) }
    viewModel { ForgotPasswordViewModel(get()) }
    viewModel { ChangePasswordViewModel(get()) }
    //viewModel { com.fiction.me.ui.fragments.loginregistration.SignUpWithEmailViewModel(get()) }
}