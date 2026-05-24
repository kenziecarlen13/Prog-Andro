package com.example.watertracker

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val tvDetailName  = findViewById<TextView>(R.id.tvDetailName)
        val tvDetailBadge = findViewById<TextView>(R.id.tvDetailBadge)
        val tvDetailScore = findViewById<TextView>(R.id.tvDetailScore)
        val btnBack       = findViewById<MaterialButton>(R.id.btnBack)

        // Retrieve extras passed via Explicit Intent
        val name         = intent.getStringExtra("name") ?: "Unknown"
        val daysCompliant = intent.getIntExtra("daysCompliant", 0)
        val badge        = intent.getStringExtra("badge") ?: "—"

        tvDetailName.text  = name
        tvDetailBadge.text = badge
        tvDetailScore.text = "$daysCompliant / 7 Hari"

        // Color-code badge text to match adapter
        val badgeColor = when (daysCompliant) {
            7        -> Color.parseColor("#FFD700") // Gold
            in 4..6  -> Color.parseColor("#42A5F5") // Blue
            in 1..3  -> Color.parseColor("#66BB6A") // Green
            else     -> Color.parseColor("#B0BEC5") // Grey
        }
        tvDetailBadge.setTextColor(badgeColor)

        btnBack.setOnClickListener { finish() }
    }
}
