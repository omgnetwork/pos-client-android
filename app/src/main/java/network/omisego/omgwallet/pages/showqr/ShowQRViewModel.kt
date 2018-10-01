package network.omisego.omgwallet.pages.showqr

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 1/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import co.omisego.omisego.qrcode.generator.QRGenerator
import network.omisego.omgwallet.data.LocalRepository

class ShowQRViewModel(
    val app: Application,
    private val localRepository: LocalRepository,
    private val generator: QRGenerator
) : AndroidViewModel(app) {

    val liveQR: MutableLiveData<Bitmap> by lazy { MutableLiveData<Bitmap>() }

    companion object {
        const val RATIO_QR_WIDTH_TO_SCREEN = 0.8
    }

    fun createQRBitmap() {
        val address = localRepository.loadWallet()!!.data[0].address
        val size = app.resources.displayMetrics.widthPixels * RATIO_QR_WIDTH_TO_SCREEN
        liveQR.value = generator.generate(address, size.toInt())
    }
}