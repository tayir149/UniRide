package com.example.uniride

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.uniride.activities.Messenger
import com.example.uniride.activities.SignUp
import com.example.uniride.activities.VideoHelp
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MessengerTest {

    @get:Rule
    var activityTestRule: ActivityTestRule<Messenger> =
        ActivityTestRule(Messenger::class.java, false, false)

    @Test
    @Throws (Exception::class)
    fun messengerTest() {
        val intent = Intent()
        intent.putExtra("userID", "royalty37")
        activityTestRule.launchActivity(intent)
    }
}