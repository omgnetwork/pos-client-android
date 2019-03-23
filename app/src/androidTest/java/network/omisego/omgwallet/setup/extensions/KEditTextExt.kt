package network.omisego.omgwallet.setup.extensions

import com.agoda.kakao.KEditText

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 8/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
fun KEditText.typeWithDelay(text: String, delay: Long = 300) {
    this.typeText(text)
    Thread.sleep(delay)
}

fun KEditText.clickThenReplace(text: String) {
    this.click()
    this.replaceText(text)
}