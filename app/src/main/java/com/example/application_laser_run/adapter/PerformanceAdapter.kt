package com.example.application_laser_run.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.application_laser_run.R
import com.example.application_laser_run.model.Performance
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PerformanceAdapter(private var performances: List<Performance>) :
    RecyclerView.Adapter<PerformanceAdapter.PerformanceViewHolder>() {

    class PerformanceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val runDuration: TextView = view.findViewById(R.id.runDuration)
        val shootDuration: TextView = view.findViewById(R.id.shootDuration)
        val speed: TextView = view.findViewById(R.id.speed)
        val missedTargets: TextView = view.findViewById(R.id.missedTargets)
        val startTime: TextView = view.findViewById(R.id.startTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerformanceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.performance_item, parent, false)
        return PerformanceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PerformanceViewHolder, position: Int) {
        val performance = performances[position]
        holder.runDuration.text = "Durée de course: ${performance.runDuration} ms"
        holder.shootDuration.text = "Durée de tir: ${performance.shootDuration} ms"
        holder.speed.text = "Vitesse: ${performance.speed} m/s"
        holder.missedTargets.text = "Cibles manquées: ${performance.missedTargets}"

        // Récupération de l'heure de début depuis la base de données
        val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val startTimeFormatted = dateFormat.format(Date(performance.startTime))
        holder.startTime.text = "Heure de début: $startTimeFormatted"
    }

    override fun getItemCount(): Int = performances.size

    fun updateData(newPerformances: List<Performance>) {
        val diffResult = DiffUtil.calculateDiff(PerformanceDiffCallback(performances, newPerformances))
        performances = newPerformances
        diffResult.dispatchUpdatesTo(this)
    }
}

class PerformanceDiffCallback(
    private val oldList: List<Performance>,
    private val newList: List<Performance>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
