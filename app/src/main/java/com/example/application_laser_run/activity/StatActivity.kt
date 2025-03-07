package com.example.application_laser_run.activity

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.application_laser_run.R
import com.example.application_laser_run.adapter.PerformanceAdapter
import com.example.application_laser_run.dao.PerformanceDao
import com.example.application_laser_run.database.AppDatabase
import com.example.application_laser_run.model.MyApplication
import com.example.application_laser_run.model.Performance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

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
                val performance = Performance(
                    totalDuration = app.totalDuration,
                    runDuration = app.runDuration,
                    avgSpeed = app.avgSpeed,
                    shootDuration = app.shotDuration,
                    shootMinDuration = app.shootMinDuration,
                    shootMaxDuration = app.shootMaxDuration,
                    shootAvgDuration = app.avgShootDuration,
                    missedTargets = app.missedTargets,
                    categoryId = app.categorie
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
                // Récupérer la dernière performance enregistrée
                val lastPerformance = performanceDao.getLastPerformance()

                // Affichage des performances dans les TextViews
                findViewById<TextView>(R.id.totalDurationText).text =
                    "Temps total de l'entraînement: ${formatDuration(lastPerformance.totalDuration)}"
                findViewById<TextView>(R.id.runDurationText).text =
                    "Temps de course: ${formatDuration(lastPerformance.runDuration)}"
                findViewById<TextView>(R.id.avgSpeedText).text =
                    "Vitesse de course moyenne: ${lastPerformance.avgSpeed}km/h"
                findViewById<TextView>(R.id.shootMinDurationText).text =
                    "Temps le plus petit lors du tir: ${formatDuration(lastPerformance.shootMinDuration)}ms"
                findViewById<TextView>(R.id.shootMaxDurationText).text =
                    "Temps le plus long lors du tir: ${formatDuration(lastPerformance.shootMaxDuration)}ms"
                findViewById<TextView>(R.id.avgShootDurationText).text =
                    "Temps moyen lors du tir: ${formatDuration(lastPerformance.shootAvgDuration)}ms"
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