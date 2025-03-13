package com.example.application_laser_run

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import com.example.application_laser_run.activity.MainActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import java.util.regex.Pattern.matches

class MainActivityTest {
    //vérifie que MainActivity est bien lancée
    @Before
    fun setUp() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    //vérifie le fonctionnement du bouton "statPerf"
    @Test
    fun testButtonStatPerf() {
        onView(withId(R.id.statPerf))
            .check(matches(isDisplayed()))
            .perform(click())
    }

    //vérifie le fonctionnement du bouton "maps"
    @Test
    fun testButtonMaps() {
        onView(withId(R.id.maps))
            .check(matches(isDisplayed()))
            .perform(click())
    }

    //vérifie le fonctionnement de la list view"
    @Test
    fun testListViewCategories() {
        onView(withId(R.id.listCategories))
            .check(matches(isDisplayed()))
    }

    //vérifie le fonctionnement du click sur la list view
    @Test
    fun testCategoryItemClick() {
        onView(withId(R.id.listCategories))
            .perform(click())
    }
}
