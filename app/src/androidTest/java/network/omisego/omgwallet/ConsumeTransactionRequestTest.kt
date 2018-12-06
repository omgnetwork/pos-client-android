package network.omisego.omgwallet

import androidx.test.runner.AndroidJUnit4
import co.omisego.omisego.extension.bd
import co.omisego.omisego.model.Balance
import co.omisego.omisego.model.Token
import co.omisego.omisego.model.TransactionConsumption
import co.omisego.omisego.model.params.LoginParams
import com.agoda.kakao.KView
import network.omisego.omgwallet.base.BaseInstrumentalTest
import network.omisego.omgwallet.config.LocalClientSetup
import network.omisego.omgwallet.config.TestData
import network.omisego.omgwallet.model.Credential
import network.omisego.omgwallet.network.ClientProvider
import network.omisego.omgwallet.screen.BalanceScreen
import network.omisego.omgwallet.screen.ConfirmTransactionRequestScreen
import network.omisego.omgwallet.screen.MainScreen
import network.omisego.omgwallet.storage.Storage
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import java.math.BigDecimal

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 23/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

// To run the tests below, please make sure that the primary token has amount at least `2 * subunitToUnit`.
// For example,
// if the primary token is "OMG" which has subunitToUnit = 100, then the minimum `amount` to run tests is 200.

@RunWith(AndroidJUnit4::class)
class ConsumeTransactionRequestTest : BaseInstrumentalTest() {
    private val mainScreen: MainScreen by lazy { MainScreen() }
    private val balanceScreen: BalanceScreen by lazy { BalanceScreen() }
    private val confirmTransactionRequestScreen: ConfirmTransactionRequestScreen by lazy { ConfirmTransactionRequestScreen() }

    companion object {
        @BeforeClass
        @JvmStatic
        fun setupClass() {
            ClientProvider.initHTTPClient(LocalClientSetup())
            Storage.clearSession()
            val response = ClientProvider.client.login(LoginParams(TestData.USER_EMAIL, TestData.USER_PASSWORD)).execute()
            Storage.saveUser(response.body()!!.data.user)
            Storage.saveCredential(Credential(response.body()!!.data.authenticationToken))
            Storage.saveUserEmail(TestData.USER_EMAIL)
        }
    }

    @Before
    fun setup() {
        Storage.deleteTokenPrimary()
        Storage.deleteFormattedIds()
        registerIdlingResource()
        start()
    }

    @After
    fun teardown() {
        unregisterIdlingResource()
    }

    @Test
    fun testConsumeTransactionRequestWithTypeSend_ThenConfirmPageShouldBeDisplayedCorrectly() {
        mainScreen {
            bottomNavigation.isDisplayed()

            /* Preparing test data */
            val currentPrimaryTokenId = Storage.loadTokenPrimary()
            val currentTxFormattedIds = Storage.loadFormattedId()
            val currentTxFormattedIdSend = currentTxFormattedIds.split("|")[1]
            val consumeToken: Token = Storage.loadWallets()?.data?.get(0)?.balances?.find { it.token.id == currentPrimaryTokenId }?.token!!

            /* Consume transaction */
            val txConsumption = consumeTx(consumeToken, currentTxFormattedIdSend)

            idle(1000)

            /* Verify confirmation screen data */
            confirmTransactionRequestScreen {
                tvTokenFrom {
                    isDisplayed()
                    hasText(stringRes(R.string.confirm_transaction_request_send))
                }
                tvTokenTo {
                    isDisplayed()
                    hasText("To ${txConsumption?.account?.name}")
                }
                tvAmount {
                    isDisplayed()
                    hasText("1.00 ${consumeToken.symbol}")
                }
                btnApprove.isDisplayed()
                btnReject.isDisplayed()
            }
        }
    }

    @Test
    fun testConsumeTransactionRequestWithTypeSend_doApprove_ThenBalanceScreenShouldBeDisplayCorrectly() {
        mainScreen {
            bottomNavigation.isDisplayed()

            /* Preparing test data */
            val currentPrimaryTokenId = Storage.loadTokenPrimary()
            val currentTxFormattedIds = Storage.loadFormattedId()
            val currentTxFormattedIdSend = currentTxFormattedIds.split("|")[1]
            val currentBalance = Storage.loadWallets()?.data?.get(0)?.balances?.find { it.token.id == currentPrimaryTokenId }!!

            /* Consume transaction */
            consumeTx(currentBalance.token, currentTxFormattedIdSend)

            idle(1000)

            /* Verify confirmation screen data */
            confirmTransactionRequestScreen {
                tvTokenFrom.isDisplayed()
                btnApprove {
                    isDisplayed()
                    click()
                }
            }

            verifyBalanceHasChanged(
                currentBalance = currentBalance,
                expectedAmount = currentBalance.amount
                    .minus(currentBalance.token.subunitToUnit)
                    .divide(currentBalance.token.subunitToUnit)
            )
        }
    }

