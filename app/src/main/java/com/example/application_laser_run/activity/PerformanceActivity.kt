package com.example.application_laser_run.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_performance)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val database = AppDatabase.getDatabase(this)
        performanceDao = database.performanceDao()

        listViewPerformance = findViewById(R.id.listViewPerformance)

        // Utilisation de lifecycleScope pour éviter les fuites mémoire
        lifecycleScope.launch {
            val performances = performanceDao.getAll()
            Log.d("PerformanceActivity", "Nombre de performances récupérées: ${performances.size}")

            if (!isFinishing) { // Vérification pour éviter des erreurs si l'activité est fermée
                val adapter = PerformanceAdapter(this@PerformanceActivity, performances)
                listViewPerformance.adapter = adapter
                Log.d("PerformanceActivity", "Adaptateur assigné avec ${adapter.count} éléments")
            }
        }

        val retour = findViewById<Button>(R.id.retour)
        retour.setOnClickListener {
            finish() // Ferme cette activité et revient en arrière proprement
        }
    }
}