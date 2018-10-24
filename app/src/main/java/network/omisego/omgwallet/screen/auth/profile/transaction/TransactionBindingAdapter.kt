package network.omisego.omgwallet.screen.auth.profile.transaction

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 22/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import co.omisego.omisego.model.pagination.Paginable
import co.omisego.omisego.model.transaction.Transaction
import network.omisego.omgwallet.R

object TransactionBindingAdapter {
    @JvmStatic
    @BindingAdapter("transaction")
    fun colorizedTransaction(tv: TextView, transaction: Transaction) {
        when (transaction.status) {
            Paginable.Transaction.TransactionStatus.CONFIRMED -> {
                if (transaction.to.accountId != null) {
                    tv.setTextColor(ContextCompat.getColor(tv.context, R.color.colorRed))
                } else {
                    tv.setTextColor(ContextCompat.getColor(tv.context, R.color.colorGreen))
                }
            }
            else -> {
                tv.setTextColor(ContextCompat.getColor(tv.context, R.color.colorGray))
            }
        }
    }
}
