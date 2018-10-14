package network.omisego.omgwallet.pages.balance.detail

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import co.omisego.omisego.model.Balance

class BalanceDetailPagerAdapter(
    val items: List<Balance>,
    fm: FragmentManager
) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int) = BalanceDetailItemFragment.create(items[position], position + 1, items.size)

    override fun getCount() = items.size
}
