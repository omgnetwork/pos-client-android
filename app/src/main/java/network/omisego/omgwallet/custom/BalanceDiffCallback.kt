package network.omisego.omgwallet.custom

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 22/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import co.omisego.omisego.model.Balance

class BalanceDiffCallback(
    private val oldItems: List<Balance>,
    private val newItems: List<Balance>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
        return oldItems[oldPos].token.id == newItems[newPos].token.id
    }

    override fun getOldListSize() = oldItems.size
    override fun getNewListSize() = newItems.size
    override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
        return oldItems[oldPos].amount == newItems[newPos].amount
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val newItem = newItems[newItemPosition]
        val oldItem = oldItems[oldItemPosition]

        val diff = Bundle()
        if (oldItem.amount != newItem.amount) {
            diff.putParcelable("balance", newItem)
        }
        return if (diff.size() == 0) {
            null
        } else {
            diff
        }
    }
}
