package com.example.application_laser_run.activity

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.application_laser_run.R
import com.example.application_laser_run.adapter.PerformanceAdapter
import com.example.application_laser_run.database.AppDatabase
import com.example.application_laser_run.model.Performance
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class StatActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PerformanceAdapter
    private lateinit var totalDurationText: TextView
    private lateinit var totalRunText: TextView
    private lateinit var averageDurationText: TextView
    private lateinit var targetCountsText: TextView
    private lateinit var beginHourText: TextView
    private var chronoTime: Long = 0L // Ajout de chronoTime
    private var chronoRun: Long = 0L // Ajout de chronoRun

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stat)

        // Initialisation des vues
        recyclerView = findViewById(R.id.recyclerView)
        totalDurationText = findViewById(R.id.totalDuration)
        totalRunText = findViewById(R.id.totalRun)
        targetCountsText = findViewById(R.id.targetCounts)
        beginHourText = findViewById(R.id.beginHour)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Récupérer la valeur de `chronoTime` de l'intent
        chronoTime = intent.getLongExtra("chronoTime", 1L)
        Log.d("StatActivity", "chronoTime récupéré: $chronoTime")

        chronoRun = intent.getLongExtra("chronoEveryRun", 1L)
        Log.d("StatActivity", "chronoRun récupéré: $chronoRun")



        // Passer le context à l'adaptateur
        adapter = PerformanceAdapter(emptyList())
        recyclerView.adapter = adapter

        // Charger les données en arrière-plan
        GlobalScope.launch {
            val db = AppDatabase.getInstance(applicationContext)
            Log.d("StatActivity", "Application context: $applicationContext")
            val performances = db.performanceDao().getAll()

            // Mettre à jour le RecyclerView
            adapter.updateData(performances)

            // Récupérer la dernière performance et afficher l'heure de début
            val lastPerformance = db.performanceDao().getLastPerformance()
            lastPerformance?.let {
                val startTime = it.startTime
                // Formater l'heure en un format lisible
                val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val formattedTime = sdf.format(Date(startTime))
                beginHourText.text = "Heure de début: $formattedTime"
            }

            // Calculer et afficher les statistiques générales
            updateStatistics(performances)
        }
    }

    private fun updateStatistics(performances: List<Performance>) {
        if (performances.isNotEmpty()) {
            val sharedPreferences = getSharedPreferences("game_prefs", MODE_PRIVATE)

            // Ajouter chronoTime à la somme des durées des performances
            val totalDuration = chronoTime
            val totalRun = chronoRun
            val totalMissedTargets = sharedPreferences.getInt("totalMissedTargets", 0)

            totalDurationText.text = "Temps total: ${formatDuration(totalDuration)}"
            totalRunText.text = "Temps passé à courir: ${formatDuration(totalRun)}"
            targetCountsText.text = "Cibles manquées: ${totalMissedTargets}"
        } else {
            totalDurationText.text = "Aucune donnée"
            targetCountsText.text = "Aucune donnée"
        }
    }

    // Fonction pour formater la durée en mm:ss
    private fun formatDuration(durationMs: Long): String {
        val minutes = (durationMs / 1000) / 60
        val seconds = (durationMs / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun formatTime(timeInMillis: Long): String {
        val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val date = Date(timeInMillis)
        return formatter.format(date)
    }

}



