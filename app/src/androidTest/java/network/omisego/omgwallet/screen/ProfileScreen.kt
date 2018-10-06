package network.omisego.omgwallet.screen

import com.agoda.kakao.KTextView
import com.agoda.kakao.Screen
import network.omisego.omgwallet.R
import network.omisego.omgwallet.custom.KSwitch

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class ProfileScreen : Screen<ProfileScreen>() {
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
}
