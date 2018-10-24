package network.omisego.omgwallet.screen

import android.view.View
import com.agoda.kakao.KRecyclerItem
import com.agoda.kakao.KRecyclerView
import com.agoda.kakao.KTextView
import com.agoda.kakao.Screen
import network.omisego.omgwallet.R
import org.hamcrest.Matcher

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class BalanceScreen : Screen<BalanceScreen>() {
    val recyclerView: KRecyclerView = KRecyclerView({
        withId(R.id.recyclerView)
    }, {
        itemType(::Item)
    })

    class Item(parent: Matcher<View>) : KRecyclerItem<Item>(parent) {
        val tvTokenLogo: KTextView = KTextView(parent) { withId(R.id.ivTokenLogo) }
        val tvCurrencyName: KTextView = KTextView(parent) { withId(R.id.tvCurrencyName) }
        val tvAmount: KTextView = KTextView(parent) { withId(R.id.tvAmount) }
        val tvCurrencySymbol: KTextView = KTextView(parent) { withId(R.id.tvCurrencySymbol) }
    }
}