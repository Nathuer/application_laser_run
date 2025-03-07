package com.example.application_laser_run.activity

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Button
import android.widget.Chronometer
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.application_laser_run.R
import com.example.application_laser_run.database.AppDatabase
import com.example.application_laser_run.model.MyApplication

class RunActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_run)

        // Initialiser la base de données Room (si nécessaire)
        val database = AppDatabase.getDatabase(this)
        val performanceDao = database.performanceDao()

        // Récupérer les données des extras passées par l'activité précédente
        val nbTour = intent.getIntExtra("NOMBRE_TOUR", 0)
        var tour = intent.getIntExtra("TOUR", 1)

        // Mettre à jour l'affichage du nombre de tours
        findViewById<Button>(R.id.lapCount).text = "Tour : $tour / $nbTour"

        // Initialiser le chronomètre
        val chronoRun = findViewById<Chronometer>(R.id.chronoRun)
        chronoRun.base = SystemClock.elapsedRealtime()
        chronoRun.start()

        // Bouton pour arrêter et passer à l'activité suivante
        val stop = findViewById<Button>(R.id.stop)
        stop.setOnClickListener {
            val elapsedTime = SystemClock.elapsedRealtime() - chronoRun.base

            // Mise à jour des durées dans MyApplication
            val app = applicationContext as MyApplication
            val runDuration = app.runDuration
            app.runDuration = runDuration + elapsedTime

            val totalDuration = app.totalDuration
            app.totalDuration = totalDuration + elapsedTime

            // Passer à l'activité suivante (ShootActivity)
            val intent = Intent(this, ShootActivity::class.java)
            intent.putExtra("NOMBRE_TOUR", nbTour)
            intent.putExtra("TOUR", tour) // Le tour actuel
            startActivity(intent)
            finish()
        }

        // Adapter l'interface pour gérer les barres système
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
