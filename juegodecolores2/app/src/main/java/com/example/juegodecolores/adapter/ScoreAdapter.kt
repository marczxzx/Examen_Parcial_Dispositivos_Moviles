package com.example.juegodecolores.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.juegodecolores.R
import com.example.juegodecolores.data.models.Score
import java.text.SimpleDateFormat
import java.util.*

class ScoreAdapter : ListAdapter<Score, ScoreAdapter.ScoreViewHolder>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Score>() {
            override fun areItemsTheSame(oldItem: Score, newItem: Score) = oldItem.timestamp == newItem.timestamp
            override fun areContentsTheSame(oldItem: Score, newItem: Score) = oldItem == newItem
        }
    }

    inner class ScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPoints: TextView = itemView.findViewById(R.id.tv_points)
        val tvTime: TextView = itemView.findViewById(R.id.tv_time_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_score, parent, false)
        return ScoreViewHolder(v)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val item = getItem(position)
        holder.tvPoints.text = item.points.toString()
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        holder.tvTime.text = sdf.format(Date(item.timestamp))
    }
}
