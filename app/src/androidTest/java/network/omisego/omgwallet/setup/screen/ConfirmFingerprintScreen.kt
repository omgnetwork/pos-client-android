package network.omisego.omgwallet.setup.screen

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 6/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import com.agoda.kakao.KButton
import com.agoda.kakao.KTextInputLayout
import com.agoda.kakao.KTextView
import com.agoda.kakao.Screen
import network.omisego.omgwallet.R

class ConfirmFingerprintScreen : Screen<ConfirmFingerprintScreen>() {
    val tvTitle: KTextView = KTextView {
        withId(R.id.tvTitle)
    }

    val tilPassword: KTextInputLayout = KTextInputLayout {
        withId(R.id.tilPassword)
    }

    val btnConfirm: KButton = KButton {
        withId(R.id.btnConfirm)
    }
}
