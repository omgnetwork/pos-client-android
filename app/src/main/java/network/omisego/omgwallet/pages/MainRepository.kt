package network.omisego.omgwallet.pages

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 21/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import network.omisego.omgwallet.storage.Storage

class MainRepository {
    fun loadWallets() = Storage.loadWallets()
    fun loadUserEmail() = Storage.loadUserEmail()
}
