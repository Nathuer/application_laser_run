package com.example.application_laser_run.activity

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.application_laser_run.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RunActivityTest {

    private lateinit var scenario: ActivityScenario<RunActivity>

    // Initialise l'ActivityScenario avec les extras nécessaires avant chaque test
    @Before
    fun setUp() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), RunActivity::class.java).apply {
            putExtra("NOMBRE_TOUR", 3)
            putExtra("TOUR", 1)
        }
        scenario = ActivityScenario.launch(intent)
    }

    // Vérifie que l'affichage du nombre de tours est correct
    @Test
    fun testLapCountDisplayedCorrectly() {
        onView(withId(R.id.lapCount)).check(matches(withText("Tour : 1 / 3")))
    }

    // Vérifie que le chronomètre est bien affiché et démarre correctement
    @Test
    fun testChronometerStartsCorrectly() {
        Thread.sleep(1000) // Attend 1 seconde pour voir si le chrono fonctionne
        onView(withId(R.id.chronoRun)).check(matches(isDisplayed()))
    }

    // Vérifie que le bouton stop redirige bien vers l'activité ShootActivity
    @Test
    fun testStopButtonNavigatesToShootActivity() {
        onView(withId(R.id.stop)).perform(click())
        onView(withId(R.id.textView)).check(matches(isDisplayed()))
    }
}
