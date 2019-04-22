package network.omisego.omgwallet.testMock

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.test.ext.junit.runners.AndroidJUnit4
import co.omisego.omisego.model.ClientAuthenticationToken
import co.omisego.omisego.model.params.LoginParams
import com.agoda.kakao.text.KButton
import network.omisego.omgwallet.R
import network.omisego.omgwallet.setup.base.BaseInstrumentalTest
import network.omisego.omgwallet.setup.config.TestData
import network.omisego.omgwallet.setup.extensions.mockEnqueueWithHttpCode
import network.omisego.omgwallet.setup.screen.BalanceDetailScreen
import network.omisego.omgwallet.setup.screen.BalanceScreen
import network.omisego.omgwallet.setup.screen.LoginScreen
import network.omisego.omgwallet.setup.screen.SplashScreen
import network.omisego.omgwallet.setup.util.NetworkMockUtil
import network.omisego.omgwallet.setup.util.ResourceFile
import org.amshove.kluent.shouldBe
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FailureTest : BaseInstrumentalTest() {
    // API response mock files
    private val mockEmptyBalance: String by ResourceFile("me.get_wallets/empty_balance.json")
    private val mockEmptyWallet: String by ResourceFile("me.get_wallets/empty_wallet.json")
    private val mockOneBalance: String by ResourceFile("me.get_wallets/one_balance.json")
    private val mockTwoBalances: String by ResourceFile("me.get_wallets/two_balances.json")
    private val mockTransactionRequestSend: String by ResourceFile("me.create_transaction_request/send.json")
    private val mockTransactionRequestReceive: String by ResourceFile("me.create_transaction_request/receive.json")
    private val mockErrorInternal: String by ResourceFile("error_internal.json")
    private val mockErrorAuth: String by ResourceFile("error_auth.json")
    private val mockErrorSameAddress: String by ResourceFile("error_transaction_same_address.json")

    // App screen
    private val balanceScreen: BalanceScreen by lazy { BalanceScreen() }
    private val balanceDetailScreen: BalanceDetailScreen by lazy { BalanceDetailScreen() }
    private val splashScreen: SplashScreen by lazy { SplashScreen() }
    private val loginScreen: LoginScreen by lazy { LoginScreen() }

    companion object : BaseInstrumentalTest() {
        lateinit var clientAuthenticationToken: ClientAuthenticationToken
        @BeforeClass
        @JvmStatic
        fun setupClass() {
            setupClient()
            val response = client.login(LoginParams(TestData.USER_EMAIL, TestData.USER_PASSWORD)).execute()
            clientAuthenticationToken = response.body()?.data!!
        }
    }

    @Before
    fun setup() {
        localRepository.deleteTransactionRequest()
        localRepository.deleteTokenPrimary()
        localRepository.saveSession(clientAuthenticationToken)

        setupMockWebServer()
        setupTestUrl()
        setupClient()

        registerIdlingResource()
    }

    @Test
    fun testEmptyBalance() {
        NetworkMockUtil.enqueue(
            mockWebServer,
            mockEmptyBalance,
            mockTransactionRequestReceive,
            mockTransactionRequestSend
        )
        start()
        splashScreen {
            tvStatus.hasText(R.string.error_empty_token)
            btnClose.isDisplayed()
            btnClose.click()
        }
        rule.activity.isFinishing shouldBe true
    }

    @Test
    fun testEmptyWallet() {
        NetworkMockUtil.enqueue(
            mockWebServer,
            mockEmptyWallet,
            mockTransactionRequestReceive,
            mockTransactionRequestSend
        )
        start()
        splashScreen {
            tvStatus.hasText(R.string.error_wallet_not_found)
            btnClose.isDisplayed()
            btnClose.click()
        }
        rule.activity.isFinishing shouldBe true
    }

    @Test
    fun testInternalServerError() {
        mockErrorInternal.mockEnqueueWithHttpCode(mockWebServer, 500)
        start()
        splashScreen {
            tvStatus.containsText("Internal Server Error")
            btnClose.isDisplayed()
            btnClose.click()
        }
        rule.activity.isFinishing shouldBe true
    }

    @Test
    fun testAuthErrorWhenLoadWallets() {
        NetworkMockUtil.enqueue(mockWebServer, mockErrorAuth, mockTransactionRequestReceive, mockTransactionRequestSend)
        start()
        splashScreen {
            tvStatus.containsText("The provided authentication scheme is not supported")
            btnClose.isDisplayed()
            btnClose.click()
        }
        rule.activity.isFinishing shouldBe false
        loginScreen {
            btnLogin.isDisplayed()
        }
    }

    @Test
    fun testAuthErrorWhenCreateReceiveTransactionRequest() {
        NetworkMockUtil.enqueue(mockWebServer, mockOneBalance, mockErrorAuth, mockTransactionRequestSend)
        start()
        splashScreen {
            tvStatus.containsText("The provided authentication scheme is not supported")
            btnClose.isDisplayed()
            btnClose.click()
        }
        rule.activity.isFinishing shouldBe false
        loginScreen {
            btnLogin.isDisplayed()
        }
    }

    @Test
    fun testAuthErrorWhenCreateSendTransactionRequest() {
        NetworkMockUtil.enqueue(mockWebServer, mockOneBalance, mockTransactionRequestReceive, mockErrorAuth)
        start()
        splashScreen {
            tvStatus.containsText("The provided authentication scheme is not supported")
            btnClose.isDisplayed()
            btnClose.click()
        }
        rule.activity.isFinishing shouldBe false
        loginScreen {
            btnLogin.isDisplayed()
        }
    }

    @Test
    fun testNonCriticalError() {
        NetworkMockUtil.enqueue(mockWebServer, mockErrorSameAddress)
        start()
        splashScreen {
            tvStatus.containsText("same_address")
            btnClose.isDisplayed()
            btnClose.click()
        }
        rule.activity.isFinishing shouldBe false
        balanceScreen {
            recyclerView.isDisplayed()
        }
    }

    @Test
    fun testSameAddressErrorWhenSetTokenPrimary() {
        NetworkMockUtil.enqueue(
            mockWebServer,
            mockTwoBalances,
            mockTransactionRequestReceive,
            mockTransactionRequestSend,
            mockErrorSameAddress,
            mockTransactionRequestSend
        )

        start()

        balanceScreen {
            recyclerView {
                lastChild<BalanceScreen.Item> {
                    click()
                }
            }
        }
        balanceDetailScreen {
            /* Verify page, token's symbol and balance amount */
            viewpager {
                /* Click the button "Set Primary" */
                val btnSetPrimary = KButton {
                    withId(R.id.btnSetPrimary)
                    withSibling { withText("ETH") }
                }
                btnSetPrimary.click()
            }
        }

        splashScreen {
            tvStatus.containsText("same_address")
            btnClose.isDisplayed()
            btnClose.click()
        }

        rule.activity.isFinishing shouldBe false

        balanceDetailScreen {
            viewpager.isDisplayed()
            indicator.isDisplayed()
            tvTokenPrimaryHelper.isDisplayed()
        }
    }

    @Test
    fun testAuthErrorWhenSetPrimaryToken() {
        NetworkMockUtil.enqueue(
            mockWebServer,
            mockTwoBalances,
            mockTransactionRequestReceive,
            mockTransactionRequestSend,
            mockTransactionRequestReceive,
            mockErrorAuth
        )
        start()

        balanceScreen {
            recyclerView {
                lastChild<BalanceScreen.Item> {
                    click()
                }
            }
        }

        balanceDetailScreen {
            /* Verify page, token's symbol and balance amount */
            viewpager {
                /* Click the button "Set Primary" */
                val btnSetPrimary = KButton {
                    withId(R.id.btnSetPrimary)
                    withSibling { withText("ETH") }
                }
                btnSetPrimary.click()
            }
        }

        splashScreen {
            tvStatus.containsText("The provided authentication scheme is not supported")
            btnClose.isDisplayed()
            btnClose.click()
        }

        rule.activity.isFinishing shouldBe false

        loginScreen {
            btnLogin.isDisplayed()
        }
    }

    @Test
    fun testUnexpectedErrorWhenSetPrimaryToken() {
        NetworkMockUtil.enqueue(
            mockWebServer,
            mockTwoBalances,
            mockTransactionRequestReceive,
            mockTransactionRequestSend,
            mockTransactionRequestReceive,
            mockErrorInternal
        )
        start()

        balanceScreen {
            recyclerView {
                lastChild<BalanceScreen.Item> {
                    click()
                }
            }
        }

        balanceDetailScreen {
            /* Verify page, token's symbol and balance amount */
            viewpager {
                /* Click the button "Set Primary" */
                val btnSetPrimary = KButton {
                    withId(R.id.btnSetPrimary)
                    withSibling { withText("ETH") }
                }
                btnSetPrimary.click()
            }
        }

        splashScreen {
            tvStatus.containsText("Internal Server Error")
            btnClose.isDisplayed()
            btnClose.click()
        }

        rule.activity.isFinishing shouldBe true
    }

    @After
    fun teardown() {
        unregisterIdlingResource()
    }
}
