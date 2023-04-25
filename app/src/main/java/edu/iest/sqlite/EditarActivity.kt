package edu.iest.sqlite

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.google.android.material.snackbar.Snackbar
import edu.iest.sqlite.db.ManejadorBaseDatos
import edu.iest.sqlite.modelos.Juego

class EditarActivity : AppCompatActivity() , AdapterView.OnItemSelectedListener {
    private lateinit var bnGuardar: Button
    private  lateinit var etPelicula: EditText
    private  lateinit var etAno: EditText
    private  lateinit var spPlataforma: Spinner
    private val plataformas = arrayOf("Netflix", "Disney+", "Amazon Prime", "HBO", "Star+")
    private var plataformaSeleccionada: String = ""
    private lateinit var tvPelicula: TextView
    var juego: Juego? = null
    var id: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar)
        //  setSupportActionBar(toolbar)
        getSupportActionBar()?.title = "Edición"
        getSupportActionBar()?.setHomeButtonEnabled(true);
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        inicializarVistas()
        id = intent.getIntExtra("id", 0)
        buscarJuego(id)
        poblarCampos()
    }

    private fun poblarCampos() {
        etPelicula.setText(juego?.nombre)
        etAno.setText(juego?.precio)
        val position = plataformas.indexOf(juego?.consola)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, plataformas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spPlataforma.adapter = adapter
        spPlataforma.onItemSelectedListener = this
        if (position >= 0) {
            spPlataforma.setSelection(position)
            plataformaSeleccionada = plataformas[position]
        }
    }

    private fun inicializarVistas() {
        etPelicula = findViewById(R.id.etPelicula)
        bnGuardar = findViewById(R.id.bnGuardar)
        etAno = findViewById(R.id.etAño)
        spPlataforma = findViewById(R.id.spPlataforma)
        tvPelicula = findViewById(R.id.tvPelicula)
        bnGuardar.setOnClickListener {
            var precio_actual: String
            precio_actual = etAno.text.toString()
            actualizarJuego(etPelicula.text.toString(), precio_actual, plataformaSeleccionada)
        }
    }

    val columnaNombreJuego = "nombre"
    val columnaPrecio = "precio"
    val columnaConsola = "consola"

    private fun actualizarJuego(nombreJuego: String, precio: String, consola: String) {
        if (!TextUtils.isEmpty(consola)) {
            val baseDatos = ManejadorBaseDatos(this)
            val contenido = ContentValues()
            contenido.put("nombre", nombreJuego)
            contenido.put("precio", precio)
            contenido.put("consola", consola)
            if ( id > 0) {
                val argumentosWhere = arrayOf(id.toString())
                val id_actualizado = baseDatos.actualizar(contenido, "id = ?", argumentosWhere)
                if (id_actualizado > 0) {
                    Snackbar.make(etPelicula, "Pelicula actualizada", Snackbar.LENGTH_LONG).show()
                } else {
                    val alerta = AlertDialog.Builder(this)
                    alerta.setTitle("Atención")
                        .setMessage("No fue posible actualizarla")
                        .setCancelable(false)
                        .setPositiveButton("Aceptar") { dialog, which ->

                        }
                        .show()
                }
            } else {
                Toast.makeText(this, "no hiciste id", Toast.LENGTH_LONG).show()
            }
            baseDatos.cerrarConexion()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("Range")
    private fun buscarJuego(idJuego: Int) {

        if (idJuego > 0) {
            val baseDatos = ManejadorBaseDatos(this)
            val columnasATraer = arrayOf("id", "nombre", "precio", "consola")
            val condicion = " id = ?"
            val argumentos = arrayOf(idJuego.toString())
            val ordenarPor = "id"
            val cursor = baseDatos.seleccionar(columnasATraer, condicion, argumentos, ordenarPor)

            if (cursor.moveToFirst()) {
                do {
                    val juego_id = cursor.getInt(cursor.getColumnIndex("id"))
                    val nombre = cursor.getString(cursor.getColumnIndex("nombre"))
                    val precio = cursor.getString(cursor.getColumnIndex("precio"))
                    val consola = cursor.getString(cursor.getColumnIndex("consola"))
                    juego = Juego(juego_id, nombre, precio, consola)
                } while (cursor.moveToNext())
            }
            baseDatos.cerrarConexion()
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        plataformaSeleccionada = plataformas[position]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}