package network.omisego.omgwallet.extensions

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 13/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.omisego.omisego.custom.OMGCallback
import co.omisego.omisego.custom.retrofit2.adapter.OMGCall
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse
import network.omisego.omgwallet.model.APIResult

fun <T> OMGCall<T>.subscribe(
    liveCallback: MutableLiveData<APIResult> = MutableLiveData()
): LiveData<APIResult> {
    this.enqueue(object : OMGCallback<T> {
        override fun fail(response: OMGResponse<APIError>) {
            liveCallback.value = APIResult.Fail(response.data)
        }

        override fun success(response: OMGResponse<T>) {
            liveCallback.value = APIResult.Success(response.data)
        }
    })
    return liveCallback
}
