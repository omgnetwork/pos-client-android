package network.omisego.omgwallet.pages.showqr

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
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_show_qr.*
import network.omisego.omgwallet.R
import network.omisego.omgwallet.databinding.FragmentShowQrBinding
import network.omisego.omgwallet.extension.bindingInflate
import network.omisego.omgwallet.extension.getDrawableCompat
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
        setupToolbar()
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        viewModel.createQRBitmap()
    }

    private fun setupToolbar() {
        val hostActivity = activity as AppCompatActivity
        hostActivity.setSupportActionBar(toolbar)
        toolbar.navigationIcon = context?.getDrawableCompat(R.drawable.ic_close)
        toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        hostActivity.supportActionBar?.title = getString(R.string.show_qr_title)

        val window = activity?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = ContextCompat.getColor(context!!, R.color.colorBlue)
    }
}
