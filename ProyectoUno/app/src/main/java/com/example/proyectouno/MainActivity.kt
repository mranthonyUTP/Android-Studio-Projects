package com.example.proyectouno

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyectouno.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // View Binding: proporciona acceso directo a las vistas del layout
    private lateinit var binding: ActivityMainBinding

    // Constantes para identificar las unidades de peso mediante índices
    companion object {
        const val KILOGRAMOS = 0
        const val LIBRAS = 1
        const val ONZAS = 2
        const val GRAMOS = 3
    }

    // Factores de conversión a gramos (base del sistema de conversión)
    // Cada unidad se convierte primero a gramos, luego al destino
    private val toGrams = mapOf(
        KILOGRAMOS to 1000.0,    // 1 kilogramo = 1000 gramos
        LIBRAS to 453.592,        // 1 libra = 453.592 gramos
        ONZAS to 28.3495,         // 1 onza = 28.3495 gramos
        GRAMOS to 1.0             // 1 gramo = 1 gramo
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicializar View Binding - vincula el código Kotlin con el layout XML
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar el padding para evitar que el contenido quede detrás de las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar los Spinners con las unidades de peso definidas en arrays.xml
        setupSpinners()

        // Asignar el evento click al botón de conversión
        binding.btnConvert.setOnClickListener {
            performConversion()
        }
    }

    /**
     * Configura los Spinners con el array de unidades de peso definido en arrays.xml.
     * Cada Spinner muestra las 4 opciones: Kilogramos, Libras, Onzas, Gramos.
     */
    private fun setupSpinners() {
        // Obtener el array de unidades desde resources
        val units = resources.getStringArray(R.array.weight_units)

        // Adaptador para el Spinner de origen
        val adapterFrom = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            units
        )
        adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFrom.adapter = adapterFrom

        // Adaptador para el Spinner de destino
        val adapterTo = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            units
        )
        adapterTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTo.adapter = adapterTo
    }

    /**
     * Ejecuta la conversión de peso entre las unidades seleccionadas.
     * Obtiene el valor de entrada, las unidades origen/destino, y muestra el resultado formateado.
     */
    private fun performConversion() {
        // Obtener el valor numérico ingresado por el usuario
        val inputText = binding.etInputValue.text.toString()

        // Validar que se haya ingresado un valor
        if (inputText.isEmpty()) {
            binding.tvResult.text = "Por favor, ingrese un valor"
            return
        }

        val inputValue = inputText.toDoubleOrNull()

        // Validar que el valor sea numérico
        if (inputValue == null) {
            binding.tvResult.text = "Valor inválido"
            return
        }

        // Obtener las unidades seleccionadas de cada Spinner
        val fromUnit = binding.spinnerFrom.selectedItemPosition
        val toUnit = binding.spinnerTo.selectedItemPosition

        // Realizar la conversión y formatear el resultado
        val result = convertWeight(inputValue, fromUnit, toUnit)
        val formattedResult = formatResult(result, toUnit)

        // Mostrar el resultado en el TextView
        binding.tvResult.text = "Resultado: $formattedResult"
    }

    /**
     * Convierte un valor de una unidad de peso a otra.
     *
     * @param value El valor numérico a convertir.
     * @param fromUnit Índice de la unidad de origen (0=Kilogramos, 1=Libras, 2=Onzas, 3=Gramos).
     * @param toUnit Índice de la unidad de destino.
     * @return El valor convertido en la unidad de destino.
     *
     * Lógica: Se convierte primero a gramos (unidad base), luego se convierte a la unidad destino.
     * Esto permite manejar cualquier combinación de conversiones de forma uniforme.
     */
    private fun convertWeight(value: Double, fromUnit: Int, toUnit: Int): Double {
        // Paso 1: Convertir el valor de la unidad de origen a gramos
        // Se usa el factor de conversión correspondiente a la unidad de origen
        val grams = value * toGrams[fromUnit]!!

        // Paso 2: Convertir de gramos a la unidad de destino
        // Se divide por el factor de conversión de la unidad destino
        val result = grams / toGrams[toUnit]!!

        return result
    }

    /**
     * Formatea el resultado de la conversión según la unidad de destino.
     *
     * @param value El valor numérico a formatear.
     * @param unit Índice de la unidad de destino para determinar el formato.
     * @return String con el valor formateado según las especificaciones:
     *   - Kilogramos: 4 decimales (ej: 1.2500 kg)
     *   - Libras: 4 decimales (ej: 2.7520 lb)
     *   - Onzas: 3 decimales (ej: 44.092 oz)
     *   - Gramos: número entero (ej: 1250 g)
     */
    private fun formatResult(value: Double, unit: Int): String {
        // Obtener el nombre de la unidad para mostrarlo con el resultado
        val units = resources.getStringArray(R.array.weight_units)
        val unitName = units[unit]

        // Aplicar el formato correspondiente según la unidad destino
        return when (unit) {
            KILOGRAMOS -> String.format("%.4f %s", value, unitName)
            LIBRAS -> String.format("%.4f %s", value, unitName)
            ONZAS -> String.format("%.3f %s", value, unitName)
            GRAMOS -> String.format("%.0f %s", value, unitName)
            else -> "$value $unitName"
        }
    }
}