package network.omisego.omgwallet.screen

import com.agoda.kakao.KButton
import com.agoda.kakao.KTextView
import com.agoda.kakao.Screen
import network.omisego.omgwallet.R

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 23/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class ConfirmTransactionRequestScreen : Screen<ConfirmTransactionRequestScreen>() {
    val tvTokenFrom: KTextView = KTextView {
        withId(R.id.tvTokenFrom)
    }

    val tvAmount: KTextView = KTextView {
        withId(R.id.tvAmount)
    }

    val tvTokenTo: KTextView = KTextView {
        withId(R.id.tvTokenTo)
    }

    val tvErrorDescription: KTextView = KTextView {
        withId(R.id.tvErrorDescription)
    }

    val btnReject: KButton = KButton {
        withId(R.id.btnReject)
    }

    val btnApprove: KButton = KButton {
        withId(R.id.btnApprove)
    }
}