package network.omisego.omgwallet.setup.screen

import com.agoda.kakao.KButton
import com.agoda.kakao.KImageView
import com.agoda.kakao.KTextView
import com.agoda.kakao.Screen
import network.omisego.omgwallet.R

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class ConfirmScreen : Screen<ConfirmScreen>() {

    val tvTitle: KTextView = KTextView {
        withId(R.id.tvTitle)
    }
    val ivLogo: KImageView = KImageView {
        withId(R.id.ivLogo)
    }
    val btnGotIt: KButton = KButton {
        withId(R.id.btnGotIt)
    }
    val tvDescription: KTextView = KTextView {
        withId(R.id.tvDescription)
    }
}