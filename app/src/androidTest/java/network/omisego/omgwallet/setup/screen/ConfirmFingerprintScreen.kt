package network.omisego.omgwallet.setup.screen

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 6/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import com.agoda.kakao.edit.KTextInputLayout
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.agoda.kakao.text.KTextView
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
