package com.example.myapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val tvNim = findViewById<TextView>(R.id.tvNimHasil)
        val tvNama = findViewById<TextView>(R.id.tvNamaHasil)
        val tvKota = findViewById<TextView>(R.id.tvKotaHasil)
        val btnKembali = findViewById<Button>(R.id.btnKembali)

        // Mengambil data dari intent yang dikirim FormActivity
        tvNim.text = "NIM : " + intent.getStringExtra("NIM_KEY")
        tvNama.text = "Nama : " + intent.getStringExtra("NAMA_KEY")
        tvKota.text = "Kota : " + intent.getStringExtra("KOTA_KEY")

        // kembali ke halaman sebelumnya
        btnKembali.setOnClickListener {
            finish()
        }
    }
}