package network.omisego.omgwallet.demo.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 18/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class MoveUpBehavior : CoordinatorLayout.Behavior<View> {

    constructor() : super()
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return dependency is Snackbar.SnackbarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
//        child.translationY = Math.min(0f, dependency.translationY - dependency.height)
        return true
    }
}