package pl.toboche.ethchecker

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pl.toboche.ethchecker.base.MainActivity

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @Rule
    @JvmField
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        onView(withText("Address:"))
            .check(matches(isDisplayed()))
        onView(withText("0xde57...78c3"))
            .check(matches(isDisplayed()))
        onView(withText("ETH Balance:"))
            .check(matches(isDisplayed()))
        onView(withText("ERC20 TOKENS"))
            .check(matches(isDisplayed()))
    }
}