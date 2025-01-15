package com.example.application_laser_run

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CategoryChooseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_choose)

        // Récupération des données envoyées avec l'Intent
        val categoryName = intent.getStringExtra("CATEGORY_NAME")
        val distanceInitiale = intent.getIntExtra("CATEGORY_DISTANCE_INITIALE", 0)
        val distanceParcouru = intent.getIntExtra("CATEGORY_DISTANCE_PARCOURU", 0)
        val tour = intent.getIntExtra("CATEGORY_TOUR", 0)
        val distanceTir = intent.getIntExtra("CATEGORY_DISTANCE_TIR", 0)

        // Associer les TextViews aux variables correspondantes
        val categoryNameText: TextView = findViewById(R.id.categoryName)
        val distanceInitialeText: TextView = findViewById(R.id.distanceInitiale)
        val distanceParcouruText: TextView = findViewById(R.id.distanceParcouru)
        val tourText: TextView = findViewById(R.id.tour)
        val distanceTirText: TextView = findViewById(R.id.distanceTir)

        // Affichage des données dans les TextViews
        categoryNameText.text = categoryName ?: "Inconnu"
        distanceInitialeText.text = "$distanceInitiale"
        distanceParcouruText.text = "$distanceParcouru"
        tourText.text = "$tour"
        distanceTirText.text = "$distanceTir"
    }
}
