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
    private var roundCount: Int = 1
    private lateinit var sharedPreferences: SharedPreferences
    private var tour: Int = 1
    private var totalMissedTargets: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shoot)

        sharedPreferences = getSharedPreferences("game_prefs", MODE_PRIVATE)

        // Récupérer la valeur de tour passée par l'activité précédente
        tour = intent.getIntExtra("CATEGORY_TOUR", 1)

        // Sauvegarder la valeur de tour dans SharedPreferences
        sharedPreferences.edit().putInt("tour", tour).apply()

        // Récupérer la valeur de `tour` depuis SharedPreferences (juste au cas où on veut l'utiliser plus tard)
        val storedTour = sharedPreferences.getInt("tour", 1)
        println("Tour actuel: $storedTour")

        val lastSessionTime = sharedPreferences.getLong("lastSessionTime", 0L)
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastSessionTime > 5000) {
            roundCount = 1
            sharedPreferences.edit().putInt("roundCount", roundCount).apply()
        } else {
            roundCount = sharedPreferences.getInt("roundCount", 1)
        }

        updateRoundTextView()

        val chronoTime = intent.getLongExtra("chronoTime", 0L)
        val chronoEveryTime = findViewById<Chronometer>(R.id.chronoEveryTime)
        chronoEveryTime.base = SystemClock.elapsedRealtime() - chronoTime
        chronoEveryTime.start()

        startCountdown()
        fetchTarget()
    }

    private fun updateRoundTextView() {
        val roundTextView = findViewById<TextView>(R.id.textTour)
        roundTextView.text = "Tour n° $roundCount"


        // Comparer roundCount et tour
        if (roundCount > tour) {
            val chronoEveryTime = findViewById<Chronometer>(R.id.chronoEveryTime)
            Toast.makeText(this, "Fin du jeu. Vous avez terminé tous les tours.", Toast.LENGTH_SHORT).show()
            val i = Intent(this, StatActivity::class.java)
            val chronoFinal = SystemClock.elapsedRealtime() - chronoEveryTime.base
            i.putExtra("chronoTime", chronoFinal)
            Log.d("ShootActivity", "chronoFinal récupéré: $chronoFinal")
            startActivity(i)
        }
    }

    private fun startCountdown() {
        val countdownTextView = findViewById<TextView>(R.id.countdownTextView)

        countdownTimer = object : CountDownTimer(timeRemaining, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeRemaining = millisUntilFinished
                val seconds = (timeRemaining / 1000).toInt()
                val minutes = seconds / 60
                val remainingSeconds = seconds % 60
                countdownTextView.text = String.format("%02d:%02d", minutes, remainingSeconds)
            }

            override fun onFinish() {
                val chronoEveryTime = findViewById<Chronometer>(R.id.chronoEveryTime)
                chronoEveryTime.stop()

                val builder = AlertDialog.Builder(this@ShootActivity)
                builder.setTitle("Temps écoulé")
                    .setMessage("Le temps du tour est terminé !")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                        val intent = Intent(this@ShootActivity, RunActivity::class.java)
                        val chronoTime = SystemClock.elapsedRealtime() - chronoEveryTime.base
                        intent.putExtra("elapsedTime", chronoTime)

                        startActivity(intent)
                    }
                builder.create().show()

                val mediaPlayer = MediaPlayer.create(this@ShootActivity, R.raw.sonnerie)
                mediaPlayer.setOnCompletionListener { it.release() }
                mediaPlayer.start()
            }
        }
        countdownTimer?.start()
    }

    private fun fetchTarget() {
        val targets = listOf("1", "2", "3", "4", "5", "0") // Les cibles sont numérotées de 1 à 5 et 0 pour "manqué"
        val listView = findViewById<ListView>(R.id.listCible)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, targets)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedTarget = targets[position]

            // Calculer les cibles ratées
            val app = application as MyApplication
            val totalMissedTargets = app.missedTargets
            app.missedTargets = totalMissedTargets + (5 - selectedTarget.toInt())

            Log.d("ShootActivity", "cibles ratées : ${app.missedTargets}")

            // Sauvegarder les cibles ratées dans SharedPreferences
            sharedPreferences.edit().putInt("totalMissedTargets", app.missedTargets).apply()

            roundCount++  // Passer au tour suivant
            sharedPreferences.edit().putInt("roundCount", roundCount).apply()

            val chronoEveryTime = findViewById<Chronometer>(R.id.chronoEveryTime)
            val chronoTime = SystemClock.elapsedRealtime() - chronoEveryTime.base

            val intent = Intent(this, RunActivity::class.java)
            intent.putExtra("elapsedTime", chronoTime)
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
        val currentTime = System.currentTimeMillis()
        sharedPreferences.edit()
            .putLong("lastSessionTime", currentTime)
            .putInt("roundCount", roundCount)
            .apply()
    }
}


