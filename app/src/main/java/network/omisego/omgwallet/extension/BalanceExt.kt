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
    return scaleAmount(amount, token.subunitToUnit, maxPrecision, minPrecision)
}

fun TransactionSource.scaleAmount(maxPrecision: Int = token.subunitToUnit.precision() - 1, minPrecision: Int = 2): BigDecimal {
    return scaleAmount(amount, token.subunitToUnit, maxPrecision, minPrecision)
}

fun TransactionConsumption.scaleAmount(maxPrecision: Int = transactionRequest.token.subunitToUnit.precision() - 1, minPrecision: Int = 2): BigDecimal {
    return scaleAmount(estimatedRequestAmount, transactionRequest.token.subunitToUnit, maxPrecision, minPrecision)
}

private fun scaleAmount(amount: BigDecimal, subunitToUnit: BigDecimal, maxPrecision: Int, minPrecision: Int): BigDecimal {
    val value = amount.divide(subunitToUnit, maxPrecision, RoundingMode.HALF_UP)
    val zero = BigDecimal.ZERO
    val strippedZeroValue =
        if (value.compareTo(zero) == 0) {
            zero
        } else {
            value.stripTrailingZeros()
        }

    if (strippedZeroValue.scale() < minPrecision) {
        // upscale
        return strippedZeroValue.setScale(minPrecision)
    }
    return strippedZeroValue
}
