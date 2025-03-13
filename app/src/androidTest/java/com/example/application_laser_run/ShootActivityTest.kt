package com.example.application_laser_run.activity

import android.content.Intent
import android.os.SystemClock
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import org.junit.Before
import org.junit.Test
import com.example.application_laser_run.R
import org.hamcrest.Matcher
import org.hamcrest.Matchers.anything

class ShootActivityTest {

    @Before
    fun setUp() {
        // Préparation de l'intent et lancement de l'activité
        val intent = Intent().apply {
            putExtra("NOMBRE_TOUR", 3)
            putExtra("TOUR", 1)
        }
        ActivityScenario.launch(ShootActivity::class.java)
    }

    @Test
    fun testChronometerIsDisplayed() {
        // Vérifie que le chronomètre est bien affiché
        onView(withId(R.id.chrono))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testListViewIsPopulated() {
        // Vérifie que la ListView est affichée et contient des éléments
        onView(withId(R.id.listCible))
            .check(matches(isDisplayed()))
            .check(matches(hasMinimumChildCount(1)))
    }

    @Test
    fun testItemClickUpdatesDataAndNavigates() {
        // Simule un clic sur un élément de la ListView
        val selectedTargetPosition = 2
        onData(anything())
            .inAdapterView(withId(R.id.listCible))
            .atPosition(selectedTargetPosition)
            .perform(click())

        // Vérifie que le chronomètre affiche "Temps écoulé"
        onView(withId(R.id.chrono)).check(matches(withText("Temps écoulé")))
    }

    @Test
    fun testSonneriePlaysWhenTimeIsUp() {
        // Simule la fin du compte à rebours et vérifie que la sonnerie se déclenche
        onView(withId(R.id.chrono))
            .perform(waitFor(50000))
        onView(withId(R.id.chrono)).check(matches(withText("Temps écoulé")))
    }

    // Fonction pour attendre une durée spécifique
    fun waitFor(duration: Long): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "Attendre " + duration + " milliseconds"
            }

            override fun perform(uiController: UiController?, view: View?) {
                Thread.sleep(duration)
            }
        }
    }
}
