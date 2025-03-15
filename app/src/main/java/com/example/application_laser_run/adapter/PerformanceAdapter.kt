package com.example.application_laser_run.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.application_laser_run.R
import com.example.application_laser_run.model.Performance
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.text.toLong

class PerformanceAdapter(private val context: Context, private val performances: List<Performance>) : BaseAdapter() {

    override fun getCount(): Int {
        Log.d("PerformanceAdapter", "Nombre total d'éléments: ${performances.size}")
        return performances.size
    }

    override fun getItem(position: Int): Any = performances[position]

    override fun getItemId(position: Int): Long = performances[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        Log.d("PerformanceAdapter", "getView appelé pour la position: $position")

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.performance_item, parent, false)

        val textViewCategoryName = view.findViewById<TextView>(R.id.categoryName)
        val textViewTotalTime = view.findViewById<TextView>(R.id.textViewTotalTime)
        val textViewRunTime = view.findViewById<TextView>(R.id.textViewRunTime)
        val textViewAvgSpeed = view.findViewById<TextView>(R.id.textViewAverageSpeed)
        val textViewMissedTargets = view.findViewById<TextView>(R.id.textViewMissedTargets)
        val textViewMinShootTime = view.findViewById<TextView>(R.id.textViewMinShootTime)
        val textViewMaxShootTime = view.findViewById<TextView>(R.id.textViewMaxShootTime)
        val textViewAvgShootTime = view.findViewById<TextView>(R.id.textViewAvgShootTime)

        val performance = performances[position]

        Log.d("PerformanceAdapter", "Données de l'élément $position: $performance")

        textViewCategoryName.text = performance.categoryName
        textViewTotalTime.text = "Temps Total: ${formatDuration(performance.totalDuration)} ms"
        textViewRunTime.text = "Temps Run: ${formatDuration(performance.runDuration)} ms"
        textViewAvgSpeed.text = "Vitesse de course moyenne: ${performance.avgSpeed} m/s"
        textViewMinShootTime.text = "Temps MinShootTime: ${formatDuration(performance.shootMinDuration)} ms"
        textViewMaxShootTime.text = "Temps MaxShootTime: ${formatDuration(performance.shootMaxDuration)} ms"
        textViewAvgShootTime.text = "Temps AvgShootTime: ${formatDuration(performance.shootAvgDuration)} ms"
        textViewMissedTargets.text = "Cibles Manquées: ${performance.missedTargets}"


        return view
    }

    // Fonction pour formater la durée en mm:ss
    private fun formatDuration(durationMs: Long): String {
        val minutes = (durationMs / 1000) / 60
        val seconds = (durationMs / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}
