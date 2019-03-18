package network.omisego.omgwallet.extension

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 6/12/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omisego.model.Balance
import co.omisego.omisego.model.TransactionConsumption
import co.omisego.omisego.model.TransactionSource
import java.math.BigDecimal
import java.math.RoundingMode

fun Balance.scaleAmount(maxPrecision: Int = token.subunitToUnit.precision() - 1, minPrecision: Int = 2): BigDecimal {
    val value = amount
        .divide(token.subunitToUnit, maxPrecision, RoundingMode.HALF_UP)
        .stripTrailingZeros()

    if (value.scale() < minPrecision) {
        // upscale
        return value.setScale(minPrecision)
    }
    return value
}

fun TransactionSource.scaleAmount(maxPrecision: Int = token.subunitToUnit.precision() - 1, minPrecision: Int = 2): BigDecimal {
    val value = amount
        .divide(token.subunitToUnit, maxPrecision, RoundingMode.HALF_UP)
        .stripTrailingZeros()

    if (value.scale() < minPrecision) {
        // upscale
        return value.setScale(minPrecision)
    }
    return value
}

fun TransactionConsumption.scaleAmount(maxPrecision: Int = transactionRequest.token.subunitToUnit.precision() - 1, minPrecision: Int = 2): BigDecimal {
    val value = estimatedRequestAmount
        .divide(transactionRequest.token.subunitToUnit, maxPrecision, RoundingMode.HALF_UP)
        .stripTrailingZeros()

    if (value.scale() < minPrecision) {
        // upscale
        return value.setScale(minPrecision)
    }
    return value
}
