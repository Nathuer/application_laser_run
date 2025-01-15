package com.example.application_laser_run

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.application_laser_run.ApiClient.apiService
import kotlinx.coroutines.launch
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Récupérer les catégories depuis l'API
        fetchCategories()
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
}
