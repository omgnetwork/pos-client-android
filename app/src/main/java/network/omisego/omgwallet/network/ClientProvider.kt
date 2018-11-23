package network.omisego.omgwallet.network

import android.util.Log
import co.omisego.omisego.OMGAPIClient
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.ClientConfiguration
import co.omisego.omisego.network.ewallet.EWalletClient
import co.omisego.omisego.websocket.OMGSocketClient
import co.omisego.omisego.websocket.SocketClientContract
import co.omisego.omisego.websocket.listener.SocketChannelListener
import co.omisego.omisego.websocket.listener.SocketConnectionListener
import com.facebook.stetho.okhttp3.StethoInterceptor
import network.omisego.omgwallet.extension.logi
import network.omisego.omgwallet.storage.Storage
import okhttp3.logging.HttpLoggingInterceptor

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

object ClientProvider {
    private lateinit var clientConfiguration: ClientConfiguration
    private lateinit var socketClientConfiguration: ClientConfiguration
    lateinit var client: OMGAPIClient
    lateinit var socketClient: SocketClientContract.Client
    lateinit var eWalletClient: EWalletClient
    lateinit var clientSetup: ClientSetup

    fun initHTTPClient(clientSetup: ClientSetup) {
        this.clientSetup = clientSetup
        clientConfiguration = ClientConfiguration(
            clientSetup.baseURL,
            clientSetup.apiKey,
            Storage.loadCredential().authenticationToken
        )
        client = create()
    }

    fun initSocketClient(authenticationToken: String) {
        socketClientConfiguration = clientConfiguration.copy(
            baseURL = clientSetup.socketBaseURL,
            authenticationToken = authenticationToken
        )
        socketClient = createSocketClient()
        addSocketListener()
    }

    fun addSocketListener() {
        ClientProvider.socketClient.addChannelListener(object : SocketChannelListener {
            override fun onError(apiError: APIError): Boolean {
                logi("Error: ${apiError.description}")
                return true
            }

            override fun onJoinedChannel(topic: String): Boolean {
                Log.i("Socket", "Joined: ${topic}")
                return true
            }

            override fun onLeftChannel(topic: String): Boolean {
                Log.i("Socket", "Left $topic")
                return true
            }
        })

        ClientProvider.socketClient.addConnectionListener(object : SocketConnectionListener {
            override fun onConnected() {
                Log.i("Socket", "Connected")
            }

            override fun onDisconnected(throwable: Throwable?) {
                Log.i("Socket", "Disconnected")
            }
        })
    }

    private fun create(): OMGAPIClient {
        eWalletClient = EWalletClient.Builder {
            clientConfiguration = this@ClientProvider.clientConfiguration
            debug = true
            debugOkHttpInterceptors = mutableListOf(
                StethoInterceptor(),
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
            )
        }.build()
        return OMGAPIClient(
            eWalletClient
        )
    }

    private fun createSocketClient(): SocketClientContract.Client {
        return OMGSocketClient.Builder {
            clientConfiguration = this@ClientProvider.socketClientConfiguration
            debug = true
        }.build()
    }
}
