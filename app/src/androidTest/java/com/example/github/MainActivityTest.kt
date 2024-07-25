package com.example.github

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.github.ui.main.MainActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {
    private val username = "ramaadjiprsty"
    @Before
    fun setup() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun getUser() {
        onView(withId(R.id.searchView)).perform(click(), closeSoftKeyboard())
        onView(withId(R.id.rvUser)).check(matches(isDisplayed()))
        onView(withText(username)).check(matches(isDisplayed()))
        onView(withText(username)).perform(click())
        onView(withId(R.id.detail_user_layout)).check(matches(isDisplayed()))
    }
}
