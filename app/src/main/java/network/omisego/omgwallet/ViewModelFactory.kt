package network.omisego.omgwallet

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import network.omisego.omgwallet.data.LocalRepository
import network.omisego.omgwallet.data.RemoteRepository
import network.omisego.omgwallet.screen.auth.AuthViewModel
import network.omisego.omgwallet.screen.auth.balance.BalanceViewModel
import network.omisego.omgwallet.screen.auth.profile.main.ConfirmFingerprintViewModel
import network.omisego.omgwallet.screen.auth.splash.PreloadResourceViewModel
import network.omisego.omgwallet.screen.unauth.signup.SignupViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                return SignupViewModel(RemoteRepository()) as T
            }
            modelClass.isAssignableFrom(BalanceViewModel::class.java) -> {
                return BalanceViewModel(LocalRepository(), RemoteRepository()) as T
            }
            modelClass.isAssignableFrom(ConfirmFingerprintViewModel::class.java) -> {
                return ConfirmFingerprintViewModel(LocalRepository(), RemoteRepository()) as T
            }
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                return AuthViewModel(LocalRepository()) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}
