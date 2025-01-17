package com.example.application_laser_run.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.widget.ArrayAdapter
import android.widget.Chronometer
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.application_laser_run.R

class ShootActivity : AppCompatActivity() {
    private var countdownTimer: CountDownTimer? = null
    private var timeRemaining: Long = 50000
    private var roundCount: Int = 0 // Compteur pour les tours

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_shoot)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Récupérer le temps du chronomètre passé dans l'Intent
        val chronoTime = intent.getLongExtra("chronoTime", 0L)

        // Initialiser le chronomètre
        val chronoEveryTime = findViewById<Chronometer>(R.id.chronoEveryTime)
        chronoEveryTime.base = SystemClock.elapsedRealtime() - chronoTime  // Définir la base du chrono à la valeur envoyée
        chronoEveryTime.start()  // Continuer à faire tourner le chronomètre

        startCountdown()

        // Configurer la ListView
        fetchTarget()
    }

    private fun startCountdown() {
        val countdownTextView = findViewById<TextView>(R.id.countdownTextView)

        countdownTimer = object : CountDownTimer(timeRemaining, 1000) {  // 1000ms = 1s
            override fun onTick(millisUntilFinished: Long) {
                timeRemaining = millisUntilFinished
                // Mettre à jour le TextView avec le temps restant au format MM:SS
                val seconds = (timeRemaining / 1000).toInt()
                val minutes = seconds / 60
                val remainingSeconds = seconds % 60
                countdownTextView.text = String.format("%02d:%02d", minutes, remainingSeconds)
            }

            override fun onFinish() {
                // Lorsque le compte à rebours est terminé
                countdownTextView.text = "00:00"
            }
        }

        countdownTimer?.start()
    }

    private fun fetchTarget() {
        // Liste des cibles à afficher
        val targets = listOf("Cible 1", "Cible 2", "Cible 3", "Cible 4", "Cible 5", "Cible 0")

        // Trouver la ListView
        val listView = findViewById<ListView>(R.id.listCible)

        // Créer un ArrayAdapter pour associer les données à la ListView
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, targets)
        listView.adapter = adapter

        // Gérer les clics sur les éléments de la liste
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedTarget = targets[position]

            // Incrémenter le nombre de tours
            roundCount++

            // Mettre à jour le texte dans le TextView de "Tour n°"
            val roundTextView = findViewById<TextView>(R.id.textTour)
            roundTextView.text = "Tour n° $roundCount"  // Mise à jour de l'affichage du tour

            // Récupérer le temps écoulé depuis le démarrage du chronomètre
            val chronoEveryTime = findViewById<Chronometer>(R.id.chronoEveryTime)
            val chronoTime = SystemClock.elapsedRealtime() - chronoEveryTime.base

            // Passer le temps à l'activité suivante (RunActivity)
            val intent = Intent(this, RunActivity::class.java)
            intent.putExtra("elapsedTime", chronoTime)
            Toast.makeText(this, "Nombre de point marqué : $selectedTarget", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
    }
}
