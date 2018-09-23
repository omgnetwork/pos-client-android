package network.omisego.omgwallet.databinding

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 22/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.content.Context
import android.graphics.Rect
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import network.omisego.omgwallet.custom.MarginDividerDecorator
import network.omisego.omgwallet.extension.dpToPx

object RecyclerViewBinding {

    @JvmStatic
    @BindingAdapter("divider_padding")
    fun dividerPadding(recyclerView: RecyclerView, paddingRect: Rect) {
        val pxPaddingSize = convertPxToDpRect(recyclerView.context, paddingRect)
        val dividerDecorator = MarginDividerDecorator(recyclerView.context, pxPaddingSize)
        recyclerView.addItemDecoration(dividerDecorator)
    }

    private fun convertPxToDpRect(context: Context, dpPaddingSize: Rect) = Rect(
        context.dpToPx(dpPaddingSize.left.toFloat()),
        context.dpToPx(dpPaddingSize.top.toFloat()),
        context.dpToPx(dpPaddingSize.right.toFloat()),
        context.dpToPx(dpPaddingSize.bottom.toFloat())
    )
}
