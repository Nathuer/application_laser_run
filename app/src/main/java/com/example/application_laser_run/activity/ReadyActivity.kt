package com.example.application_laser_run.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.application_laser_run.R

class ReadyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ready)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Récupération de la valeur de `tour` depuis l'intent
        val tour = intent.getIntExtra("CATEGORY_TOUR", 1)

        findViewById<Button>(R.id.buttonReady).setOnClickListener {
            val i = Intent(this, RunActivity::class.java)
            i.putExtra("CATEGORY_TOUR", tour) // Ajout de la valeur `tour`
            startActivity(i)
        }
    }
}
