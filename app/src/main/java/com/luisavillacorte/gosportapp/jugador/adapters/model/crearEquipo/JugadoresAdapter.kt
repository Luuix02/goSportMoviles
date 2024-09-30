package com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.User

class JugadoresAdapter(
    private val onJugadorSelected: (User) -> Unit,
    private val onHideSuggestions: () -> Unit,
    private val jugadoresEnEquipo: Set<String>,
    private val jugadoresSeleccionados: List<User>,

    ) : ListAdapter<User, JugadoresAdapter.JugadorViewHolder>(JugadorDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JugadorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_jugador, parent, false)
        return JugadorViewHolder(view)
    }

    override fun onBindViewHolder(holder: JugadorViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    inner class JugadorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreTextView: TextView = itemView.findViewById(R.id.nombreJugador)
        private val fichaTextView: TextView = itemView.findViewById(R.id.fichaJugador)
        private val dorsalEditText: EditText = itemView.findViewById(R.id.dorsalJugador)
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkboxSeleccionarJugador)

        fun bind(user: User) {
            Log.d("JugadorViewHolder", "Binding user: $user")
            Log.d("JugadorViewHolder", "Nombre: ${user.nombres}, Ficha: ${user.ficha}, Dorsal: ${user.dorsal}, Seleccionado: ${user.isSelected}")
            nombreTextView.text = user.nombres
            fichaTextView.text = user.ficha
            dorsalEditText.setText(user.dorsal)
            checkBox.isChecked = user.isSelected

            Log.d("JugadorViewHolder", "Nombre del jugador: ${user.nombres}")
            Log.d("JugadorViewHolder", "Ficha del jugador: ${user.ficha}")
            Log.d("JugadorViewHolder", "Dorsal del jugador: ${user.dorsal}")
            Log.d("JugadorViewHolder", "Seleccionado: ${user.isSelected}")

            //nuevaLinea
            val isInTeam = jugadoresEnEquipo.contains(user.identificacion)
            Log.d("JugadorViewHolder", "Jugador en equipo: $isInTeam")

            dorsalEditText.isEnabled = !user.isSelected
            checkBox.isEnabled = !user.isSelected && dorsalEditText.text.isNotEmpty() && !isInTeam
            Log.d("JugadorViewHolder", "Dorsal EditText habilitado: ${dorsalEditText.isEnabled}, CheckBox habilitado: ${checkBox.isEnabled}")


            dorsalEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    user.dorsal = s.toString()
                    checkBox.isEnabled = s.isNullOrEmpty().not() && !isInTeam
                    Log.d("JugadorViewHolder", "Dorsal después de cambio: ${user.dorsal}, CheckBox habilitado: ${checkBox.isEnabled}")
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                user.isSelected = isChecked
                Log.d("JugadorViewHolder", "CheckBox cambiado: $isChecked para ${user.nombres}")
                if (isChecked) {
                    onJugadorSelected(user)
                    onHideSuggestions()
                } else {
                    onJugadorSelected(user) // Necesario para desmarcar el jugador
                }
            }

            itemView.setOnClickListener {
                if (!user.isSelected && !user.dorsal.isNullOrEmpty() && !isInTeam) {
                    checkBox.isChecked = true
                    Log.d("JugadorViewHolder", "Item clickeado: ${user.nombres}, CheckBox marcado")
                }
            }
        }
    }

    class JugadorDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.identificacion == newItem.identificacion
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}