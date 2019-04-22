package network.omisego.omgwallet.listener

import co.omisego.omisego.model.ClientAuthenticationToken

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 21/3/2019 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

interface LoginListener {
    fun onLoggedin(clientAuthToken: ClientAuthenticationToken)
    fun onLoggedout()
}
