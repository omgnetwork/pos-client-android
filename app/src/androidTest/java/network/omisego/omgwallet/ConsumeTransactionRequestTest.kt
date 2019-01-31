package network.omisego.omgwallet

import androidx.test.ext.junit.runners.AndroidJUnit4
import co.omisego.omisego.extension.bd
import co.omisego.omisego.model.Balance
import co.omisego.omisego.model.Token
import co.omisego.omisego.model.TransactionConsumption
import co.omisego.omisego.model.params.LoginParams
import com.agoda.kakao.screen.Screen.Companion.idle
import network.omisego.omgwallet.extension.calledName
import network.omisego.omgwallet.setup.base.BaseInstrumentalTest
import network.omisego.omgwallet.setup.config.TestData
import network.omisego.omgwallet.setup.custom.assertions.onToast
import network.omisego.omgwallet.setup.custom.matchers.ToastMatcher
import network.omisego.omgwallet.setup.screen.BalanceScreen
import network.omisego.omgwallet.setup.screen.ConfirmTransactionRequestScreen
import network.omisego.omgwallet.setup.screen.MainScreen
import network.omisego.omgwallet.setup.util.TestUtil
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

    companion object : BaseInstrumentalTest() {

        @BeforeClass
        @JvmStatic
        fun setupClass() {
            setupClient()

            val user = LoginParams(TestData.USER_EMAIL, TestData.USER_PASSWORD)
            val consumerUser = LoginParams(TestData.CONSUME_TX_USER_EMAIL, TestData.CONSUME_TX_USER_PASSWORD)

            TestUtil.ensureEnoughBalances(client, user)
            TestUtil.ensureEnoughBalances(client, consumerUser)

            localRepository.deleteSession()
            val response = client.login(user).execute()
            val clientAuthenticationToken = response.body()?.data!!
            localRepository.saveSession(clientAuthenticationToken)
        }
    }

    @Before
    fun setup() {
        setupClient()
        localRepository.deleteTokenPrimary()
        localRepository.deleteTransactionRequest()
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
            val currentPrimaryTokenId = localRepository.loadTokenPrimary()
            val currentTxFormattedIds = localRepository.loadTransactionRequest()
            val currentTxFormattedIdSend = currentTxFormattedIds.split("|")[1]
            val consumeToken: Token = localRepository.loadWallets()?.data?.get(0)?.balances?.find { it.token.id == currentPrimaryTokenId }?.token!!

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
                    hasText("${txConsumption?.calledName()}")
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
            val currentPrimaryTokenId = localRepository.loadTokenPrimary()
            val currentTxFormattedIds = localRepository.loadTransactionRequest()
            val currentTxFormattedIdSend = currentTxFormattedIds.split("|")[1]
            val currentBalance = localRepository.loadWallets()?.data?.get(0)?.balances?.find { it.token.id == currentPrimaryTokenId }!!

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
            val currentPrimaryTokenId = localRepository.loadTokenPrimary()
            val currentTxFormattedIds = localRepository.loadTransactionRequest()
            val currentTxFormattedIdSend = currentTxFormattedIds.split("|")[1]
            val currentBalance = localRepository.loadWallets()?.data?.get(0)?.balances?.find { it.token.id == currentPrimaryTokenId }!!

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

            onToast(ToastMatcher.contains(String.format(
                stringRes(R.string.notification_transaction_rejected),
                txConsumption?.calledName()
            )))
        }
    }

    @Test
    fun testConsumeTransactionRequestWithTypeSend_doConsumeLargeAmount_ThenErrorShouldBeDisplayed() {
        mainScreen {
            bottomNavigation.isDisplayed()

            /* Preparing test data */
            val currentPrimaryTokenId = localRepository.loadTokenPrimary()
            val currentTxFormattedIds = localRepository.loadTransactionRequest()
            val currentTxFormattedIdSend = currentTxFormattedIds.split("|")[1]
            val currentBalance = localRepository.loadWallets()?.data?.get(0)?.balances?.find { it.token.id == currentPrimaryTokenId }!!

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
            val currentPrimaryTokenId = localRepository.loadTokenPrimary()
            val currentTxFormattedIds = localRepository.loadTransactionRequest()
            val currentTxFormattedIdReceive = currentTxFormattedIds.split("|")[0]
            val currentBalance = localRepository.loadWallets()?.data?.get(0)?.balances?.find { it.token.id == currentPrimaryTokenId }!!

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
            val tokenIndex = localRepository.loadWallets()?.data?.get(0)?.balances?.indexOfFirst { currentBalance.token.id == it.token.id }!!
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
