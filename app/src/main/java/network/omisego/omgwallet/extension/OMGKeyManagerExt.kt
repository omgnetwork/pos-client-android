package network.omisego.omgwallet.extension

import co.omisego.omisego.security.OMGKeyManager

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

fun OMGKeyManager.encrypt(data: String) = this.encrypt(data.toByteArray())

fun OMGKeyManager.decrypt(data: String) = this.decrypt(data.toByteArray())