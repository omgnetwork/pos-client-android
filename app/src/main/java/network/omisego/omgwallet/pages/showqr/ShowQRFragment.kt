package network.omisego.omgwallet.pages.showqr

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 23/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.omisego.omisego.qrcode.generator.QRGenerator
import kotlinx.android.synthetic.main.fragment_show_qr.*
import network.omisego.omgwallet.R
import network.omisego.omgwallet.databinding.FragmentShowQrBinding
import network.omisego.omgwallet.extension.getDrawableCompat
import network.omisego.omgwallet.storage.Storage

class ShowQRFragment : Fragment() {
    private lateinit var binding: FragmentShowQrBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_show_qr, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        val address = Storage.loadWallets()!!.data[0].address
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val size = displayMetrics.widthPixels * 0.8
        ivQR.setImageBitmap(QRGenerator().generate(address, size.toInt()))
    }

    private fun setupToolbar() {
        val hostActivity = activity as AppCompatActivity
        hostActivity.setSupportActionBar(toolbar)
        toolbar.navigationIcon = context?.getDrawableCompat(R.drawable.ic_close)
        toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        hostActivity.supportActionBar?.title = getString(R.string.show_qr_title)
    }
}
