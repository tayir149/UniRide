package com.example.uniride

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.uniride.activities.SignUp
import com.example.uniride.activities.VideoHelp
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FacebookUsernameTest {

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule<SignUp> (
        SignUp::class.java
    )

    @Test
    @Throws (Exception::class)
    fun videoHelpTest() {
        Intents.init()
        onView(withId(R.id.useridtext)).perform(click())
        intended(hasComponent(VideoHelp::class.java.name))
    }

    /*
    @Test
    @Throws (Exception::class)
    fun signUpTest() {
        Intents.init()
        onView(withId(R.id.firstName)).perform(typeText(firstName))
        onView(withId(R.id.lastName)).perform(typeText(lastName))
        onView(withId(R.id.address)).perform(typeText(address)).perform(
            closeSoftKeyboard())
        onView(withId(R.id.username)).perform(typeText(username)).perform(
            closeSoftKeyboard())
        onView(withId(R.id.password_signup)).perform(typeText(password)).perform(
            closeSoftKeyboard())
        onView(withId(R.id.userid)).perform(typeText(userID)).perform(
            closeSoftKeyboard())
        onView(withId(R.id.login)).perform((click()))
        intended(hasComponent(PassengerDriverSelector::class.java.name))
        */
}

