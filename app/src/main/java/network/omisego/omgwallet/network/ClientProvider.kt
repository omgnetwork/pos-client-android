package network.omisego.omgwallet.network

import co.omisego.omisego.model.ClientConfiguration
import co.omisego.omisego.network.ewallet.EWalletClient
import co.omisego.omisego.websocket.OMGSocketClient
import co.omisego.omisego.websocket.SocketClientContract
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.logging.HttpLoggingInterceptor

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

object ClientProvider {
    // Providing way for inject tested ewallet client.
    private var testEWalletClient: EWalletClient? = null

    fun setTestEWalletClient(client: EWalletClient) {
        testEWalletClient = client
    }

    fun createClient(
        authenticationToken: String? = null,
        config: ClientConfig = APIClientConfig()
    ): EWalletClient {
        testEWalletClient?.let { return it }
        val clientConfig = ClientConfiguration(
            config.baseURL,
            config.apiKey,
            authenticationToken
        )
        return initializeEWalletClient(clientConfig)
    }

    fun createSocketClient(
        authenticationToken: String,
        config: ClientConfig = APIClientConfig()
    ): SocketClientContract.Client {
        val socketClientConfiguration = ClientConfiguration(
            baseURL = config.socketBaseURL,
            apiKey = config.apiKey,
            authenticationToken = authenticationToken
        )
        return initializeSocketClient(socketClientConfiguration)
    }

    private fun initializeEWalletClient(config: ClientConfiguration): EWalletClient {
        return EWalletClient.Builder {
            clientConfiguration = config
            debug = true
            debugOkHttpInterceptors = mutableListOf(
                StethoInterceptor(),
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
            )
        }.build()
    }

    private fun initializeSocketClient(config: ClientConfiguration): SocketClientContract.Client {
        return OMGSocketClient.Builder {
            clientConfiguration = config
            debug = true
        }.build()
    }
}
