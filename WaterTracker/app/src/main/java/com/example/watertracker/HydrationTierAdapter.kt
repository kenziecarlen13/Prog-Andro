package com.example.watertracker

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HydrationTierAdapter(
    private val tiers: List<TierUser>
) : RecyclerView.Adapter<HydrationTierAdapter.TierViewHolder>() {

    class TierViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatar)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvBadge: TextView = itemView.findViewById(R.id.tvBadge)
        val tvScore: TextView = itemView.findViewById(R.id.tvScore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TierViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tier_user, parent, false)
        return TierViewHolder(view)
    }

    override fun onBindViewHolder(holder: TierViewHolder, position: Int) {
        val user = tiers[position]

        holder.tvName.text = user.name
        holder.tvScore.text = "${user.daysCompliant}/7 Hari"

        // PENTING (Logika Pengelompokan)
        val badgeText = when (user.daysCompliant) {
            7 -> {
                holder.tvBadge.setTextColor(Color.parseColor("#FFD700")) // Gold
                "Hydro Archon"
            }
            in 4..6 -> {
                holder.tvBadge.setTextColor(Color.parseColor("#42A5F5")) // Blue
                "Aqua Ascendant"
            }
            in 1..3 -> {
                holder.tvBadge.setTextColor(Color.parseColor("#66BB6A")) // Green
                "Oasis Seeker"
            }
            else -> {
                holder.tvBadge.setTextColor(Color.parseColor("#B0BEC5")) // Grey
                "Thirsty Iron"
            }
        }
        holder.tvBadge.text = badgeText

        // PENTING (Syarat URI Modul 8): Try-Catch loading URI dengan Fallback
        try {
            if (user.profileImageUri.isNullOrEmpty()) {
                throw Exception("URI is null or empty")
            }
            val uri = Uri.parse(user.profileImageUri)
            holder.ivAvatar.setImageURI(uri)

            // Check if setImageURI actually worked, if the drawable is null it means invalid uri
            if (holder.ivAvatar.drawable == null) {
                throw Exception("Invalid URI or failed to load")
            }
        } catch (e: SecurityException) {
            // Sesuai tuntutan modul
            holder.ivAvatar.setImageResource(R.drawable.ic_profile)
        } catch (e: Exception) {
            // Fallback apabila uri invalid, kosong, dll
            holder.ivAvatar.setImageResource(R.drawable.ic_profile)
        }

        // Task 2 (Modul 5): Explicit Intent to open DetailActivity on item click
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("name", user.name)
                putExtra("daysCompliant", user.daysCompliant)
                putExtra("badge", badgeText)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return tiers.size
    }
}
