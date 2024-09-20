package com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.crearEquipoFragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.R.id.logoEquipo
import com.luisavillacorte.gosportapp.common.apiRetrofit.RetrofitInstance
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.formCrearEquipoService.CrearEquipoApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.User
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.CrearEquipoContract
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.CrearEquipoPresenter
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.Equipo
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.JugadoresAdapter
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.JugadoresSeleccionadosAdapter
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.Participante
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class FragmentCrearEquipo : Fragment(), CrearEquipoContract.View {

    private lateinit var presenter: CrearEquipoContract.Presenter
    private val TAG = "CrearEquipoFragment"
    private var imageUri: Uri? = null
    private var userId: String? = null
    private lateinit var logoEquipo: ImageView
    private lateinit var iconoCamara: ImageView
    private lateinit var nombreEquipoEditText: EditText
    private lateinit var nombreCapitanEditText: EditText
    private lateinit var celularPrincipalEditText: EditText
    private lateinit var celularSecundarioEditText: EditText
    private lateinit var buscadorJugadores: EditText
    private lateinit var btnCrearEquipo: Button
    private lateinit var listaJugadores: RecyclerView
    private lateinit var jugadoresAdapter: JugadoresAdapter
    private lateinit var progressBar: ProgressBar

    private val jugadoresSeleccionados = mutableListOf<User>()
    private lateinit var jugadoresSeleccionadosAdapter: JugadoresSeleccionadosAdapter
    private lateinit var recyclerJugadoresSeleccionados: RecyclerView

    private var cedulaUsuario: String? = null
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
        buscadorJugadores = view.findViewById(R.id.buscadorCompañeros)
        listaJugadores = view.findViewById(R.id.recyclerJugadores)
        btnCrearEquipo = view.findViewById(R.id.btnCrearEquipo)
        progressBar = view.findViewById(R.id.progressBar)
        logoEquipo = view.findViewById(R.id.logoEquipo)
        iconoCamara = view.findViewById(R.id.iconoCamara)

        logoEquipo.setOnClickListener {
            openGallery()
        }
        iconoCamara.setOnClickListener {
            openGallery()
        }
        recyclerJugadoresSeleccionados = view.findViewById(R.id.recyclerJugadoresSeleccionados)

        recyclerJugadoresSeleccionados.layoutManager = LinearLayoutManager(requireContext())
        jugadoresSeleccionadosAdapter =
            JugadoresSeleccionadosAdapter(jugadoresSeleccionados) { jugador ->
                eliminarJugadorSeleccionado(jugador)
            }
        recyclerJugadoresSeleccionados.adapter = jugadoresSeleccionadosAdapter

        Log.d(TAG, "Inicializando vistas y presenter")
        presenter = CrearEquipoPresenter(
            this, requireContext(),
            RetrofitInstance.createService(CrearEquipoApiService::class.java)
        )

        presenter.getPerfilUsuario()

        Log.d(TAG, "Configurando RecyclerView y TextWatcher")

        listaJugadores.layoutManager = LinearLayoutManager(requireContext())

        val jugadoresEnEquipoList: Set<String> = mutableSetOf()
        val jugadoresSeleccionadosList: List<User> = mutableListOf()

        jugadoresAdapter = JugadoresAdapter(
            jugadoresEnEquipo = jugadoresEnEquipoList,
            jugadoresSeleccionados = jugadoresSeleccionadosList,
            onJugadorSelected = { usuario ->
                Log.d("CrearEquipoPresenter", "Jugador seleccionado: $usuario")
                Log.d("CrearEquipoPresenter", "Número de dorsal del jugador: ${usuario.dorsal}")
                if (jugadoresSeleccionados.contains(usuario)) {
                    eliminarJugadorSeleccionado(usuario)
                } else {
                    if (isDorsalValid(usuario.dorsal) && !isJugadorEnEquipo(usuario)) {
                        presenter.validarInscripcion(usuario.id)
                        //
//                                            agregarJugadorSeleccionado(usuario)
                    } else {
                        showError("El jugador ya está en el equipo o el dorsal no es válido.")
                    }
//                    agregarJugadorSeleccionado(usuario)
                }
            },
            onHideSuggestions = {
                listaJugadores.visibility = View.GONE
            }
        )

        listaJugadores.adapter = jugadoresAdapter

        buscadorJugadores.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                if (query.isNotEmpty()) {
                    presenter.buscarJugadores(query)
                    listaJugadores.visibility = View.VISIBLE
                } else {
                    // Limpiar la lista si no hay consulta
                    jugadoresAdapter.submitList(emptyList())
                    listaJugadores.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

//        logoEquipo.setOnClickListener {
//            imageResultLauncher.launch("image/*")
//        }


        btnCrearEquipo.setOnClickListener {
            Log.d(TAG, "Botón Crear Equipo clicado")
            if (validateFields()) {
                if (imageUri != null) {
                    if (userId != null) {
                        presenter.getPerfilUsuario()
                        val equipo = Equipo(
                            nombreEquipo = nombreEquipoEditText.text.toString(),
                            nombreCapitan = nombreCapitanEditText.text.toString(),
                            contactoUno = celularPrincipalEditText.text.toString(),
                            contactoDos = celularSecundarioEditText.text.toString(),
                            jornada = "Tarde",
                            cedula = cedulaUsuario!!,
                            imgLogo = "",
                            idLogo = "",
                            estado = true,
                            puntos = 0,
//                            id = "",
                            participantes = getSelectedPlayers()
                        )

                        Log.d(TAG, "Datos del equipo: $equipo")
                        presenter.subirLogoEquipo(userId!!, imageUri!!, equipo)
//                    presenter.crearEquipo(equipo)
                    } else {
                        Log.e(TAG, "Validación fallida. Campos incompletos")
                        showError("Por favor, complete todos los campos.")
                    }
                } else {
                    showError("Por favor, seleccione una imagen para el logo del equipo.")
                }
            }else {
                    showError("Se requiere al menos 4 jugadores seleccionados y completar todos los campos.")
                }
            }


        return view
    }
    private fun isDorsalValid(dorsal: String?): Boolean{
        return dorsal.isNullOrEmpty() || jugadoresSeleccionados.none {it.dorsal == dorsal}
    }


    private fun agregarJugadorSeleccionado(jugador: User) {
        if (jugadoresSeleccionados.none { it.id == jugador.id }) {
            jugadoresSeleccionados.add(jugador)
            jugadoresSeleccionadosAdapter.notifyItemInserted(jugadoresSeleccionados.size - 1)
        }
    }

    private fun isJugadorEnEquipo(jugador: User): Boolean {
        return jugadoresSeleccionados.any { it.id == jugador.id }
    }

    private fun eliminarJugadorSeleccionado(jugador: User) {
        val index =
            jugadoresSeleccionados.indexOfFirst { it.identificacion == jugador.identificacion }
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

        Log.d(TAG, "Nombre Equipo: $nombreEquipo")
        Log.d(TAG, "Nombre Capitán: $nombreCapitan")
        Log.d(TAG, "Contacto Uno: $contactoUno")
        Log.d(TAG, "Contacto Dos: $contactoDos")
        Log.d(TAG, "Jugadores Seleccionados: ${jugadoresSeleccionados.size}")

        val isValid =
            nombreEquipo.isNotEmpty() &&
                    nombreCapitan.isNotEmpty() &&
                    contactoUno.isNotEmpty() &&
                    contactoDos.isNotEmpty() &&
                    jugadoresSeleccionados.size >= 4

        Log.d(TAG, "Validación de campos: $isValid")
        return isValid
    }



    override fun getSelectedPlayers(): List<User> {
        return jugadoresSeleccionados
    }

    override fun showPerfilUsuario(perfil: PerfilUsuarioResponse) {
        Log.d(TAG, "Perfil de usuario mostrado: $perfil")
        nombreCapitanEditText.setText(perfil.nombres)
        celularPrincipalEditText.setText(perfil.telefono)
        userId = perfil.id
        cedulaUsuario = perfil.identificacion

        val capitan = User(
            id = perfil.id,
            nombres = perfil.nombres,
            telefono = perfil.telefono,
            correo = perfil.correo,
            identificacion = perfil.identificacion,
            ficha = perfil.ficha,
            dorsal = "",
            rol = perfil.rol

        )
        agregarJugadorSeleccionado(capitan)
    }

    override fun showJugadores(jugadores: List<User>) {
        Log.d(TAG, "Jugadores mostrados: $jugadores")
        jugadoresAdapter.submitList(jugadores)
    }

    override fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun showSuccess(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

    }
    override fun showValidacionExitosa(idJugador: String) {
        // Obtén el jugador usando el ID
        val jugador = jugadoresAdapter.currentList.find { it.id == idJugador }
        jugador?.let {
            agregarJugadorSeleccionado(it)
        }
    }

    override fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

    }

    override fun onImageUploadSuccess(imageUrl: String?, imagePublicId: String?) {
        // Actualiza la imagen del equipo después de la carga
        val equipo = Equipo(
            nombreEquipo = nombreEquipoEditText.text.toString(),
            nombreCapitan = nombreCapitanEditText.text.toString(),
            contactoUno = celularPrincipalEditText.text.toString(),
            contactoDos = celularSecundarioEditText.text.toString(),
            jornada = "Tarde",
            cedula = cedulaUsuario!!,
            imgLogo = imageUrl ?: "",
            idLogo = imagePublicId ?: "",
            estado = true,
            puntos = 0,
//            id = "",
            participantes = getSelectedPlayers()
        )
        presenter.crearEquipo(equipo)
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
        }
        imageResultLauncher.launch("image/*")
    }

    private val imageResultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = uri
            logoEquipo.setImageURI(imageUri)

        }

    }

}
