package network.omisego.omgwallet.util

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 23/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import network.omisego.omgwallet.data.LocalRepository
import network.omisego.omgwallet.data.RemoteRepository

object RepositoryUtil {
    lateinit var localRepository: LocalRepository
    lateinit var remoteRepository: RemoteRepository
}