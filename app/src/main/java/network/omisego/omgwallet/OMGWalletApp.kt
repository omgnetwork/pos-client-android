package network.omisego.omgwallet

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 13/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import co.omisego.omisego.model.ClientAuthenticationToken
import com.facebook.stetho.Stetho
import network.omisego.omgwallet.data.LocalRepository
import network.omisego.omgwallet.data.RemoteRepository
import network.omisego.omgwallet.model.Credential
import network.omisego.omgwallet.network.ClientProvider
import network.omisego.omgwallet.network.ProductionClientSetup
import network.omisego.omgwallet.util.ContextUtil
import network.omisego.omgwallet.util.RepositoryUtil

class OMGWalletApp : Application(), LoginListener {
    override fun onCreate() {
        super.onCreate()
        ContextUtil.context = applicationContext
        RepositoryUtil.localRepository = LocalRepository()
        RepositoryUtil.remoteRepository = RemoteRepository()
        ClientProvider.initHTTPClient(ProductionClientSetup())
        RepositoryUtil.localRepository.loadCredential().authenticationToken?.let {
            ClientProvider.initSocketClient(it)
        }
        Stetho.initializeWithDefaults(this)
    }

    override fun onLoggedin(email: String, token: ClientAuthenticationToken) {
        val credential = Credential(token.authenticationToken)
        RepositoryUtil.localRepository.clearOldAccountCache(email)
        RepositoryUtil.localRepository.saveUserEmail(email)
        RepositoryUtil.localRepository.saveUser(token.user)
        RepositoryUtil.localRepository.saveCredential(credential)
        ClientProvider.initSocketClient(credential.authenticationToken!!)
    }

    override fun onLoggedout() {
        RepositoryUtil.remoteRepository.stopListeningToUserSocketEvent()
        RepositoryUtil.localRepository.clearSession()
    }
}

interface LoginListener {
    fun onLoggedin(email: String, token: ClientAuthenticationToken)
    fun onLoggedout()
}