package network.omisego.omgwallet.setup.screen

import com.agoda.kakao.common.views.KView
import com.agoda.kakao.pager.KViewPager
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KTextView
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