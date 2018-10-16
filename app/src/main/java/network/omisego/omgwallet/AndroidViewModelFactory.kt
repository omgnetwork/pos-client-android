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
import co.omisego.omisego.qrcode.generator.QRGenerator
import network.omisego.omgwallet.data.LocalRepository
import network.omisego.omgwallet.data.RemoteRepository
import network.omisego.omgwallet.screen.auth.balance.detail.BalanceDetailItemViewModel
import network.omisego.omgwallet.screen.auth.balance.detail.BalanceDetailViewModel
import network.omisego.omgwallet.screen.auth.profile.main.ProfileViewModel
import network.omisego.omgwallet.screen.auth.profile.transaction.TransactionListTransformer
import network.omisego.omgwallet.screen.auth.profile.transaction.TransactionListViewModel
import network.omisego.omgwallet.screen.auth.showqr.ShowQRViewModel
import network.omisego.omgwallet.screen.unauth.signin.FingerprintBottomSheetViewModel
import network.omisego.omgwallet.screen.unauth.signin.SignInViewModel
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
            modelClass.isAssignableFrom(TransactionListViewModel::class.java) -> {
                return TransactionListViewModel(application, LocalRepository(), RemoteRepository(), TransactionListTransformer(application)) as T
            }
            modelClass.isAssignableFrom(ShowQRViewModel::class.java) -> {
                return ShowQRViewModel(application, LocalRepository(), QRGenerator()) as T
            }
            modelClass.isAssignableFrom(BalanceDetailItemViewModel::class.java) -> {
                return BalanceDetailItemViewModel(application) as T
            }
            modelClass.isAssignableFrom(BalanceDetailViewModel::class.java) -> {
                return BalanceDetailViewModel(application, LocalRepository()) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}
