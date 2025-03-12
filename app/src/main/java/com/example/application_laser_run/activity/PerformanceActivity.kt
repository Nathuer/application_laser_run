package com.example.application_laser_run.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.application_laser_run.R
import com.example.application_laser_run.adapter.PerformanceAdapter
import com.example.application_laser_run.dao.PerformanceDao
import com.example.application_laser_run.database.AppDatabase
import kotlinx.coroutines.launch

class PerformanceActivity : AppCompatActivity() {
    private lateinit var performanceDao: PerformanceDao
    private lateinit var listViewPerformance: ListView
    private lateinit var avgTotalDuration: TextView
    private lateinit var avgRunDuration: TextView
    private lateinit var avgShootDuration: TextView
    private lateinit var avgTargetsMissed: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_performance)

        avgTotalDuration = findViewById(R.id.avgTotalDuration)
        avgRunDuration = findViewById(R.id.avgRunDuration)
        avgShootDuration = findViewById(R.id.avgShootDuration)
        avgTargetsMissed = findViewById(R.id.avgTargetsMissed)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val database = AppDatabase.getDatabase(this)
        performanceDao = database.performanceDao()

        listViewPerformance = findViewById(R.id.listViewPerformance)

        lifecycleScope.launch {
            val performances = performanceDao.getAll()
            Log.d("PerformanceActivity", "Nombre de performances récupérées: ${performances.size}")

            if (!isFinishing) {
                val adapter = PerformanceAdapter(this@PerformanceActivity, performances)
                listViewPerformance.adapter = adapter
                Log.d("PerformanceActivity", "Adaptateur assigné avec ${adapter.count} éléments")
            }
        }

        // Récupérer et afficher les moyennes
        val categoryId = 0 // Remplace par l'ID de catégorie souhaité
        lifecycleScope.launch {
            val avgTotalDurationValue = performanceDao.getAvgTotalDuration(categoryId) ?: 0
            val avgRunDurationValue = performanceDao.getAvgRunDuration(categoryId) ?: 0
            val avgShootDurationValue = performanceDao.getAvgShootDuration(categoryId) ?: 0
            val avgTargetsMissedValue = performanceDao.getAvgTargetsMissed(categoryId) ?: 0

            // Mettre à jour l'UI
            avgTotalDuration.text = "Vos entraînements ont duré en moyenne : ${formatDuration(avgTotalDurationValue)} min"
            avgRunDuration.text = "Sur tous vos entraînements, vous avez couru en moyenne : ${formatDuration(avgRunDurationValue)} min"
            avgShootDuration.text = "Sur tous vos entraînements, vous avez tiré en moyenne : ${formatDuration(avgShootDurationValue)} min"
            avgTargetsMissed.text = "Sur tous vos entraînements, vous ratez en moyenne : $avgTargetsMissedValue cibles"

            // Log pour débogage
            Log.d("PerformanceActivity", "Category ID utilisé : $categoryId")
            Log.d("PerformanceActivity", "Moyenne Total Duration : $avgTotalDurationValue")
            Log.d("PerformanceActivity", "Moyenne Run Duration : $avgRunDurationValue")
            Log.d("PerformanceActivity", "Moyenne Shoot Duration : $avgShootDurationValue")
            Log.d("PerformanceActivity", "Moyenne Targets Missed : $avgTargetsMissedValue")
        }

        val retour = findViewById<Button>(R.id.retour)
        retour.setOnClickListener {
            finish()
        }
    }

    private fun formatDuration(durationMs: Long): String {
        val minutes = (durationMs / 1000) / 60
        val seconds = (durationMs / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}
