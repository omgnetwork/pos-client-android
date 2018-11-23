package network.omisego.omgwallet

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import network.omisego.omgwallet.screen.auth.MainViewModel
import network.omisego.omgwallet.screen.auth.balance.BalanceViewModel
import network.omisego.omgwallet.screen.auth.profile.main.ConfirmFingerprintViewModel
import network.omisego.omgwallet.screen.unauth.signup.SignupViewModel
import network.omisego.omgwallet.util.RepositoryUtil

@Suppress("UNCHECKED_CAST")
class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                return SignupViewModel(RepositoryUtil.remoteRepository) as T
            }
            modelClass.isAssignableFrom(BalanceViewModel::class.java) -> {
                return BalanceViewModel(RepositoryUtil.localRepository, RepositoryUtil.remoteRepository) as T
            }
            modelClass.isAssignableFrom(ConfirmFingerprintViewModel::class.java) -> {
                return ConfirmFingerprintViewModel(RepositoryUtil.localRepository, RepositoryUtil.remoteRepository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                return MainViewModel(RepositoryUtil.localRepository, RepositoryUtil.remoteRepository) as T
            }
            modelClass.isAssignableFrom(GlobalViewModel::class.java) -> {
                return GlobalViewModel(RepositoryUtil.remoteRepository) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}
