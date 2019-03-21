package network.omisego.omgwallet.livedata

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 20/3/2019 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import co.omisego.omisego.websocket.SocketClientContract
import network.omisego.omgwallet.state.SocketState

class SocketStateLiveData(
    liveAuthenticationToken: LiveData<String?>,
    liveSocketClient: LiveData<SocketClientContract.Client?>
) : MediatorLiveData<SocketState>() {
    private var socketClient: SocketClientContract.Client? = null
    private var authenticationToken: String? = null

    init {
        addSource(liveAuthenticationToken) {
            authenticationToken = it
            value = moveState(authenticationToken, socketClient)
        }
        addSource(liveSocketClient) {
            socketClient = it
            value = moveState(authenticationToken, socketClient)
        }
    }

    private fun moveState(authenticationToken: String?, socketClient: SocketClientContract.Client?): SocketState {
        return when {
            authenticationToken == null && socketClient != null -> {
                SocketState.Stop(socketClient)
            }
            authenticationToken != null && socketClient == null -> {
                SocketState.Start(authenticationToken)
            }
            else -> {
                SocketState.Idle
            }
        }
    }
}
