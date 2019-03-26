package network.omisego.omgwallet.setup.custom

import android.view.View
import androidx.test.espresso.DataInteraction
import com.agoda.kakao.common.builders.ViewBuilder
import com.agoda.kakao.common.views.KBaseView
import com.agoda.kakao.list.ScrollViewActions
import org.hamcrest.Matcher

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 8/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class KScrollView : KBaseView<KScrollView>, ScrollViewActions {
    constructor(function: ViewBuilder.() -> Unit) : super(function)
    constructor(parent: Matcher<View>, function: ViewBuilder.() -> Unit) : super(parent, function)
    constructor(parent: DataInteraction, function: ViewBuilder.() -> Unit) : super(parent, function)
}
