package com.example.uniride

import androidx.test.core.app.ActivityScenario

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.uniride.activities.CreateTrip
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)


class CreateTripTest{

    @Test
    fun appLaunchesSuccessfully() {
        ActivityScenario.launch(CreateTrip::class.java)
    }





}