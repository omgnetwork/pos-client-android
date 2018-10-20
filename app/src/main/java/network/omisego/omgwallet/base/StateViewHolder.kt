package network.omisego.omgwallet.base

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

sealed class StateViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
    class Loading(itemView: View) : StateViewHolder(itemView)
    class Show<T : ViewDataBinding>(val binding: T) : StateViewHolder(binding.root)
}
