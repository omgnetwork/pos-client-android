package network.omisego.omgwallet.custom

import android.view.View
import androidx.test.espresso.DataInteraction
import com.agoda.kakao.KBaseView
import com.agoda.kakao.ViewBuilder
import network.omisego.omgwallet.custom.assertions.ToolbarAssertion
import org.hamcrest.Matcher

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 6/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class KToolbar : KBaseView<KToolbar>, ToolbarAssertion {
    constructor(function: ViewBuilder.() -> Unit) : super(function)
    constructor(parent: Matcher<View>, function: ViewBuilder.() -> Unit) : super(parent, function)
    constructor(parent: DataInteraction, function: ViewBuilder.() -> Unit) : super(parent, function)
}
