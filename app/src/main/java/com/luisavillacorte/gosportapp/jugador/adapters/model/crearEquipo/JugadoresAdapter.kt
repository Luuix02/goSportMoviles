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

    //    private val jugadores: List<Participante>,
    private val onJugadorSelected: (User) -> Unit
) : ListAdapter<User, JugadoresAdapter.JugadorViewHolder>(JugadorDiffCallback()) {


        private val TAG = "JugadoresAdapter"
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JugadorViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_jugador, parent, false)
            return JugadorViewHolder(view)
        }

        override fun onBindViewHolder(holder: JugadorViewHolder, position: Int) {
            val user = getItem(position)
            holder.bind(user)
//        holder.itemView.setOnClickListener {
//            Log.d(TAG, "Jugador clicado: $jugador")
//            onJugadorSelected(jugador) }
        }

//    override fun getItemCount(): Int = jugadores.size

        inner class JugadorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val nombreTextView: TextView = itemView.findViewById(R.id.nombreJugador)
            private val fichaTextView: TextView = itemView.findViewById(R.id.fichaJugador)
            private val dorsalEditText: EditText = itemView.findViewById(R.id.dorsalJugador)
            //        private val eliminarJugador: Button = itemView.findViewById(R.id.eliminarJugador)
            private val checkBox: CheckBox = itemView.findViewById(R.id.checkboxSeleccionarJugador)

            fun bind(user: User) {
                nombreTextView.text = user.nombres
                checkBox.isEnabled = false

                fichaTextView.text = user.ficha
                dorsalEditText.setText(user.dorsal)
                checkBox.isChecked = user.isSelected

                Log.d("ViewHolder", "Datos del jugador: $user")

                dorsalEditText.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        checkBox.isEnabled = !s.isNullOrEmpty()
//                    user.dorsal = s.toString()
                    }
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                })

                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked){
                        onJugadorSelected(user) // Notify selection change
                    }
//                user.isSelected = isChecked

                }

//            eliminarJugador.setOnClickListener {
//                /* val index = jugadores.indexOf(jugador)
//                if (index != -1) {
//                    jugadores.removeAt(index)
//                    notifyItemRemoved(index)*/
//            }
                itemView.setOnClickListener { onJugadorSelected(user) }
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
