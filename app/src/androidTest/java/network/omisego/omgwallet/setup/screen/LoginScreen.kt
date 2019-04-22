package network.omisego.omgwallet.setup.screen

import com.agoda.kakao.edit.KTextInputLayout
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

class LoginScreen : Screen<LoginScreen>() {

    val tilEmail: KTextInputLayout = KTextInputLayout {
        withId(R.id.tilEmail)
    }
    val tilPassword: KTextInputLayout = KTextInputLayout {
        withId(R.id.tilPassword)
    }
    val btnLogin: KButton = KButton {
        withId(R.id.btnSignIn)
    }
    val btnFingerprint: KButton = KButton {
        withId(R.id.btnFingerprint)
    }
    val tvSignUp: KTextView = KTextView {
        withId(R.id.tvSignUp)
    }
    val tvVersion: KTextView = KTextView {
        withId(R.id.tvVersion)
    }
}