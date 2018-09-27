package network.omisego.omgwallet.pages.signup

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 20/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.MutableLiveData
import co.omisego.omisego.model.params.SignUpParams
import network.omisego.omgwallet.extension.subscribe
import network.omisego.omgwallet.model.APIResult
import network.omisego.omgwallet.network.ClientProvider

class SignupRepository {
    fun signup(params: SignUpParams, liveAPIResult: MutableLiveData<APIResult>) {
        ClientProvider.client.signup(params).subscribe(liveAPIResult)
    }
}
