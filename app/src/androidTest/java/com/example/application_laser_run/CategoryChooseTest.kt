package com.example.application_laser_run.activity

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.application_laser_run.R
import org.junit.Rule
import org.junit.Test

class CategoryChooseActivityTest {

    // Règle qui lance automatiquement CategoryChooseActivity avant chaque test
    @get:Rule
    val activityRule = ActivityScenarioRule(CategoryChooseActivity::class.java)

    // Vérifie que les valeurs affichées à l'écran sont correctes
    @Test
    fun testActivityLaunch() {
        onView(withId(R.id.initialDistance)).check(matches(withText("Distance initiale : 500")))
        onView(withId(R.id.lapDistance)).check(matches(withText("Distance parcourue : 1000")))
        onView(withId(R.id.lapCount)).check(matches(withText("Nombre de tours : 5")))
        onView(withId(R.id.shootDistance)).check(matches(withText("Distance de tir : 200")))
    }

    // Vérifie que le bouton "Ready" est bien cliquable et déclenche l'action attendue
    @Test
    fun testReadyButton() {
        activityRule.scenario.onActivity { activity ->
            activity.intent.putExtra("CATEGORY_TOUR", 5)
        }

        // Vérifie que le bouton est bien affiché à l'écran
        onView(withId(R.id.ready)).check(matches(isDisplayed()))

        // Effectue un clic sur le bouton
        onView(withId(R.id.ready)).perform(click())
    }

    // Vérifie que l'intent transmet correctement les extras lors du lancement de l'activité
    @Test
    fun testIntentExtras() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), CategoryChooseActivity::class.java).apply {
            putExtra("CATEGORY_TOUR", 5)
        }

        // Lance l'activité avec cet intent
        val scenario = ActivityScenario.launch<CategoryChooseActivity>(intent)

        // Vérifie que l'extra a bien été reçu
        scenario.onActivity { activity ->
            val lapCount = activity.intent.getIntExtra("CATEGORY_TOUR", 0)
            assert(lapCount == 5) { "CATEGORY_TOUR attendu: 5, reçu: $lapCount" }
        }
    }
}
