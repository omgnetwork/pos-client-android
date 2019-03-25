package network.omisego.omgwallet.state

import co.omisego.omisego.constant.enums.ErrorCode
import co.omisego.omisego.model.APIError

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 23/3/2019 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

enum class ErrorState {
    BACK,
    SIGN_IN,
    QUIT;

    companion object {
        fun getErrorState(error: APIError): ErrorState {
            return when (error.code) {
                ErrorCode.SDK_NETWORK_ERROR,
                ErrorCode.SERVER_INTERNAL_SERVER_ERROR,
                ErrorCode.SERVER_UNKNOWN_ERROR,
                ErrorCode.CLIENT_ENDPOINT_NOT_FOUND,
                ErrorCode.CLIENT_INVALID_API_KEY,
                ErrorCode.SDK_UNEXPECTED_ERROR -> ErrorState.QUIT
                ErrorCode.CLIENT_INVALID_AUTH_SCHEME,
                ErrorCode.USER_AUTH_TOKEN_EXPIRED,
                ErrorCode.USER_AUTH_TOKEN_NOT_FOUND -> ErrorState.SIGN_IN
                else -> ErrorState.BACK
            }
        }
    }
}
