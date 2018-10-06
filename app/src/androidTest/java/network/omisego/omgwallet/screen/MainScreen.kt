package network.omisego.omgwallet.screen

import com.agoda.kakao.KView
import com.agoda.kakao.Screen
import network.omisego.omgwallet.R

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class MainScreen : Screen<MainScreen>() {
    val bottombarBalance: KView = KView {
        withId(R.id.bottomBarBalance)
    }
    val bottomBarProfile: KView = KView {
        withId(R.id.bottomBarProfile)
    }
    val fabQR: KView = KView {
        withId(R.id.fabQR)
    }
}
