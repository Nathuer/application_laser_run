package com.example.application_laser_run.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.application_laser_run.R
import com.example.application_laser_run.webservice.ApiClient.apiService
import com.example.application_laser_run.database.AppDatabase
import com.example.application_laser_run.model.Category
import com.example.application_laser_run.model.Performance
import kotlinx.coroutines.launch
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialisation de la base de données
        appDatabase = AppDatabase.getInstance(this)

        // Récupérer les catégories depuis l'API
        fetchCategories()
        loadPerformances()
    }

    private fun fetchCategories() {
        lifecycleScope.launch {
            try {
                // Appel à l'API pour récupérer les catégories
                val response: Response<List<Category>> = apiService.getCategories()

                if (response.isSuccessful) {
                    val categories = response.body() ?: emptyList()

                    // Mapper les catégories en une liste de noms pour l'affichage
                    val categoryNames = categories.map { it.name }

                    // Afficher dans le ListView
                    val listView: ListView = findViewById(R.id.listCategories)
                    val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, categoryNames)
                    listView.adapter = adapter

                    // Ajouter un listener pour gérer le clic sur les catégories
                    listView.setOnItemClickListener { _, _, position, _ ->
                        val selectedCategory = categories[position]
                        // Passer les données à une nouvelle activité
                        val intent = Intent(this@MainActivity, CategoryChooseActivity::class.java).apply {
                            putExtra("CATEGORY_NAME", selectedCategory.name)
                            putExtra("CATEGORY_DISTANCE_INITIALE", selectedCategory.initialDistance)
                            putExtra("CATEGORY_DISTANCE_PARCOURU", selectedCategory.lapDistance)
                            putExtra("CATEGORY_TOUR", selectedCategory.lapCount)
                            putExtra("CATEGORY_DISTANCE_TIR", selectedCategory.shootDistance)
                        }
                        startActivity(intent)

                        // Enregistrer la performance dans la base de données
                        savePerformance(selectedCategory)
                    }
                } else {
                    // Gérer l'erreur
                    Toast.makeText(this@MainActivity, "Erreur lors de la récupération des catégories", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // Gérer les exceptions
                Toast.makeText(this@MainActivity, "Erreur de réseau: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadPerformances() {
        lifecycleScope.launch {
            val performances = appDatabase.performanceDao().getAll()

            // Tu peux ensuite afficher les performances dans ton UI
            performances.forEach { performance ->
                println("Performance ID: ${performance.id}, Start Time: ${performance.startTime}")
            }
        }
    }


    private fun savePerformance(category: Category) {
        // Créer une performance à enregistrer
        val performance = Performance(
            runDuration = 0L, // Exemple, tu peux ajuster avec des données réelles
            shootDuration = 0L,
            shootMinDuration = 0L,
            shootAvgDuration = 0L,
            shootMaxDuration = 0L,
            missedTargets = 0,
            speed = 0,
            totalDuration = 0L,
            categoryRef = category.name,
            startTime = System.currentTimeMillis() // Heure de début actuelle
        )

        // Sauvegarde dans la base de données (utiliser une coroutine pour éviter de bloquer le thread principal)
        lifecycleScope.launch {
            appDatabase.performanceDao().insert(performance)
            Toast.makeText(this@MainActivity, "Performance enregistrée!", Toast.LENGTH_SHORT).show()
        }
    }
}
