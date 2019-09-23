package com.example.uniride

import androidx.test.core.app.ActivityScenario
import com.example.uniride.activities.CreateTrip
import org.junit.Test


class CreateTripActivityTest {

    @Test
    fun activityLaunchesSuccessfully(){
        ActivityScenario.launch(CreateTrip::class.java)
    }
}