package network.omisego.omgwallet.extension

import java.math.BigDecimal

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 18/3/2019 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

fun BigDecimal.formatAmount() =
    "%,.${this.scale()}f".format(this)