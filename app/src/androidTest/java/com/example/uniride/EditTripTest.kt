package com.example.uniride

import androidx.test.core.app.ActivityScenario

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.uniride.activities.EditTrip
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)

class EditTripTest{

    @Test
    fun pageLaunchesSuccessfully() {
        ActivityScenario.launch(EditTrip::class.java)
    }
}