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
        get() = "http://10.0.2.2:4000/api/client/"
    override val apiKey: String
        get() = "eWjUTqgor_-WP6Dx28Y7rG9oXKohRm571YGsspg627Y"
}
