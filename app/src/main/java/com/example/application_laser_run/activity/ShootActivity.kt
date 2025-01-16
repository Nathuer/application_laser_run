package com.example.application_laser_run.activity

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.ArrayAdapter
import android.widget.Chronometer
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.application_laser_run.R

class ShootActivity : AppCompatActivity() {
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
        val chronoTime = intent.getLongExtra("chronoTime", 0)

        // Initialiser le chronomètre
        val chronoEveryTime = findViewById<Chronometer>(R.id.chronoEveryTime)
        chronoEveryTime.base = SystemClock.elapsedRealtime() - chronoTime  // Définir la base du chrono à la valeur envoyée
        chronoEveryTime.start()  // Continuer à faire tourner le chronomètre

        // Configurer la ListView
        fetchTarget()
    }

    private fun fetchTarget() {
        // Liste des cibles à afficher
        val targets = listOf("Cible 1", "Cible 2", "Cible 3", "Cible 4", "Cible 5")

        // Trouver la ListView
        val listView = findViewById<ListView>(R.id.listCible)

        // Créer un ArrayAdapter pour associer les données à la ListView
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, targets)
        listView.adapter = adapter

        // Gérer les clics sur les éléments de la liste
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedTarget = targets[position]
            // Rediriger vers une autre activité
            val intent = Intent(this, RunActivity::class.java)
            Toast.makeText(this, "Nombre de point marqué : $selectedTarget", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
        }
    }

