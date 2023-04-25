package edu.iest.sqlite

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import edu.iest.sqlite.db.ManejadorBaseDatos

class AgregarActivity : AppCompatActivity() , AdapterView.OnItemSelectedListener {
    private  lateinit var fabAgregar: FloatingActionButton
    private  lateinit var etPelicula: EditText
    private  lateinit var etAño: EditText
    private  lateinit var spPlataforma: Spinner
    private val plataformas = arrayOf("Netflix", "Disney+", "Amazon Prime", "HBO", "Star+")
    private var plataformaSeleccionada: String = ""
    private  lateinit var tvPelicula: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar)
        inicializarVistas()

        val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item, plataformas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spPlataforma.adapter = adapter
        spPlataforma.onItemSelectedListener = this
        fabAgregar.setOnClickListener{
            insertarJuego( etPelicula.text.toString(),  etAño.text.toString(),plataformaSeleccionada)
        }
    }

    val columnaID = "id"
    val columnaNombreJuego = "nombre"
    val columnaPrecio = "precio"
    val columnaConsola = "consola"
    var id: Int = 0
    private fun insertarJuego(nombreJuego: String, precio: String, consola: String){
        if(!TextUtils.isEmpty(consola)) {
            val baseDatos = ManejadorBaseDatos(this)
            //  val columnas = arrayOf(columnaID, columnaNombreJuego, columnaPrecio, columnaConsola)
            val contenido = ContentValues()
            contenido.put(columnaNombreJuego, nombreJuego)
            contenido.put(columnaPrecio, precio)
            contenido.put(columnaConsola, consola)
            //guardar imagen
            id = baseDatos.insertar(contenido).toInt()
            if (id > 0) {
                Toast.makeText(this, "Pelicula: " + nombreJuego +" del "+ precio +" agregada", Toast.LENGTH_LONG).show()
                finish()
            } else
                Toast.makeText(this, "Ups no se pudo guardar la peli", Toast.LENGTH_LONG).show()
            baseDatos.cerrarConexion()
        }else{
            Snackbar.make(tvPelicula,"Favor seleccionar una plataforma", 0).show()
        }
    }

    private fun inicializarVistas(){
        etPelicula = findViewById(R.id.etPelicula)
        fabAgregar = findViewById(R.id.fabAgregar)
        etAño = findViewById(R.id.etAño)
        spPlataforma = findViewById(R.id.spPlataforma)
        tvPelicula = findViewById(R.id.tvPelicula)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
        plataformaSeleccionada = plataformas[position]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }



}