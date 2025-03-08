package com.example.application_laser_run.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.application_laser_run.R

class TargetAdapter(context: Context, targets: List<String>) : ArrayAdapter<String>(context, 0, targets) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.target_item, parent, false)
        }

        val target = getItem(position)
        val targetTextView = view?.findViewById<TextView>(R.id.targetText)
        val targetImageView = view?.findViewById<ImageView>(R.id.targetImage)

        targetTextView?.text = target

        return view!!
    }
}