    @Test
    fun testConsumeTransactionRequestWithTypeSend_doReject_ThenBalanceScreenShouldBeDisplayCorrectly() {
        mainScreen {
            bottomNavigation.isDisplayed()

            /* Preparing test data */
            val currentPrimaryTokenId = Storage.loadTokenPrimary()
            val currentTxFormattedIds = Storage.loadFormattedId()
            val currentTxFormattedIdSend = currentTxFormattedIds.split("|")[1]
            val currentBalance = Storage.loadWallets()?.data?.get(0)?.balances?.find { it.token.id == currentPrimaryTokenId }!!

            /* Consume transaction */
            val txConsumption = consumeTx(currentBalance.token, currentTxFormattedIdSend)

            idle(1000)

            /* Verify confirmation screen data */
            confirmTransactionRequestScreen {
                tvTokenFrom.isDisplayed()
                btnReject {
                    isDisplayed()
                    click()
                }
            }

            verifyBalanceHasChanged(
                currentBalance = currentBalance,
                expectedAmount = currentBalance.amount.divide(currentBalance.token.subunitToUnit)
            )

            val toastText = KView {
                withText(String.format(stringRes(R.string.notification_transaction_rejected), txConsumption?.account?.name))
            }
            toastText.isDisplayed()
        }
    }

    @Test
    fun testConsumeTransactionRequestWithTypeSend_doConsumeLargeAmount_ThenErrorShouldBeDisplayed() {
        mainScreen {
            bottomNavigation.isDisplayed()

            /* Preparing test data */
            val currentPrimaryTokenId = Storage.loadTokenPrimary()
            val currentTxFormattedIds = Storage.loadFormattedId()
            val currentTxFormattedIdSend = currentTxFormattedIds.split("|")[1]
            val currentBalance = Storage.loadWallets()?.data?.get(0)?.balances?.find { it.token.id == currentPrimaryTokenId }!!

            /* Consume transaction */
            consumeTx(currentBalance.token, currentTxFormattedIdSend, largeAmount = true)

            idle(1000)

            /* Verify confirmation screen data */
            confirmTransactionRequestScreen {
                tvTokenFrom.isDisplayed()
                btnApprove {
                    isDisplayed()
                    click()
                }
                tvErrorDescription {
                    isDisplayed()
                    hasText(
                        String.format(
                            stringRes(R.string.confirm_transaction_request_error_not_has_enough_fund),
                            currentBalance.displayAmount(),
                            currentBalance.token.symbol
                        )
                    )
                }
            }
        }
    }

    @Test
    fun testConsumeTransactionRequestWithTypeReceive_ThenBalanceScreenShouldBeDisplayedCorrectly() {
        mainScreen {
            bottomNavigation.isDisplayed()

            /* Preparing test data */
            val currentPrimaryTokenId = Storage.loadTokenPrimary()
            val currentTxFormattedIds = Storage.loadFormattedId()
            val currentTxFormattedIdReceive = currentTxFormattedIds.split("|")[0]
            val currentBalance = Storage.loadWallets()?.data?.get(0)?.balances?.find { it.token.id == currentPrimaryTokenId }!!

            /* Consume transaction */
            consumeTx(currentBalance.token, currentTxFormattedIdReceive)

            idle(1000)

            verifyBalanceHasChanged(
                currentBalance = currentBalance,
                expectedAmount = currentBalance.amount
                    .plus(currentBalance.token.subunitToUnit)
                    .divide(currentBalance.token.subunitToUnit)
            )
        }
    }

    private fun verifyBalanceHasChanged(currentBalance: Balance, expectedAmount: BigDecimal) {
        balanceScreen {
            val tokenIndex = Storage.loadWallets()?.data?.get(0)?.balances?.indexOfFirst { currentBalance.token.id == it.token.id }!!
            recyclerView {
                isDisplayed()
                childAt<BalanceScreen.Item>(tokenIndex) {
                    val expectedAmountText = String.format("%,.2f", expectedAmount)
                    idle(500)
                    tvAmount.hasText(expectedAmountText)
                }
            }
        }
    }

    private fun consumeTx(token: Token, formattedId: String, largeAmount: Boolean = false): TransactionConsumption? {
        var consumeResponse: TransactionConsumption? = null
        mainScreen {
            bottomNavigation.isDisplayed()
            consumeResponse = if (!largeAmount) {
                consumerClient.consumeTxFormattedId(formattedId, token.subunitToUnit)
            } else {
                consumerClient.consumeTxFormattedId(formattedId, token.subunitToUnit * 100_000_000_000.bd)
            }
            bottomNavigation.isDisplayed()
        }

        return consumeResponse
    }
}
