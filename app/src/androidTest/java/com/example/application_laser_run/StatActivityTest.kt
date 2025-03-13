package com.example.application_laser_run.activity

import android.widget.Button
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.example.application_laser_run.R
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
@LargeTest
class StatActivityTest {

    private lateinit var scenario: ActivityScenario<StatActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(StatActivity::class.java)
    }

    @Test
    fun testTextViews() {
        scenario.onActivity { activity ->
            // Vérifie que les TextViews sont présents et contiennent les bonnes valeurs
            val totalDurationText = activity.findViewById<TextView>(R.id.totalDurationText)
            val runDurationText = activity.findViewById<TextView>(R.id.runDurationText)
            val avgSpeedText = activity.findViewById<TextView>(R.id.avgSpeedText)
            val shootMinDurationText = activity.findViewById<TextView>(R.id.shootMinDurationText)
            val shootMaxDurationText = activity.findViewById<TextView>(R.id.shootMaxDurationText)
            val avgShootDurationText = activity.findViewById<TextView>(R.id.avgShootDurationText)
            val missedTargetsText = activity.findViewById<TextView>(R.id.missedTargetsText)

            assertNotNull("TextView totalDurationText is null", totalDurationText)
            assertNotNull("TextView runDurationText is null", runDurationText)
            assertNotNull("TextView avgSpeedText is null", avgSpeedText)
            assertNotNull("TextView shootMinDurationText is null", shootMinDurationText)
            assertNotNull("TextView shootMaxDurationText is null", shootMaxDurationText)
            assertNotNull("TextView avgShootDurationText is null", avgShootDurationText)
            assertNotNull("TextView missedTargetsText is null", missedTargetsText)

            assertTrue("Le texte de totalDurationText ne contient pas le bon format",
                totalDurationText.text.contains(":"))
            assertTrue("Le texte de runDurationText ne contient pas le bon format",
                runDurationText.text.contains(":"))
        }
    }

    @Test
    fun testComebackButton() {
        scenario.onActivity { activity ->
            val comebackButton = activity.findViewById<Button>(R.id.comebackMain)
            assertNotNull("Button comebackMain is null", comebackButton)

            comebackButton.performClick()
        }
    }
}
