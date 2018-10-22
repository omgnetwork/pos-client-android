package network.omisego.omgwallet

import androidx.test.runner.AndroidJUnit4
import co.omisego.omisego.model.params.LoginParams
import com.agoda.kakao.KButton
import network.omisego.omgwallet.base.BaseInstrumentalTest
import network.omisego.omgwallet.config.LocalClientSetup
import network.omisego.omgwallet.config.TestData
import network.omisego.omgwallet.model.Credential
import network.omisego.omgwallet.network.ClientProvider
import network.omisego.omgwallet.screen.BalanceDetailScreen
import network.omisego.omgwallet.screen.BalanceScreen
import network.omisego.omgwallet.screen.MainScreen
import network.omisego.omgwallet.screen.SplashScreen
import network.omisego.omgwallet.storage.Storage
import org.amshove.kluent.shouldNotBe
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 22/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
@RunWith(AndroidJUnit4::class)
class BalanceDetailTest : BaseInstrumentalTest() {
    private val mainScreen: MainScreen by lazy { MainScreen() }
    private val balanceScreen: BalanceScreen by lazy { BalanceScreen() }
    private val balanceDetailScreen: BalanceDetailScreen by lazy { BalanceDetailScreen() }
    private val splashScreen: SplashScreen by lazy { SplashScreen() }

    companion object {
        @BeforeClass
        @JvmStatic
        fun setupClass() {
            ClientProvider.init(LocalClientSetup())
            Storage.clearSession()
            val response = ClientProvider.client.login(LoginParams(TestData.USER_EMAIL, TestData.USER_PASSWORD)).execute()
            Storage.saveUser(response.body()!!.data.user)
            Storage.saveCredential(Credential(response.body()!!.data.authenticationToken))
            Storage.saveUserEmail(TestData.USER_EMAIL)
        }
    }

    @Before
    fun setup() {
        registerIdlingResource()
        start()
    }

    @After
    fun teardown() {
        unregisterIdlingResource()
    }

    @Test
    fun testBalanceDetailShouldBeDisplayedCorrectly() {
        mainScreen {
            bottomNavigation.isDisplayed()

            val balances = Storage.loadWallets()?.data?.get(0)?.balances!!
            val totalPage = balances.size
            val firstToken = balances[0].token
            val lastToken = balances[totalPage - 1].token

            /* Click at first token */
            balanceScreen {
                recyclerView {
                    firstChild<BalanceScreen.Item> {
                        click()
                    }
                }
            }

            balanceDetailScreen {
                viewpager.isDisplayed()
                indicator.isDisplayed()
                tvTokenPrimaryHelper.isDisplayed()

                /* Verify page, token's symbol and balance amount */
                viewpager {
                    isAtPage(0)
                    hasDescendant {
                        withText(firstToken.symbol)
                    }
                    hasDescendant {
                        withText(balances[0].displayAmount())
                    }
                }

                pressBack()
            }

            /* Do the same thing with the last token */
            balanceScreen {
                recyclerView {
                    swipeUp()
                    lastChild<BalanceScreen.Item> {
                        click()
                    }
                }
            }

            balanceDetailScreen {
                viewpager.isDisplayed()
                indicator.isDisplayed()
                tvTokenPrimaryHelper.isDisplayed()

                viewpager {
                    isAtPage(totalPage - 1)
                    hasDescendant {
                        withText(lastToken.symbol)
                    }
                    hasDescendant {
                        withText(balances[totalPage - 1].displayAmount())
                    }
                }

                pressBack()
            }
        }
    }

    @Test
    fun testBalanceDetailShouldBeSwipable() {
        mainScreen {
            bottomNavigation.isDisplayed()

            /* Click at first token */
            balanceScreen {
                recyclerView {
                    firstChild<BalanceScreen.Item> {
                        click()
                    }
                }
            }

            balanceDetailScreen {
                viewpager.isDisplayed()
                indicator.isDisplayed()
                tvTokenPrimaryHelper.isDisplayed()

                viewpager {
                    swipeLeft()
                    isAtPage(1)
                    swipeLeft()
                    isAtPage(2)
                    swipeRight()
                    isAtPage(1)
                    swipeRight()
                    isAtPage(0)
                }
            }
        }
    }

    @Test
    fun testSetPrimaryToken() {
        mainScreen {
            bottomNavigation.isDisplayed()

            val primaryTokenId = Storage.loadTokenPrimary()
            val balances = Storage.loadWallets()?.data?.get(0)?.balances!!
            val nextPrimaryBalance = balances.find { it.token.id != primaryTokenId }!!
            val nextPrimaryBalanceIndex = balances.indexOfFirst { it.token.id == nextPrimaryBalance.token.id }

            /* Click at first token */
            balanceScreen {
                recyclerView {
                    childAt<BalanceScreen.Item>(nextPrimaryBalanceIndex) {
                        click()
                    }
                }
            }

            balanceDetailScreen {
                viewpager {
                    hasDescendant {
                        withText(stringRes(R.string.balance_detail_token_set_primary))
                    }
                }
                val btnSetPrimary = KButton {
                    withId(R.id.btnSetPrimary)
                    withSibling { withText(nextPrimaryBalance.token.symbol) }
                }
                btnSetPrimary.click()

                primaryTokenId shouldNotBe Storage.loadTokenPrimary()
                btnSetPrimary.isDisabled()
                btnSetPrimary.hasText(R.string.balance_detail_token_primary)

                pressBack()
            }

            balanceScreen {
                recyclerView {
                    childAt<BalanceScreen.Item>(nextPrimaryBalanceIndex) {
                        tvCurrencyName.hasText(nextPrimaryBalance.token.name)
                        tvPrimaryToken.isDisplayed()
                    }
                }
            }
        }
    }
}
