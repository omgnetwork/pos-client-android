package network.omisego.omgwallet.extension

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 13/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.omisego.omisego.constant.enums.ErrorCode
import co.omisego.omisego.custom.OMGCallback
import co.omisego.omisego.custom.retrofit2.adapter.OMGCall
import co.omisego.omisego.exception.OMGAPIErrorException
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse
import network.omisego.omgwallet.model.APIResult
import network.omisego.omgwallet.util.Event

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

fun <T> OMGCall<T>.subscribeSingleEvent(
    liveCallback: MutableLiveData<Event<APIResult>> = MutableLiveData()
): LiveData<Event<APIResult>> {
    this.enqueue(object : OMGCallback<T> {
        override fun fail(response: OMGResponse<APIError>) {
            liveCallback.value = Event(APIResult.Fail(response.data))
        }

        override fun success(response: OMGResponse<T>) {
            liveCallback.value = Event(APIResult.Success(response.data))
        }
    })
    return liveCallback
}

fun <T> OMGCall<T>.safeExecute(): APIResult {
    return try {
        val response = this.execute()
        APIResult.Success(response.body()!!.data)
    } catch (e: OMGAPIErrorException) {
        APIResult.Fail(e.response.data)
    } catch (e: Exception) {
        APIResult.Fail(APIError(ErrorCode.SDK_UNEXPECTED_ERROR, "Unknown error"))
    }
}
