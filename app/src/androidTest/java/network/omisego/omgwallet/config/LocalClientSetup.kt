package network.omisego.omgwallet.config

import network.omisego.omgwallet.network.ClientSetup

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class LocalClientSetup : ClientSetup {
    override val baseURL: String
        get() = "https://coffeego.omisego.io/api/client/" // Special ip-address for local machine in the emulator
    override val apiKey: String
        get() = "fxqhJomqeemaAomNyfH_RphsVx4D2Z0ruBo_g-3jCY4"
}
