package network.omisego.omgwallet.screen

import com.agoda.kakao.KTextView
import com.agoda.kakao.KView
import com.agoda.kakao.KViewPager
import com.agoda.kakao.Screen
import network.omisego.omgwallet.R

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 22/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class BalanceDetailScreen : Screen<BalanceDetailScreen>() {
    val viewpager: KViewPager = KViewPager {
        withId(R.id.viewpager)
    }

    val tvTokenPrimaryHelper: KTextView = KTextView {
        withId(R.id.tvTokenPrimaryHelper)
    }

    val indicator: KView = KView {
        withId(R.id.pageIndicatorView)
    }
}