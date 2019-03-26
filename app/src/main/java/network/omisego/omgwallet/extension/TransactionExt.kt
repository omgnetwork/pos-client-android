package network.omisego.omgwallet.extension

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 26/3/2019 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omisego.model.TransactionConsumption
import co.omisego.omisego.model.TransactionSource

fun TransactionSource.calledName() = account?.name ?: user?.email

fun TransactionConsumption.calledName(): String? = account?.name ?: user?.email
