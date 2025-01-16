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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_run)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val chronoEveryTime = findViewById<Chronometer>(R.id.chronoEveryTime)



        chronoEveryTime.base = (application as MyApplication).start
        chronoEveryTime.start()

        val imageButton = findViewById<Button>(R.id.buttonRun)
        imageButton.setOnClickListener {
            // Récupérer le temps écoulé de chronoEveryTime
            val chronoTime = SystemClock.elapsedRealtime() - chronoEveryTime.base

            // Créer un Intent pour la nouvelle activité et ajouter le temps du chronomètre
            val intent = Intent(this, ShootActivity::class.java)
            intent.putExtra("chronoTime", chronoTime)  // Passer le temps du chronomètre
            startActivity(intent)
        }
    }

    private fun chronoEternal(c: Chronometer){
        c.base
    }
}
