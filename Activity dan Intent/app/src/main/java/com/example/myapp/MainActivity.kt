package com.example.myapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnForm = findViewById<Button>(R.id.btnForm)
        val btnLinkedIn = findViewById<Button>(R.id.btnLinkedIn)

        // FormActivity (Explicit Intent)
        btnForm.setOnClickListener {
            val intent = Intent(this, FormActivity::class.java)
            startActivity(intent)
        }

        // Profil LinkedIn (Implicit Intent)
        btnLinkedIn.setOnClickListener {
            val linkedInUrl = "https://www.linkedin.com/"
//            val linkedInUrl = "https://www.linkedin.com/in/kenzie-carlen-b841a4253/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkedInUrl))
            startActivity(intent)
        }
    }
}