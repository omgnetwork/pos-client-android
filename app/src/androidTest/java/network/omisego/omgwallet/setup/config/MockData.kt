package network.omisego.omgwallet.setup.config

import co.omisego.omisego.extension.bd
import co.omisego.omisego.model.Balance
import co.omisego.omisego.model.Token
import co.omisego.omisego.model.Wallet
import co.omisego.omisego.model.WalletList
import java.util.Date

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

object MockData {
    val walletList = WalletList(
        listOf(
            Wallet(
                "12345678",
                listOf(
                    Balance(
                        10000.bd,
                        Token(
                            "1234",
                            "OMG",
                            "Omise GO",
                            1000.bd,
                            Date(),
                            Date(),
                            mapOf(),
                            mapOf())
                    )
                ),
                "Euro",
                "",
                null,
                null,
                null,
                null,
                mapOf(),
                mapOf()
            )
        )
    )
}