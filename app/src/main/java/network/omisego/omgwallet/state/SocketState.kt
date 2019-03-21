package network.omisego.omgwallet.state

import co.omisego.omisego.websocket.SocketClientContract

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 21/3/2019 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

sealed class SocketState {
    class Start(val authToken: String) : SocketState()
    class Stop(val socketClient: SocketClientContract.Client) : SocketState()
    object Idle: SocketState()
}