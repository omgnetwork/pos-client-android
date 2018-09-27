package network.omisego.omgwallet.view

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 21/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.view_menu_bottom_bar.view.*
import network.omisego.omgwallet.R

class BottomBarItem @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_menu_bottom_bar, this, true)
        attrs?.let { attr ->
            val typedArray = context.obtainStyledAttributes(attr, R.styleable.BottomBarItem, 0, 0)
            val itemName = typedArray.getString(R.styleable.BottomBarItem_bb_title)
            val icon = typedArray.getString(R.styleable.BottomBarItem_bb_icon)
            itemName?.let { tvItemName.text = it }
            icon?.let { tvIcon.text = it }
            typedArray.recycle()
        }
        select(false)
    }

    fun select(isSelect: Boolean) {
        tvItemName.isSelected = isSelect
        tvIcon.isSelected = isSelect
    }

    override fun setOnClickListener(l: OnClickListener?) {
        container.setOnClickListener(l)
    }

    override fun setSelected(selected: Boolean) {
        select(selected)
    }
}