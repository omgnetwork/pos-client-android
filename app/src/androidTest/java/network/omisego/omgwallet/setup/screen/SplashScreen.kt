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

class SplashScreen : Screen<SplashScreen>() {

    val loadingGif: KImageView = KImageView {
        withId(R.id.imageView)
    }

    val tvStatus: KTextView = KTextView {
        withId(R.id.tvCurrentStatus)
    }

    val btnClose: KButton = KButton {
        withId(R.id.btnClose)
    }
}