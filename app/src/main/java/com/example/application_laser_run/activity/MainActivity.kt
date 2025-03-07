package com.example.application_laser_run.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.application_laser_run.R
import com.example.application_laser_run.adapter.CategoryAdapter
import com.example.application_laser_run.webservice.ApiClient.apiService
import com.example.application_laser_run.database.AppDatabase
import com.example.application_laser_run.model.Category
import com.example.application_laser_run.model.Performance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import kotlin.collections.get

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val statPerf = findViewById<Button>(R.id.statPerf)
        statPerf.setOnClickListener {
            val intent = Intent(this, PerformanceActivity::class.java)
            startActivity(intent)
        }
        fetchCategories()
    }
    fun fetchCategories() {
        GlobalScope.launch(Dispatchers.IO) {  // Utilisation de Dispatchers.IO pour les tâches réseau
            try {
                // Appel à l'API pour récupérer les catégories
                val response = apiService.getCategories()
                if (response.isSuccessful) {
                    val categories = response.body() ?: emptyList()

                    // Utilisation de withContext pour revenir au thread principal pour l'UI
                    withContext(Dispatchers.Main) {
                        val listView: ListView = findViewById(R.id.listCategories)
                        listView.adapter = CategoryAdapter(this@MainActivity, categories)

                        // Mise en place de l'écouteur d'éléments cliqués sur le ListView
                        listView.setOnItemClickListener { parent, view, position, id ->
                            val selectedCategory = categories[position]
                            val intent = Intent(
                                this@MainActivity,
                                CategoryChooseActivity::class.java
                            ).apply {
                                putExtra("CATEGORY_ID", selectedCategory.id)
                                putExtra("CATEGORY_NAME", selectedCategory.name)
                                putExtra(
                                    "CATEGORY_DISTANCE_INITIALE",
                                    selectedCategory.initialDistance
                                )
                                putExtra("CATEGORY_DISTANCE_PARCOURU", selectedCategory.lapDistance)
                                putExtra("CATEGORY_TOUR", selectedCategory.lapCount)
                                putExtra("CATEGORY_DISTANCE_TIR", selectedCategory.shootDistance)
                            }
                            startActivity(intent)
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        // Gérer l'erreur
                        Toast.makeText(
                            this@MainActivity,
                            "Erreur lors de la récupération des catégories",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // Gérer les exceptions
                    Toast.makeText(
                        this@MainActivity,
                        "Erreur réseau : ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("MainActivity", "Erreur réseau", e)
                }
            }
        }
    }
}
