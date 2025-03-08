package com.example.application_laser_run.activity

import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Chronometer
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.application_laser_run.R
import com.example.application_laser_run.adapter.TargetAdapter
import com.example.application_laser_run.model.MyApplication

class ShootActivity : AppCompatActivity() {
    private var countdownTimer: CountDownTimer? = null
    private var timeRemaining: Long = 50000
    private lateinit var sharedPreferences: SharedPreferences
    private var timer: Long = 0
    private var sonnerie: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shoot)

        // Récupération du nombre de tours et du tour actuel
        val nbTour = intent.getIntExtra("NOMBRE_TOUR", 0)
        var tour = intent.getIntExtra("TOUR", 1)

        val targets = listOf("1", "2", "3", "4", "5", "0")
        val listView = findViewById<ListView>(R.id.listCible)
        val adapter = TargetAdapter(this, targets)
        listView.adapter = adapter

        timer = SystemClock.elapsedRealtime()

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedTarget = targets[position].toInt()
            val elapsedTime = SystemClock.elapsedRealtime() - timer

            val app = applicationContext as MyApplication
            val targetsMissed = app.missedTargets
            app.missedTargets = targetsMissed + (5 - selectedTarget)


            val totalDuration = app.totalDuration
            app.totalDuration = totalDuration + elapsedTime
            app.shootDuration.add(elapsedTime)

            val avgShootDuration = if (app.shootDuration.isNotEmpty()) {
                app.shootDuration.average().toLong()
            } else {
                0L // ou une autre valeur par défaut si la liste est vide
            }
            app.avgShootDuration = avgShootDuration

            val shootMaxDuration = app.shootMaxDuration
            app.shootMaxDuration = if (elapsedTime > shootMaxDuration) elapsedTime else shootMaxDuration

            val shootMinDuration = app.shootMinDuration
            app.shootMinDuration = if (elapsedTime < shootMinDuration) elapsedTime else shootMinDuration

            // Vérification du dernier tour
            if (tour < nbTour) {
                // Passer au tour suivant
                tour++
                val intent = Intent(this, RunActivity::class.java)
                intent.putExtra("NOMBRE_TOUR", nbTour)
                intent.putExtra("TOUR", tour)
                startActivity(intent)
                finish()
            } else {
                // Si c'est le dernier tour, aller à l'activité Stat
                val intent = Intent(this, StatActivity::class.java)
                intent.putExtra("NOMBRE_TOUR", nbTour)
                intent.putExtra("TOUR", tour)
                startActivity(intent)
                finish()
            }
        }

        // Initialisation du CountDownTimer
        val chronoTextView = findViewById<TextView>(R.id.chrono)

        countdownTimer = object : CountDownTimer(timeRemaining, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                chronoTextView.text = "$seconds secondes"
            }

            override fun onFinish() {
                chronoTextView.text = "Temps écoulé"
                // Initialiser le MediaPlayer et jouer la sonnerie
                sonnerie = MediaPlayer.create(this@ShootActivity, R.raw.sonnerie)
                sonnerie?.start()
            }
        }
        countdownTimer?.start()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Libérer les ressources du MediaPlayer
    override fun onStop() {
        super.onStop()
        sonnerie?.release()
        sonnerie = null
    }
}
