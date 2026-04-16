package com.example.miprimerapantalla

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val txtNombre = findViewById<EditText>(R.id.txtNombre)
        val btnSaludar = findViewById<Button>(R.id.btnSaludar)
        btnSaludar.setOnClickListener {
            val nombre = txtNombre.text.toString()
            Toast.makeText(this, "Hola $nombre",
                Toast.LENGTH_SHORT).show()
        }
    }
}
