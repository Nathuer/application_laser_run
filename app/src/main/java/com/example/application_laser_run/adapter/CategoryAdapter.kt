package com.example.application_laser_run.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.application_laser_run.R
import com.example.application_laser_run.model.Category

class CategoryAdapter(context: Context,
                      private val categories: List<Category>
) : ArrayAdapter<Category>(context, R.layout.category_item, categories) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.category_item, parent, false)
        val category = getItem(position) // Utilisation de getItem() au lieu de categories[position]

        val nameTextView = view.findViewById<TextView>(R.id.name)
        nameTextView.text = "Catégorie choisie: ${category?.name}" // Vérification de nullité

        return view
    }
}
