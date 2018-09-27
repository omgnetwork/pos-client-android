package network.omisego.omgwallet.network

import co.omisego.omisego.OMGAPIClient
import co.omisego.omisego.model.ClientConfiguration
import co.omisego.omisego.network.ewallet.EWalletClient
import com.facebook.stetho.okhttp3.StethoInterceptor
import network.omisego.omgwallet.BuildConfig
import network.omisego.omgwallet.model.Credential
import network.omisego.omgwallet.storage.Storage
import okhttp3.logging.HttpLoggingInterceptor

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

object ClientProvider {
    private val credential: Credential
        get() = Storage.loadCredential()

    private lateinit var clientConfiguration: ClientConfiguration
    lateinit var client: OMGAPIClient

    fun init() {
        clientConfiguration = ClientConfiguration(
            BuildConfig.CLIENT_API_BASE_URL,
            BuildConfig.CLIENT_API_KEY,
            credential.authenticationToken
        )

        client = create()
    }

    private fun create(): OMGAPIClient {
        return OMGAPIClient(
            EWalletClient.Builder {
                clientConfiguration = this@ClientProvider.clientConfiguration
                debug = true
                debugOkHttpInterceptors = mutableListOf(
                    StethoInterceptor(),
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                )
            }.build()
        )
    }
}
