package network.omisego.omgwallet

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import network.omisego.omgwallet.data.LocalRepository
import network.omisego.omgwallet.data.RemoteRepository
import network.omisego.omgwallet.pages.profile.main.ProfileViewModel
import network.omisego.omgwallet.pages.signin.FingerprintBottomSheetViewModel
import network.omisego.omgwallet.pages.signin.SignInViewModel
import network.omisego.omgwallet.util.BiometricUtil

@Suppress("UNCHECKED_CAST")
class AndroidViewModelFactory(private val application: Application) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignInViewModel::class.java) -> {
                return SignInViewModel(application, LocalRepository(), RemoteRepository(), BiometricUtil()) as T
            }
            modelClass.isAssignableFrom(FingerprintBottomSheetViewModel::class.java) -> {
                FingerprintBottomSheetViewModel(application) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                return ProfileViewModel(application, LocalRepository()) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}
