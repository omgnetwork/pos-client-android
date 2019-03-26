package network.omisego.omgwallet.setup.screen

import com.agoda.kakao.image.KImageView
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.agoda.kakao.text.KTextView
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