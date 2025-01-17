package com.example.application_laser_run.activity

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.application_laser_run.R

class RunActivity : AppCompatActivity() {

    private var chronoBaseTime: Long = 0  // Variable pour stocker le temps de base du chronomètre

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_run)

        // Adapter l'interface pour gérer les barres système
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val chronoEveryTime = findViewById<Chronometer>(R.id.chronoEveryTime)

        // Vérifier si un temps écoulé est passé depuis l'activité précédente
        val elapsedTimeFromPreviousActivity = intent.getLongExtra("elapsedTime", 0L)
        if (elapsedTimeFromPreviousActivity != 0L) {
            // Si un temps est passé, ajuster le chronomètre à la valeur actuelle + le temps écoulé
            chronoBaseTime = SystemClock.elapsedRealtime() - elapsedTimeFromPreviousActivity
        } else {
            // Si aucun temps n'est passé, initialiser normalement
            chronoBaseTime = SystemClock.elapsedRealtime()
        }

        chronoEveryTime.base = chronoBaseTime
        chronoEveryTime.start()

        val buttonRun = findViewById<Button>(R.id.buttonRun)
        buttonRun.setOnClickListener {
            // Récupérer le temps écoulé du chrono
            val chronoTime = SystemClock.elapsedRealtime() - chronoEveryTime.base

            // Passer le temps à l'activité suivante
            val intent = Intent(this, ShootActivity::class.java)
            intent.putExtra("chronoTime", chronoTime)
            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        // Enregistrer l'état du chrono avant de quitter l'activité
        val chronoEveryTime = findViewById<Chronometer>(R.id.chronoEveryTime)
        chronoBaseTime = chronoEveryTime.base + (SystemClock.elapsedRealtime() - chronoEveryTime.base)
    }

    override fun onResume() {
        super.onResume()
        // Restaurer l'état du chrono lorsque l'activité reprend
        val chronoEveryTime = findViewById<Chronometer>(R.id.chronoEveryTime)
        chronoEveryTime.base = chronoBaseTime
        chronoEveryTime.start()
    }
}


    private fun chronoEternal(c: Chronometer){
        c.base
    }

