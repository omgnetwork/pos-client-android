package network.omisego.omgwallet.state

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 21/3/2019 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

sealed class SocketState {
    class Start(val authToken: String) : SocketState()
    object Stop : SocketState()
    object Idle : SocketState()
}