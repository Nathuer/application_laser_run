package com.example.application_laser_run.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.application_laser_run.R
import com.example.application_laser_run.dao.PerformanceDao
import com.example.application_laser_run.database.AppDatabase
import com.example.application_laser_run.model.MyApplication
import com.example.application_laser_run.model.Performance
import kotlinx.coroutines.launch

class StatActivity : AppCompatActivity() {

    private lateinit var performanceDao: PerformanceDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stat)

        // Récupérer l'instance de la base de données via le contexte de l'application
        val app = applicationContext as MyApplication
        performanceDao = AppDatabase.getDatabase(applicationContext).performanceDao()

        // Calcul du temps total en heures
        Log.d("StatActivity", "totalDuration: ${app.runDuration}")
        val timesRun = app.runDuration / 1000
        Log.d("StatActivity", "timesInHours: $timesRun")
        val timesInHours = timesRun / 3600.0

        // Calcul de la vitesse moyenne
        val distanceKM = (app.initialDistanceInCategory + app.lapCountInCategory * app.lapDistanceInCategory) / 1000
        Log.d("StatActivity", "distanceKM: $distanceKM, timesInHours: $timesRun")

        app.avgSpeed = if (timesInHours > 0) {
            (distanceKM / timesInHours).toInt() // Diviser par le nombre d'heures
        } else {
            0
        }

        val comebackButton = findViewById<Button>(R.id.comebackMain)
        comebackButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Enregistrer les performances dans la base de données
        savePerformance()

        // Récupérer et afficher les performances enregistrées
        displayPerformance()
    }

    private fun savePerformance() {
        lifecycleScope.launch {
            try {
                val app = applicationContext as MyApplication

                // Création de la performance à enregistrer
                val categoryName = if (app.categorieName.isNotBlank()) app.categorieName else "Inconnue"
                val performance = Performance(
                    totalDuration = app.totalDuration,
                    runDuration = app.runDuration,
                    avgSpeed = app.avgSpeed,
                    shootDuration = app.shotDuration,
                    shootMinDuration = app.shootMinDuration,
                    shootMaxDuration = app.shootMaxDuration,
                    shootAvgDuration = app.avgShootDuration,
                    missedTargets = app.missedTargets,
                    categoryId = app.categorie,
                    categoryName = app.categorieName
                )

                // Enregistrer la performance dans la base de données
                performanceDao.insert(performance)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun displayPerformance() {
        lifecycleScope.launch {
            try {
                val app = applicationContext as MyApplication

                // Récupérer la dernière performance enregistrée
                val lastPerformance = performanceDao.getLastPerformance()

                // Affichage des performances dans les TextViews
                findViewById<TextView>(R.id.categoryName).text = "Rappel de la catégorie: ${app.categorieName}"

                findViewById<TextView>(R.id.totalDurationText).text =
                    "Temps total de l'entraînement: ${formatDuration(lastPerformance.totalDuration)}"
                findViewById<TextView>(R.id.runDurationText).text =
                    "Temps de course: ${formatDuration(lastPerformance.runDuration)}"
                findViewById<TextView>(R.id.avgSpeedText).text =
                    "Vitesse de course moyenne: ${lastPerformance.avgSpeed} km/h"
                findViewById<TextView>(R.id.shootMinDurationText).text =
                    "Temps le plus petit lors du tir: ${formatDuration(lastPerformance.shootMinDuration)} min"
                findViewById<TextView>(R.id.shootMaxDurationText).text =
                    "Temps le plus long lors du tir: ${formatDuration(lastPerformance.shootMaxDuration)} min"
                findViewById<TextView>(R.id.avgShootDurationText).text =
                    "Temps moyen lors du tir: ${formatDuration(lastPerformance.shootAvgDuration)} min"
                findViewById<TextView>(R.id.missedTargetsText).text =
                    "Nombre de cibles manquées: ${lastPerformance.missedTargets}"

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    // Fonction pour formater la durée en mm:ss
    private fun formatDuration(durationMs: Long): String {
        val minutes = (durationMs / 1000) / 60
        val seconds = (durationMs / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}