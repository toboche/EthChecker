package pl.toboche.ethchecker

import android.os.StrictMode
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jakewharton.espresso.OkHttp3IdlingResource
import com.squareup.rx2.idler.Rx2Idler
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.reactivex.plugins.RxJavaPlugins
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pl.toboche.ethchecker.base.MainActivity
import pl.toboche.ethchecker.mockserver.SuccessDispatcher
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class HomeScreenTest {

    @Rule
    @JvmField
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Rule
    @JvmField
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var okHttpClient: OkHttpClient

    val mockWebServer = MockWebServer()

    @Before
    fun setup() {
        //This could be put inside a TestRule as a next step,
        //to be shared with the next testing classes
        hiltRule.inject()
        mockWebServer.start(8080)
        mockWebServer.dispatcher = SuccessDispatcher()
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
        RxJavaPlugins.setInitComputationSchedulerHandler(
            Rx2Idler.create("RxJava 2.x Computation Scheduler")
        )
        RxJavaPlugins.setInitIoSchedulerHandler(
            Rx2Idler.create("RxJava 2.x IO Scheduler")
        )
        val okHttp3IdlingResource = OkHttp3IdlingResource.create("OkHttp", okHttpClient)
        IdlingRegistry.getInstance().register(okHttp3IdlingResource)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun showExpectedStaticTexts() {
        onView(withText("Address:"))
            .check(matches(isDisplayed()))
        onView(withText("0xde57...78c3"))
            .check(matches(isDisplayed()))
        onView(withText("ETH Balance:"))
            .check(matches(isDisplayed()))
        onView(withText("ERC20 TOKENS"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun loadEthBalance() {
        onView(withId(R.id.balance_text))
            .check(matches(withText("$278.78 (0.15 ETH)")))
    }
}