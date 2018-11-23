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
import network.omisego.omgwallet.screen.auth.balance.detail.BalanceDetailItemViewModel
import network.omisego.omgwallet.screen.auth.balance.detail.BalanceDetailViewModel
import network.omisego.omgwallet.screen.auth.confirm.ConfirmTransactionRequestViewModel
import network.omisego.omgwallet.screen.auth.profile.main.ProfileViewModel
import network.omisego.omgwallet.screen.auth.profile.transaction.TransactionListTransformer
import network.omisego.omgwallet.screen.auth.profile.transaction.TransactionListViewModel
import network.omisego.omgwallet.screen.auth.showqr.ShowQRViewModel
import network.omisego.omgwallet.screen.auth.splash.PreloadResourceViewModel
import network.omisego.omgwallet.screen.unauth.signin.FingerprintBottomSheetViewModel
import network.omisego.omgwallet.screen.unauth.signin.SignInViewModel
import network.omisego.omgwallet.util.BiometricUtil
import network.omisego.omgwallet.util.RepositoryUtil

@Suppress("UNCHECKED_CAST")
class AndroidViewModelFactory(private val application: Application) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignInViewModel::class.java) -> {
                return SignInViewModel(application, RepositoryUtil.localRepository, RepositoryUtil.remoteRepository, BiometricUtil()) as T
            }
            modelClass.isAssignableFrom(FingerprintBottomSheetViewModel::class.java) -> {
                FingerprintBottomSheetViewModel(application) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                return ProfileViewModel(application, RepositoryUtil.localRepository) as T
            }
            modelClass.isAssignableFrom(TransactionListViewModel::class.java) -> {
                return TransactionListViewModel(application, RepositoryUtil.localRepository, RepositoryUtil.remoteRepository, TransactionListTransformer(application)) as T
            }
            modelClass.isAssignableFrom(ShowQRViewModel::class.java) -> {
                return ShowQRViewModel(application, RepositoryUtil.localRepository, QRGenerator()) as T
            }
            modelClass.isAssignableFrom(BalanceDetailItemViewModel::class.java) -> {
                return BalanceDetailItemViewModel(application) as T
            }
            modelClass.isAssignableFrom(BalanceDetailViewModel::class.java) -> {
                return BalanceDetailViewModel(application, RepositoryUtil.localRepository) as T
            }
            modelClass.isAssignableFrom(PreloadResourceViewModel::class.java) -> {
                return PreloadResourceViewModel(application, RepositoryUtil.localRepository, RepositoryUtil.remoteRepository) as T
            }
            modelClass.isAssignableFrom(ConfirmTransactionRequestViewModel::class.java) -> {
                return ConfirmTransactionRequestViewModel(application, RepositoryUtil.localRepository, RepositoryUtil.remoteRepository) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}
