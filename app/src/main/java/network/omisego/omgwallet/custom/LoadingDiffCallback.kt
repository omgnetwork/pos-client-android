package network.omisego.omgwallet.custom

import androidx.recyclerview.widget.DiffUtil

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 22/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class LoadingDiffCallback(
    private val contentItems: List<*>,
    private val allItems: List<Any>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
        return contentItems[oldPos] == allItems[newPos]
    }

    override fun getOldListSize() = contentItems.size
    override fun getNewListSize() = allItems.size
    override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
        return contentItems[oldPos] == allItems[newPos]
    }
}
