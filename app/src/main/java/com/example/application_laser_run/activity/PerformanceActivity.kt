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

    // M60 TextViews
    private lateinit var avgTotalDurationM60: TextView
    private lateinit var avgShootDurationM60: TextView
    private lateinit var avgRunDurationM60: TextView
    private lateinit var avgTargetsMissedM60: TextView

    // M40 TextViews
    private lateinit var avgTotalDurationM40: TextView
    private lateinit var avgShootDurationM40: TextView
    private lateinit var avgRunDurationM40: TextView
    private lateinit var avgTargetsMissedM40: TextView

    // Senior TextViews
    private lateinit var avgTotalDurationSenior: TextView
    private lateinit var avgShootDurationSenior: TextView
    private lateinit var avgRunDurationSenior: TextView
    private lateinit var avgTargetsMissedSenior: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_performance)

        // M60
        avgTotalDurationM60 = findViewById(R.id.avgTotalDurationM60)
        avgShootDurationM60 = findViewById(R.id.avgShootDurationM60)
        avgRunDurationM60 = findViewById(R.id.avgRunDurationM60)
        avgTargetsMissedM60 = findViewById(R.id.avgTargetsMissedM60)

        // M40
        avgTotalDurationM40 = findViewById(R.id.avgTotalDurationM40)
        avgShootDurationM40 = findViewById(R.id.avgShootDurationM40)
        avgRunDurationM40 = findViewById(R.id.avgRunDurationM40)
        avgTargetsMissedM40 = findViewById(R.id.avgTargetsMissedM40)

        // Senior
        avgTotalDurationSenior = findViewById(R.id.avgTotalDurationSenior)
        avgShootDurationSenior = findViewById(R.id.avgShootDurationSenior)
        avgRunDurationSenior = findViewById(R.id.avgRunDurationSenior)
        avgTargetsMissedSenior = findViewById(R.id.avgTargetsMissedSenior)

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

        // Récupérer et afficher les moyennes pour M60
        lifecycleScope.launch {
            val avgTotalDurationM60Value = performanceDao.getAvgRunDurationByCategoryName("M60") ?: 0
            val avgShootDurationM60Value = performanceDao.getAvgShootDurationByCategoryName("M60") ?: 0
            val avgTargetsMissedM60Value = performanceDao.getAvgTargetsMissedByCategoryName("M60") ?: 0
            val avgRunDurationM60Value = performanceDao.getAvgRunDurationByCategoryName("M60") ?: 0

            avgTotalDurationM60.text = "Durée totale moyenne : ${formatDuration(avgTotalDurationM60Value)} min"
            avgRunDurationM60.text = "Durée de course moyenne : ${formatDuration(avgRunDurationM60Value)} min"
            avgShootDurationM60.text = "Durée de tir moyenne : ${formatDuration(avgShootDurationM60Value)} min"
            avgTargetsMissedM60.text = "Cibles ratées moyennes : $avgTargetsMissedM60Value"

            // M40
            val avgTotalDurationM40Value = performanceDao.getAvgRunDurationByCategoryName("M40") ?: 0
            val avgShootDurationM40Value = performanceDao.getAvgShootDurationByCategoryName("M40") ?: 0
            val avgTargetsMissedM40Value = performanceDao.getAvgTargetsMissedByCategoryName("M40") ?: 0
            val avgRunDurationM40Value = performanceDao.getAvgRunDurationByCategoryName("M40") ?: 0

            avgTotalDurationM40.text = "Durée totale moyenne : ${formatDuration(avgTotalDurationM40Value)} min"
            avgRunDurationM40.text = "Durée de course moyenne : ${formatDuration(avgRunDurationM40Value)} min"
            avgShootDurationM40.text = "Durée de tir moyenne : ${formatDuration(avgShootDurationM40Value)} min"
            avgTargetsMissedM40.text = "Cibles ratées moyennes : $avgTargetsMissedM40Value"

            // Senior
            val avgTotalDurationSeniorValue = performanceDao.getAvgRunDurationByCategoryName("Senior") ?: 0
            val avgShootDurationSeniorValue = performanceDao.getAvgShootDurationByCategoryName("Senior") ?: 0
            val avgTargetsMissedSeniorValue = performanceDao.getAvgTargetsMissedByCategoryName("Senior") ?: 0
            val avgRunDurationSeniorValue = performanceDao.getAvgRunDurationByCategoryName("Senior") ?: 0

            avgTotalDurationSenior.text = "Durée totale moyenne : ${formatDuration(avgTotalDurationSeniorValue)} min"
            avgRunDurationSenior.text = "Durée de course moyenne : ${formatDuration(avgRunDurationSeniorValue)} min"
            avgShootDurationSenior.text = "Durée de tir moyenne : ${formatDuration(avgShootDurationSeniorValue)} min"
            avgTargetsMissedSenior.text = "Cibles ratées moyennes : $avgTargetsMissedSeniorValue"

            // Log pour débogage
            Log.d("PerformanceActivity", "Moyenne Total Duration M60 : $avgTotalDurationM60Value")
            Log.d("PerformanceActivity", "Moyenne Shoot Duration M60 : $avgShootDurationM60Value")
            Log.d("PerformanceActivity", "Moyenne Targets Missed M60 : $avgTargetsMissedM60Value")
            Log.d("PerformanceActivity", "Moyenne Total Duration M40 : $avgTotalDurationM40Value")
            Log.d("PerformanceActivity", "Moyenne Shoot Duration M40 : $avgShootDurationM40Value")
            Log.d("PerformanceActivity", "Moyenne Targets Missed M40 : $avgTargetsMissedM40Value")
            Log.d("PerformanceActivity", "Moyenne Total Duration Senior : $avgTotalDurationSeniorValue")
            Log.d("PerformanceActivity", "Moyenne Shoot Duration Senior : $avgShootDurationSeniorValue")
            Log.d("PerformanceActivity", "Moyenne Targets Missed Senior : $avgTargetsMissedSeniorValue")
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
