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
import network.omisego.omgwallet.pages.MainViewModel
import network.omisego.omgwallet.pages.balance.BalanceNavigationViewModel
import network.omisego.omgwallet.pages.balance.BalanceViewModel
import network.omisego.omgwallet.pages.profile.ProfileNavigationViewModel
import network.omisego.omgwallet.pages.profile.main.ConfirmFingerprintViewModel
import network.omisego.omgwallet.pages.signup.SignupViewModel
import network.omisego.omgwallet.pages.splash.PreloadResourceViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                return MainViewModel(LocalRepository()) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                return SignupViewModel(RemoteRepository()) as T
            }
            modelClass.isAssignableFrom(PreloadResourceViewModel::class.java) -> {
                return PreloadResourceViewModel(LocalRepository(), RemoteRepository()) as T
            }
            modelClass.isAssignableFrom(BalanceViewModel::class.java) -> {
                return BalanceViewModel(LocalRepository(), RemoteRepository()) as T
            }
            modelClass.isAssignableFrom(ConfirmFingerprintViewModel::class.java) -> {
                return ConfirmFingerprintViewModel(LocalRepository(), RemoteRepository()) as T
            }
            modelClass.isAssignableFrom(ProfileNavigationViewModel::class.java) -> {
                return ProfileNavigationViewModel() as T
            }
            modelClass.isAssignableFrom(BalanceNavigationViewModel::class.java) -> {
                return BalanceNavigationViewModel() as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}
