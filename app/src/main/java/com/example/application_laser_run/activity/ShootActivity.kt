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
import com.example.application_laser_run.R

class ShootActivity : AppCompatActivity() {
    private var countdownTimer: CountDownTimer? = null
    private var timeRemaining: Long = 50000
    private lateinit var sharedPreferences: SharedPreferences
    private var tour: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shoot)

        sharedPreferences = getSharedPreferences("game_prefs", MODE_PRIVATE)
        val app = application as MyApplication

        // Récupération du nombre de tours
        tour = intent.getIntExtra("CATEGORY_TOUR", 1)
        sharedPreferences.edit().putInt("tour", tour).apply()

        // Gestion du roundCount
        val lastSessionTime = sharedPreferences.getLong("lastSessionTime", 0L)
        val currentTime = System.currentTimeMillis()
        app.roundCount = if (currentTime - lastSessionTime > 5000) 1 else sharedPreferences.getInt("roundCount", 1)

        updateRoundTextView()

        // Gestion du chronomètre
        val chronoTime = intent.getLongExtra("chronoTime", 0L)
        val chronoRun = intent.getLongExtra("chronoEveryRun", 0L)
        app.chronometer += chronoRun

        val chronoEveryTime = findViewById<Chronometer>(R.id.chronoEveryTime)

        chronoEveryTime.base = SystemClock.elapsedRealtime() - chronoTime
        chronoEveryTime.start()

        startCountdown()
        fetchTarget()
    }

    private fun updateRoundTextView() {
        val roundTextView = findViewById<TextView>(R.id.textTour)
        val app = application as MyApplication
        roundTextView.text = "Tour n° ${app.roundCount}"

        if (app.roundCount > tour) {
            val chronoEveryTime = findViewById<Chronometer>(R.id.chronoEveryTime)
            Toast.makeText(this, "Fin du jeu. Vous avez terminé tous les tours.", Toast.LENGTH_SHORT).show()
            val i = Intent(this, StatActivity::class.java)
            val chronoFinal = SystemClock.elapsedRealtime() - chronoEveryTime.base
            i.putExtra("chronoTime", chronoFinal)
            i.putExtra("chronoEveryRun", app.chronometer)
            i.putExtra("chronoShoot", app.chronometerForRuntime)
            Log.d("ShootActivity", "ChronoShoot envoyé: ${app.chronometerForRuntime}")
            startActivity(i)
        }
    }

    private fun startCountdown() {
        val countdownTextView = findViewById<TextView>(R.id.countdownTextView)
        countdownTimer = object : CountDownTimer(timeRemaining, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeRemaining = millisUntilFinished
                val minutes = (timeRemaining / 1000) / 60
                val seconds = (timeRemaining / 1000) % 60
                countdownTextView.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                val chronoEveryTime = findViewById<Chronometer>(R.id.chronoEveryTime)
                chronoEveryTime.stop()

                AlertDialog.Builder(this@ShootActivity)
                    .setTitle("Temps écoulé")
                    .setMessage("Le temps du tour est terminé !")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                        val intent = Intent(this@ShootActivity, RunActivity::class.java)
                        intent.putExtra("elapsedTime", SystemClock.elapsedRealtime() - chronoEveryTime.base)
                        startActivity(intent)
                    }
                    .create().show()

                MediaPlayer.create(this@ShootActivity, R.raw.sonnerie).apply {
                    setOnCompletionListener { release() }
                    start()
                }
            }
        }.start()
    }

    private fun fetchTarget() {
        val targets = listOf("1", "2", "3", "4", "5", "0")
        val listView = findViewById<ListView>(R.id.listCible)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, targets)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedTarget = targets[position].toInt()
            val app = application as MyApplication
            app.missedTargets += (5 - selectedTarget)
            sharedPreferences.edit().putInt("totalMissedTargets", app.missedTargets).apply()

            // Récupérer la valeur du countdownTextView et la convertir en millisecondes
            val countdownTextView = findViewById<TextView>(R.id.countdownTextView)
            val countdownTime = countdownTextView.text.toString() // "mm:ss"
            val timeParts = countdownTime.split(":")
            val minutes = timeParts[0].toInt()
            val seconds = timeParts[1].toInt()

            // Convertir en millisecondes
            val countdownInMillis = (minutes * 60 + seconds) * 1000

            // La valeur de base (50000 ms ou 5 minutes)
            val baseTime = 50000L // En millisecondes

            // Faire la soustraction
            val result = baseTime - countdownInMillis

            // Enregistrer le résultat dans chronometerForRuntime
            app.chronometerForRuntime += result

            // Log de la soustraction
            Log.d("ShootActivity", "${app.chronometerForRuntime}")



            app.roundCount++
            sharedPreferences.edit().putInt("roundCount", app.roundCount).apply()

            val chronoEveryTime = findViewById<Chronometer>(R.id.chronoEveryTime)
            val intent = Intent(this, RunActivity::class.java)
            intent.putExtra("elapsedTime", SystemClock.elapsedRealtime() - chronoEveryTime.base)
            Toast.makeText(this, "Nombre de points marqués : $selectedTarget", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
    }



    override fun onResume() {
        super.onResume()
        updateRoundTextView()
    }

    override fun onStop() {
        super.onStop()
        sharedPreferences.edit()
            .putLong("lastSessionTime", System.currentTimeMillis())
            .putInt("roundCount", (application as MyApplication).roundCount)
            .apply()
    }
}
