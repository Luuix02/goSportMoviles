package com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.crearEquipoFragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.common.apiRetrofit.RetrofitInstance
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.formCrearEquipoService.CrearEquipoApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.User
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.CrearEquipoContract
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.CrearEquipoPresenter
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.Equipo
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.JugadoresAdapter
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.JugadoresSeleccionadosAdapter


class FragmentCrearEquipo : Fragment(), CrearEquipoContract.View {


    private lateinit var presenter: CrearEquipoContract.Presenter
    private val TAG = "CrearEquipoFragment"

    private lateinit var nombreEquipoEditText: EditText
    private lateinit var nombreCapitanEditText: EditText
    private lateinit var celularPrincipalEditText: EditText
    private lateinit var celularSecundarioEditText: EditText
    private lateinit var logoEquipoImageView: ImageView
    private lateinit var buscadorJugadores: EditText
    private lateinit var btnCrearEquipo: Button
    private lateinit var listaJugadores: RecyclerView
    private lateinit var jugadoresAdapter: JugadoresAdapter
    private lateinit var progressBar: ProgressBar

    //    private val jugadores = mutableListOf<Participante>()
    private val jugadoresSeleccionados = mutableListOf<User>()
    private lateinit var jugadoresSeleccionadosAdapter: JugadoresSeleccionadosAdapter
    private lateinit var recyclerJugadoresSeleccionados: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_crear_equipo, container, false)

        // Inicializar las vistas


        nombreEquipoEditText = view.findViewById(R.id.nombreEquipo)
        nombreCapitanEditText = view.findViewById(R.id.nombreCapitan)
        celularPrincipalEditText = view.findViewById(R.id.CelularPrincipal)
        celularSecundarioEditText = view.findViewById(R.id.CelularSecundario)
        logoEquipoImageView = view.findViewById(R.id.logoEquipo)
        buscadorJugadores = view.findViewById(R.id.buscadorCompañeros)
        listaJugadores = view.findViewById(R.id.recyclerJugadores)
        btnCrearEquipo = view.findViewById(R.id.btnCrearEquipo)
        progressBar = view.findViewById(R.id.progressBar)
        recyclerJugadoresSeleccionados = view.findViewById(R.id.recyclerJugadoresSeleccionados)

        recyclerJugadoresSeleccionados.layoutManager = LinearLayoutManager(requireContext())
        jugadoresSeleccionadosAdapter =
            JugadoresSeleccionadosAdapter(jugadoresSeleccionados) { jugador ->
                eliminarJugadorSeleccionado(jugador)
            }
        recyclerJugadoresSeleccionados.adapter = jugadoresSeleccionadosAdapter


        Log.d(TAG, "Inicializando vistas y presenter")
        presenter =
            CrearEquipoPresenter(
                this, requireContext(),
                RetrofitInstance.createService(CrearEquipoApiService::class.java)
            )

        presenter.getPerfilUsuario()

        Log.d(TAG, "Configurando RecyclerView y TextWatcher")


        listaJugadores.layoutManager = LinearLayoutManager(requireContext())

        jugadoresAdapter = JugadoresAdapter { usuario: User ->
            if (jugadoresSeleccionados.contains(usuario)) {
                eliminarJugadorSeleccionado(usuario)
//                jugadoresSeleccionados.remove(usuario)
            } else {
                agregarJugadorSeleccionado(usuario)
            }
            jugadoresAdapter.submitList(jugadoresAdapter.currentList.toMutableList())
        }

        listaJugadores.adapter = jugadoresAdapter


        buscadorJugadores.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                if (query.isNotEmpty()) {
                    presenter.buscarJugadores(query)
                } else {
                    // Limpiar la lista si no hay consulta
                    jugadoresAdapter.submitList(emptyList())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


        btnCrearEquipo.setOnClickListener {
            Log.d(TAG, "Botón Crear Equipo clicado")
            if (validateFields()) {
                val equipo = Equipo(
                    nombreEquipo = nombreEquipoEditText.text.toString(),
                    nombreCapitan = nombreCapitanEditText.text.toString(),
                    contactoUno = celularPrincipalEditText.text.toString(),
                    contactoDos = celularSecundarioEditText.text.toString(),
                    jornada = "Tarde",
                    cedula = "10203040",
                    imgLogo = "logo.jpg",
                    idLogo = "id_logo",
                    estado = true,
                    participantes = getSelectedPlayers(),
                )
                Log.d(TAG, "Datos del equipo: $equipo")
                presenter.crearEquipo(equipo)
            } else {
                Log.e(TAG, "Validación fallida. Campos incompletos")
                showError("Por favor, complete todos los campos.")
            }

        }

        return view
    }

    private fun agregarJugadorSeleccionado(jugador: User) {
        jugadoresSeleccionados.add(jugador)
        jugadoresSeleccionadosAdapter.notifyItemInserted(jugadoresSeleccionados.size - 1)
    }

    private fun eliminarJugadorSeleccionado(jugador: User) {
        val index = jugadoresSeleccionados.indexOf(jugador)
        if (index != -1) {
            jugadoresSeleccionados.removeAt(index)
            jugadoresSeleccionadosAdapter.notifyItemRemoved(index)
        }
    }

    override fun validateFields(): Boolean {
        val nombreEquipo = nombreEquipoEditText.text.toString()
        val nombreCapitan = nombreCapitanEditText.text.toString()
        val contactoUno = celularPrincipalEditText.text.toString()
        val contactoDos = celularSecundarioEditText.text.toString()
        val jugadoresSeleccionados = getSelectedPlayers()

        return nombreEquipo.isNotEmpty() &&
                nombreCapitan.isNotEmpty() &&
                contactoUno.isNotEmpty() &&
                contactoDos.isNotEmpty() &&
                jugadoresSeleccionados.size >= 8
    }

    override fun getSelectedPlayers(): List<User> {
        return jugadoresSeleccionados
    }

    override fun showPerfilUsuario(perfil: PerfilUsuarioResponse) {
        // Mostrar el perfil del usuario en la UI
        Log.d(TAG, "Perfil de usuario mostrado: $perfil")
        nombreCapitanEditText.setText(perfil.nombres) // Asigna el nombre del capitán
        celularPrincipalEditText.setText(perfil.telefono) // Asigna el contacto principal
    }

    override fun showJugadores(jugadores: List<User>) {
        Log.d(TAG, "Jugadores mostrados: $jugadores")
//        jugadores.forEach { Log.d(TAG, "Jugador: $it") }
        jugadoresAdapter.submitList(jugadores)
    }

    override fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun showSuccess(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}


//        private fun convertirUsuariosAParticipantes(users: List<User>): List<User> {
//            return users
//
//        }
