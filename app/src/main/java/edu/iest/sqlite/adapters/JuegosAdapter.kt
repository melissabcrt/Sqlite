package edu.iest.sqlite.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.iest.sqlite.R
import edu.iest.sqlite.db.ManejadorBaseDatos
import edu.iest.sqlite.interfaces.juegosInterface
import edu.iest.sqlite.modelos.Juego

class JuegosAdapter (var contexto: Context, var listadDejuegos: ArrayList<Juego>, var juegoInterface: juegosInterface) :
    RecyclerView.Adapter<JuegosAdapter.JuegosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JuegosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_juego, parent, false)
        return JuegosViewHolder(view)
    }

    override fun onBindViewHolder(holder: JuegosViewHolder, position: Int) {
        val juego = listadDejuegos[position]
        holder.bind(juego)
    }

    override fun getItemCount(): Int {
        return listadDejuegos.size
    }

    inner class JuegosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val etNombrePeli: TextView = itemView.findViewById(R.id.nombre)
        val tvAño: TextView = itemView.findViewById(R.id.año)
        val tvPlataforma: TextView = itemView.findViewById(R.id.plataforma)
        val img01: ImageView = itemView.findViewById(R.id.img01)
        val img02: ImageView = itemView.findViewById(R.id.img02)

        fun bind(juego: Juego) {
            etNombrePeli.text = juego.nombre
            tvAño.text = juego.precio
            tvPlataforma.text = juego.consola

            img02.setOnClickListener(){
                //eliminar
                val baseDatos = ManejadorBaseDatos(contexto)
                val argumentosWhere = arrayOf(juego.id.toString())
                baseDatos.eliminar("id = ? ", argumentosWhere)
                juegoInterface.juegoEliminado()
            }

            img01.setOnClickListener(){
                //Editar
                juegoInterface.editarJuego(juego)
            }
        }
    }
}
