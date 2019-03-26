package network.omisego.omgwallet.setup.screen

import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KTextView
import network.omisego.omgwallet.R
import network.omisego.omgwallet.setup.custom.KScrollView
import network.omisego.omgwallet.setup.custom.KSwitch

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class ProfileScreen : Screen<ProfileScreen>() {
    val scrollView: KScrollView = KScrollView {
        withId(R.id.scrollView)
    }
    val tvTransaction: KTextView = KTextView {
        withId(R.id.tvTransaction)
    }
    val tvFingerprintTitle: KTextView = KTextView {
        withId(R.id.tvFingerprintTitle)
    }
    val tvFingerprintDescription: KTextView = KTextView {
        withId(R.id.tvFingerprintDescription)
    }
    val tvSignOut: KTextView = KTextView {
        withId(R.id.tvSignOut)
    }
    val switch: KSwitch = KSwitch {
        withId(R.id.switchFingerprint)
    }
    val tvEndpointName: KTextView = KTextView {
        withId(R.id.tvEndpointData)
    }
    val tvVersionName: KTextView = KTextView {
        withId(R.id.tvVersionData)
    }
}
