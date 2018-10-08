package network.omisego.omgwallet.screen

import com.agoda.kakao.KButton
import com.agoda.kakao.KTextInputLayout
import com.agoda.kakao.Screen
import network.omisego.omgwallet.R
import network.omisego.omgwallet.custom.KScrollView

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class RegisterScreen : Screen<RegisterScreen>() {
    val scrollView: KScrollView = KScrollView {
        withId(R.id.scrollView)
    }
    val tilFullname: KTextInputLayout = KTextInputLayout {
        withId(R.id.tilFullname)
    }
    val tilEmail: KTextInputLayout = KTextInputLayout {
        withId(R.id.tilEmail)
    }
    val tilPassword: KTextInputLayout = KTextInputLayout {
        withId(R.id.tilPassword)
    }
    val tilRetypePassword: KTextInputLayout = KTextInputLayout {
        withId(R.id.tilRetypePassword)
    }
    val btnSignUp: KButton = KButton {
        withId(R.id.btnSignUp)
    }
}
