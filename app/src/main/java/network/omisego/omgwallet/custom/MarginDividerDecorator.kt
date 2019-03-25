package network.omisego.omgwallet.custom

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 20/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import network.omisego.omgwallet.R

class MarginDividerDecorator(context: Context, val margin: Rect) : DividerItemDecoration(context, VERTICAL) {
    private val divider: Drawable by lazy { ContextCompat.getDrawable(context, R.drawable.shape_divider)!! }
    private val bound: Rect = Rect()

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        canvas.save()
        val left = margin.left
        val right: Int = parent.width - margin.right

        val childCount = parent.childCount

        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, bound)
            val bottom = bound.bottom + Math.round(child.translationY)
            val top = bottom - divider.intrinsicHeight
            divider.setBounds(left, top, right, bottom)
            divider.draw(canvas)
        }

        canvas.restore()
    }
}
