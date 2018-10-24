package network.omisego.omgwallet.screen.auth.showqr

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 23/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import network.omisego.omgwallet.R
import network.omisego.omgwallet.databinding.FragmentShowQrBinding
import network.omisego.omgwallet.extension.bindingInflate
import network.omisego.omgwallet.extension.provideAndroidViewModel

class ShowQRFragment : Fragment() {
    private lateinit var binding: FragmentShowQrBinding
    private lateinit var viewModel: ShowQRViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideAndroidViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = bindingInflate(R.layout.fragment_show_qr, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        viewModel.createQRBitmap()
    }
}
