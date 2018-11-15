package network.omisego.omgwallet.extension

import co.omisego.omisego.model.APIError
import co.omisego.omisego.utils.Either
import co.omisego.omisego.utils.GsonProvider
import retrofit2.Response

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 17/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

/*  Either<T,APIError> */

fun <T> Response<T>.either(doOnSuccess: (T) -> Unit, doOnError: (APIError) -> Unit) {
    val successOrError = if (this.isSuccessful) {
        Either.Left(this.body()!!)
    } else {
        Either.Right(GsonProvider.create().fromJson(this.errorBody()?.string(), APIError::class.java))
    }

    successOrError.either(doOnSuccess, doOnError)
}
