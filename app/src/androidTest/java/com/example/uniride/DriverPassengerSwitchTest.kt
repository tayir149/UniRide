package com.example.uniride

import androidx.test.core.app.ActivityScenario
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.uniride.activities.CreateTrip
import com.example.uniride.activities.DriverInterface
import com.example.uniride.activities.EditTrip
import com.example.uniride.activities.PassengerInterface
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DriverPassengerSwitchTest {


    @Rule @JvmField
    var activityRule = ActivityTestRule<CreateTrip>(
        CreateTrip::class.java
    )

    //Test if the Driver Interface lunch successfully
    @Test
    fun driverInterfaceLaunchesSuccessfully() {
        ActivityScenario.launch(DriverInterface::class.java)
    }

    //Test if the Passenger Interface lunch successfully
    @Test
    fun passengerInterfaceLaunchesSuccessfully() {
        ActivityScenario.launch(PassengerInterface::class.java)
    }

}


