package network.omisego.omgwallet.setup.screen

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
 * Created by Phuchit Sirimongkolsathien on 6/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class TransactionListScreen : Screen<TransactionListScreen>() {
    val recyclerView: KRecyclerView = KRecyclerView({
        withId(R.id.recyclerView)
    }, {
        itemType(::Item)
    })

    class Item(parent: Matcher<View>) : KRecyclerItem<Item>(parent) {
        val tvAccount: KTextView = KTextView(parent) { withId(R.id.tvAccount) }
        val tvdate: KTextView = KTextView(parent) { withId(R.id.tvDate) }
        val tvAmount: KTextView = KTextView(parent) { withId(R.id.tvAmount) }
        val tvStatus: KTextView = KTextView(parent) { withId(R.id.tvStatus) }
        val tvStatusIcon: KTextView = KTextView(parent) { withId(R.id.tvStatusIcon) }
    }
}