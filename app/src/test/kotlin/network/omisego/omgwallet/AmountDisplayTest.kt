package network.omisego.omgwallet

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 6/12/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omisego.extension.bd
import co.omisego.omisego.model.Balance
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import network.omisego.omgwallet.extension.displayFormattedAmount
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test

class AmountDisplayTest {

    private val mockBalance: Balance = mock()

    @Before
    fun setup() {
        whenever(mockBalance.token).thenReturn(mock())
    }

    @Test
    fun `test amount should not loss precision`() {
        whenever(mockBalance.amount).thenReturn(1240.bd)
        whenever(mockBalance.token.subunitToUnit).thenReturn(100.bd)

        mockBalance.displayFormattedAmount() shouldEqual 12.40.bd.setScale(2)

        whenever(mockBalance.amount).thenReturn(1245.bd)
        whenever(mockBalance.token.subunitToUnit).thenReturn(100.bd)

        mockBalance.displayFormattedAmount() shouldEqual 12.45.bd

        whenever(mockBalance.amount).thenReturn(12.bd)
        whenever(mockBalance.token.subunitToUnit).thenReturn(100.bd)

        mockBalance.displayFormattedAmount() shouldEqual 0.12.bd

        whenever(mockBalance.amount).thenReturn(1.bd)
        whenever(mockBalance.token.subunitToUnit).thenReturn(100.bd)

        mockBalance.displayFormattedAmount() shouldEqual 0.01.bd

        whenever(mockBalance.amount).thenReturn(100.bd)
        whenever(mockBalance.token.subunitToUnit).thenReturn(100.bd)

        mockBalance.displayFormattedAmount() shouldEqual 1.00.bd.setScale(2)

        whenever(mockBalance.amount).thenReturn(0.bd)
        whenever(mockBalance.token.subunitToUnit).thenReturn(100000.bd)

        mockBalance.displayFormattedAmount() shouldEqual 0.00.bd.setScale(2)

        whenever(mockBalance.amount).thenReturn(200005000000000000.bd)
        whenever(mockBalance.token.subunitToUnit).thenReturn(100000000000000000.bd)

        mockBalance.displayFormattedAmount() shouldEqual 2.00005.bd
    }
}