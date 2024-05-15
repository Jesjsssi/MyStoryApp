package com.dicoding.mystoryapp.view.loginlogout

import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.dicoding.mystoryapp.R
import com.dicoding.mystoryapp.util.EspressoIdlingResource
import com.dicoding.mystoryapp.view.login.LoginActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginLogoutActivityTest{

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun testIfLoginLogoutSuccess() {
        Espresso.onView(withId(R.id.ed_login_email))
            .perform(ViewActions.typeText("frieren@gmail.com"), ViewActions.closeSoftKeyboard())
        Espresso.onView(withId(R.id.ed_login_password))
            .perform(ViewActions.typeText("12345678"), ViewActions.closeSoftKeyboard())
        Espresso.onView(withId(R.id.loginButton))
            .perform(ViewActions.click())
        Espresso.onView(withText(R.string.continue_dialog))
            .perform(ViewActions.click())
        Espresso.onView(withId(R.id.fab_add_story))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.menu_option2))
            .perform(ViewActions.click())
        Espresso.onView(withText(R.string.continue_dialog)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.imageView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}