package com.example.myapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class FormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        val etNim = findViewById<EditText>(R.id.etNim)
        val etNama = findViewById<EditText>(R.id.etNama)
        val etKota = findViewById<EditText>(R.id.etKota)
        val btnKirim = findViewById<Button>(R.id.btnKirim)

        btnKirim.setOnClickListener {
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("NIM_KEY", etNim.text.toString())
            intent.putExtra("NAMA_KEY", etNama.text.toString())
            intent.putExtra("KOTA_KEY", etKota.text.toString())
            startActivity(intent)
        }
    }
}