package network.omisego.omgwallet.model

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 13/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omisego.model.APIError

sealed class APIResult {
    class Success<T>(val data: T) : APIResult()
    class Fail<APIError>(val error: APIError) : APIResult()

    fun <T> handle(handleSuccess: (T) -> Unit, handleError: (APIError) -> Unit) {
        when (this) {
            is Success<*> -> {
                handleSuccess(this.data as T)
            }
            is Fail<*> -> {
                handleError(this.error as APIError)
            }
        }
    }
}
