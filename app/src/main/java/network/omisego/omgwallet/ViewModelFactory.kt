package network.omisego.omgwallet

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import network.omisego.omgwallet.pages.MainRepository
import network.omisego.omgwallet.pages.MainViewModel
import network.omisego.omgwallet.pages.balance.BalanceRepository
import network.omisego.omgwallet.pages.balance.BalanceViewModel
import network.omisego.omgwallet.pages.signup.SignupRepository
import network.omisego.omgwallet.pages.signup.SignupViewModel
import network.omisego.omgwallet.pages.splash.PreloadResourceRepository
import network.omisego.omgwallet.pages.splash.PreloadResourceViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                return MainViewModel(MainRepository()) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                return SignupViewModel(SignupRepository()) as T
            }
            modelClass.isAssignableFrom(PreloadResourceViewModel::class.java) -> {
                return PreloadResourceViewModel(PreloadResourceRepository()) as T
            }
            modelClass.isAssignableFrom(BalanceViewModel::class.java) -> {
                return BalanceViewModel(BalanceRepository()) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}
