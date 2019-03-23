package network.omisego.omgwallet.screen.unauth.signin

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 12/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

data class SignInState(
    val email: String,
    val password: String,
    val btnText: String,
    val loading: Boolean
)
