package network.omisego.omgwallet.pages.signin

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottomsheet_fingerprint_scan.*
import network.omisego.omgwallet.R
import network.omisego.omgwallet.databinding.BottomSheetFingerprintScanBinding
import network.omisego.omgwallet.extension.provideAndroidViewModel

class FingerprintBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetFingerprintScanBinding
    private var viewModel: FingerprintBottomSheetViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = parentFragment?.provideAndroidViewModel()
        viewModel?.cancel()
        viewModel?.init(context!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.bottomsheet_fingerprint_scan,
            container,
            false
        )
        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface?) {
        viewModel?.cancel()
        super.onDismiss(dialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        btnCancel.setOnClickListener {
            dismiss()
        }

        viewModel?.authenticate()
    }
}
