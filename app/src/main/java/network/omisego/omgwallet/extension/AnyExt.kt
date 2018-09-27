package network.omisego.omgwallet.extension

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
import android.util.Log

fun Any.logi(message: Any?) = Log.i(this.javaClass.simpleName, message?.toString())

fun Any.logd(message: Any?) = Log.d(this.javaClass.simpleName, message?.toString())
