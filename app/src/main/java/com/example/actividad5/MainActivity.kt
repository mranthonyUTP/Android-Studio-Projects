package com.example.actividad5

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.materialswitch.MaterialSwitch
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var etFullName: EditText
    private lateinit var rgFood: RadioGroup
    private lateinit var ivFoodImage: ImageView
    private lateinit var spHobby: AutoCompleteTextView
    private lateinit var swSports: MaterialSwitch
    private lateinit var btnDate: Button
    private lateinit var tvDob: TextView
    private lateinit var btnToast: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Binding views
        etFullName = findViewById(R.id.etFullName)
        rgFood = findViewById(R.id.rgFood)
        ivFoodImage = findViewById(R.id.ivFoodImage)
        spHobby = findViewById(R.id.spHobby)
        swSports = findViewById(R.id.swSports)
        btnDate = findViewById(R.id.btnDate)
        tvDob = findViewById(R.id.tvDob)
        btnToast = findViewById(R.id.btnToast)

        setupHobbySpinner()
        setupFoodSelection()
        setupDatePicker()
        setupToastButton()

        // Load initial image
        loadFoodImage(R.drawable.hijacked)
    }

    private fun loadFoodImage(resourceId: Int) {
        Glide.with(this)
            .load(resourceId)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_report_image)
            .into(ivFoodImage)
    }

    private fun setupHobbySpinner() {
        val hobbies = resources.getStringArray(R.array.hobbies_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, hobbies)
        spHobby.setAdapter(adapter)
    }

    private fun setupFoodSelection() {
        rgFood.setOnCheckedChangeListener { _, checkedId ->
            val imageRes = when (checkedId) {
                R.id.rbItalian -> R.drawable.hijacked
                R.id.rbChinese -> R.drawable.slums
                R.id.rbPanamanian -> R.drawable.standoff
                else -> android.R.drawable.ic_menu_gallery
            }
            loadFoodImage(imageRes)
        }
    }

    private fun setupDatePicker() {
        btnDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    tvDob.text = date
                },
                year, month, day
            )
            datePickerDialog.show()
        }
    }

    private fun setupToastButton() {
        btnToast.setOnClickListener {
            Toast.makeText(this@MainActivity, R.string.toast_message, Toast.LENGTH_SHORT).show()
        }
    }

    // Custom method for XML onClick="showSummary"
    fun showSummary(@Suppress("UNUSED_PARAMETER") view: View) {
        val name = etFullName.text.toString().trim().ifEmpty { "No proporcionado" }

        val selectedFoodId = rgFood.checkedRadioButtonId
        val food = if (selectedFoodId != -1) {
            findViewById<RadioButton>(selectedFoodId).text.toString()
        } else {
            "No seleccionada"
        }

        val hobby = spHobby.text.toString()
        val sports = if (swSports.isChecked) "Sí" else "No"
        val dob = tvDob.text.toString()

        val summary = getString(
            R.string.summary_format,
            name, food, hobby, sports, dob
        )

        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_title)
            .setMessage(summary)
            .setPositiveButton("Cerrar", null)
            .show()
    }
}
