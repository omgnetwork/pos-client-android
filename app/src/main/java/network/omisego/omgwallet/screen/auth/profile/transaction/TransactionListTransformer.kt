package network.omisego.omgwallet.screen.auth.profile.transaction

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 29/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.content.Context
import co.omisego.omisego.model.Transaction
import co.omisego.omisego.model.pagination.Paginable
import network.omisego.omgwallet.R
import network.omisego.omgwallet.extension.calledName
import network.omisego.omgwallet.extension.scaleAmount

class TransactionListTransformer(
    val context: Context
) {
    val Transaction.isTopup: Boolean
        get() = this.from.accountId != null

    fun transformTransactionDisplayName(transaction: Transaction): String {
        return if (transaction.isTopup) {
            context.getString(
                R.string.transaction_list_info_name_id,
                transaction.from.calledName()
            )
        } else {
            context.getString(
                R.string.transaction_list_info_name_id,
                transaction.to.calledName()
            )
        }
    }

    fun transformDescription(transaction: Transaction): String {
        return if (transaction.error == null) {
            context.getString(R.string.transaction_list_info_success)
        } else {
            context.getString(R.string.transaction_list_info_failure)
        }
    }

    fun transformAmount(transaction: Transaction): String {
        val amountText = if (transaction.isTopup) {
            context.getString(
                R.string.transaction_list_info_amount,
                transaction.to.scaleAmount(maxPrecision = 2),
                transaction.to.token.symbol
            )
        } else {
            context.getString(
                R.string.transaction_list_info_amount,
                transaction.from.scaleAmount(maxPrecision = 2),
                transaction.from.token.symbol
            )
        }
        return when (transaction.status) {
            Paginable.Transaction.TransactionStatus.CONFIRMED -> {
                if (transaction.to.accountId != null) {
                    "- $amountText"
                } else {
                    "+ $amountText"
                }
            }
            else -> {
                amountText
            }
        }
    }

    fun transformStatusIcon(transaction: Transaction): String {
        return if (transaction.error == null) {
            "\uE906"
        } else {
            "\uE90B"
        }
    }

    fun transformDate(transaction: Transaction): String {
        return context.getString(R.string.transaction_list_info_date_time, transaction.createdAt)
    }
}
