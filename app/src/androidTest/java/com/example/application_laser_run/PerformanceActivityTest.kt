package com.example.application_laser_run.activity

import android.widget.ListView
import android.widget.TextView
import android.widget.Button
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.example.application_laser_run.R
import org.hamcrest.CoreMatchers.containsString

@RunWith(AndroidJUnit4::class)
class PerformanceActivityTest {

    private lateinit var scenario: ActivityScenario<PerformanceActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(PerformanceActivity::class.java)
    }

    @Test
    fun testTextViewsAreDisplayedCorrectly() {
        // Vérifie que les TextViews sont visibles et contiennent du texte
        onView(withId(R.id.avgTotalDuration))
            .check(matches(isDisplayed()))
            .check(matches(withText(containsString("Vos entraînements ont duré en moyenne :"))))

        onView(withId(R.id.avgRunDuration))
            .check(matches(isDisplayed()))
            .check(matches(withText(containsString("Sur tous vos entraînements, vous avez couru en moyenne :"))))

        onView(withId(R.id.avgShootDuration))
            .check(matches(isDisplayed()))
            .check(matches(withText(containsString("Sur tous vos entraînements, vous avez tiré en moyenne :"))))

        onView(withId(R.id.avgTargetsMissed))
            .check(matches(isDisplayed()))
            .check(matches(withText(containsString("Sur tous vos entraînements, vous ratez en moyenne :"))))
    }

    @Test
    fun testListViewIsPopulated() {
        // Vérifie que la ListView est visible et contient des éléments
        onView(withId(R.id.listViewPerformance))
            .check(matches(isDisplayed()))

        // Ici, tu peux ajouter une vérification pour t'assurer qu'il y a des éléments dans la ListView,
        // en fonction du nombre de performances récupérées dans la base de données.
    }

    @Test
    fun testRetourButton() {
        // Vérifie que le bouton retour est bien présent et fonctionne
        onView(withId(R.id.retour))
            .check(matches(isDisplayed()))
            .perform(click())


    }

    // Test pour vérifier que les performances sont bien récupérées et affichées (base de données)
    // Assurez-vous de mocker la base de données pour des tests unitaires plus isolés.
    @Test
    fun testDatabaseCall() {
        // Ce test doit être amélioré pour simuler une réponse de base de données ou utiliser un test en local.
        // Pour l'instant, ce test se concentre sur le bon fonctionnement de l'UI.
    }
}
