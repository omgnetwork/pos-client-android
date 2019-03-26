package network.omisego.omgwallet.setup.screen

import com.agoda.kakao.bottomnav.KBottomNavigationView
import com.agoda.kakao.common.views.KView
import com.agoda.kakao.screen.Screen
import network.omisego.omgwallet.R
import network.omisego.omgwallet.setup.custom.KToolbar

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class MainScreen : Screen<MainScreen>() {
    val toolbar: KToolbar = KToolbar {
        withId(R.id.toolbar)
    }

    val bottomNavigation: KBottomNavigationView = KBottomNavigationView {
        withId(R.id.bottomNavigation)
    }

    val fabQR: KView = KView {
        withId(R.id.fabQR)
    }
}
