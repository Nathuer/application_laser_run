package com.example.application_laser_run.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.application_laser_run.R
import com.example.application_laser_run.model.MyApplication

class CategoryChooseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_category_choose)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val app = applicationContext as MyApplication
        app.categorie = intent.getIntExtra("CATEGORY_ID", 0)
        Log.d("CategoryChooseActivity", "Category ID: ${app.categorie}")
        app.lapDistanceInCategory = intent.getIntExtra("CATEGORY_DISTANCE_PARCOURU", 0)
        app.lapCountInCategory = intent.getIntExtra("CATEGORY_TOUR", 0)
        app.initialDistanceInCategory = intent.getIntExtra("CATEGORY_DISTANCE_INITIALE", 0)

        val name = intent.getStringExtra("CATEGORY_NAME")
        val initialDistance = intent.getIntExtra("CATEGORY_DISTANCE_INITIALE", 0)
        val lapDistance = intent.getIntExtra("CATEGORY_DISTANCE_PARCOURU", 0)
        val lapCount = intent.getIntExtra("CATEGORY_TOUR", 0)
        val shootDistance = intent.getIntExtra("CATEGORY_DISTANCE_TIR", 0)

        findViewById<TextView>(R.id.name).text = "$name"
        findViewById<TextView>(R.id.initialDistance).text = "Distance initiale : $initialDistance"
        findViewById<TextView>(R.id.lapDistance).text = "Distance parcourue : $lapDistance"
        findViewById<TextView>(R.id.lapCount).text = "Nombre de tours : $lapCount"
        findViewById<TextView>(R.id.shootDistance).text = "Distance de tir : $shootDistance"

        val ready = findViewById<Button>(R.id.ready)
        ready.setOnClickListener {
            val intent = Intent(this, RunActivity::class.java)
            intent.putExtra("NOMBRE_TOUR", lapCount)
            startActivity(intent)
        }
    }
}
