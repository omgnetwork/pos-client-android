package network.omisego.omgwallet

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 13/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import com.facebook.stetho.Stetho
import network.omisego.omgwallet.repository.LocalRepository
import network.omisego.omgwallet.storage.SessionStorage
import network.omisego.omgwallet.storage.Storage
import network.omisego.omgwallet.util.ContextUtil
import network.omisego.omgwallet.util.RepositoryUtil

class OMGWalletApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        ContextUtil.context = applicationContext

        val storage = Storage.create(this)
        RepositoryUtil.localRepository = LocalRepository(
            storage,
            SessionStorage(storage)
        )
    }
}
