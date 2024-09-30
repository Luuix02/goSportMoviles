package com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.gestionarMiEquipoFragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.common.apiRetrofit.RetrofitInstance
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.authService.ApiService
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.gestionarMiEquipoService.GestionarMiEquipoApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.User
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.Equipo
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.JugadoresAdapter
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.JugadoresSeleccionadosAdapter
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.Participante
import com.luisavillacorte.gosportapp.jugador.adapters.model.gestionarMiEquipo.GestionarMiEquipoContract
import com.luisavillacorte.gosportapp.jugador.adapters.model.gestionarMiEquipo.GestionarMiEquipoPresenter
import com.squareup.picasso.Picasso
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.IOException
import java.io.File

class FragmentGestionarMiEquipo : Fragment(), GestionarMiEquipoContract.View {

    private lateinit var equipo: Equipo
    private lateinit var jugadoresAdapter: JugadoresSeleccionadosAdapter
    private lateinit var presenter: GestionarMiEquipoContract.Presenter
    private lateinit var apiService: GestionarMiEquipoApiService
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiService = RetrofitInstance.createService(GestionarMiEquipoApiService::class.java)
        presenter = GestionarMiEquipoPresenter(this, apiService, requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gestionar_mi_equipo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        equipo = arguments?.getParcelable("equipo")
            ?: throw IllegalArgumentException("Equipo no encontrado")
        val idJugador = arguments?.getString("idJugador")
            ?: throw IllegalArgumentException("ID del jugador no encontrado")
        mostrarDatosEquipo(equipo)

        configurarCamposSegunEstadoEquipo(equipo.estado)


        view.findViewById<Button>(R.id.btnActualizarEquipo).setOnClickListener {
                subirLogoEquipoYActualizar()
            }



        view.findViewById<ImageView>(R.id.iconoCamara).setOnClickListener {
            if (!equipo.estado) {
                getImageLauncher.launch("image/*")
            } else {
                showError("No es posible cambiar el logo del equipo en este momento.")
            }
        }
    }

    private fun configurarCamposSegunEstadoEquipo(estado: Boolean) {
        if (estado) {
            view?.findViewById<EditText>(R.id.nombreEquipo)?.isEnabled = false
            view?.findViewById<TextView>(R.id.nombreCapitan)?.isEnabled = false
            view?.findViewById<EditText>(R.id.CelularSecundario)?.isEnabled = false
            view?.findViewById<TextView>(R.id.CelularPrincipal)?.isEnabled = false
            view?.findViewById<EditText>(R.id.buscadorCompañeros)?.isEnabled = false
            view?.findViewById<Button>(R.id.btnActualizarEquipo)?.isEnabled = false
            view?.findViewById<Button>(R.id.botonEliminar)?.isEnabled = false
        } else {
            view?.findViewById<EditText>(R.id.nombreEquipo)?.isEnabled = true
            view?.findViewById<TextView>(R.id.nombreCapitan)?.isEnabled = true
            view?.findViewById<EditText>(R.id.CelularSecundario)?.isEnabled = true
            view?.findViewById<TextView>(R.id.CelularPrincipal)?.isEnabled = true
            view?.findViewById<EditText>(R.id.buscadorCompañeros)?.isEnabled = true
            view?.findViewById<Button>(R.id.btnActualizarEquipo)?.isEnabled = true
            view?.findViewById<Button>(R.id.botonEliminar)?.isEnabled = true
        }
    }




    private val getImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = uri
            view?.findViewById<ImageView>(R.id.logoEquipo)?.setImageURI(selectedImageUri)
        }
    }

    private fun subirLogoEquipoYActualizar() {
        if (selectedImageUri != null) {
            val file = getFileFromUri(selectedImageUri!!)
            if (file != null) {
                showLoading(true)
                uploadImage(file) { success ->
                    showLoading(false)
                    if (success) {
                        actualizarDatosEquipo()
                    } else {
                        showError("Error al subir la imagen.")
                    }
                }
            } else {
                showError("No se pudo obtener el archivo de la imagen seleccionada.")
            }
        } else {
            actualizarDatosEquipo()
        }
    }

    private fun uploadImage(file: File, callback: (Boolean) -> Unit) {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        equipo.id?.let { equipoId ->
            equipo.idLogo?.let { publicId ->
                Log.d("ImageUpload", "Uploading image for equipoId: $equipoId, idLogo: $publicId")
                presenter.actualizarLogoEquipo(equipoId, publicId, body)
                callback(true)
            } ?: run {
                Log.e("ImageUpload", "Error: El idLogo del equipo es nulo.")
                callback(false)
            }
        } ?: run {
            Log.e("ImageUpload", "Error: El ID del equipo es nulo.")
            callback(false)
        }
    }

    private fun actualizarDatosEquipo() {
        val nuevoNombreEquipo = view?.findViewById<EditText>(R.id.nombreEquipo)?.text.toString()
        val nuevoNombreCapitan = view?.findViewById<TextView>(R.id.nombreCapitan)?.text.toString()
        val nuevoCelularPrincipal = view?.findViewById<TextView>(R.id.CelularPrincipal)?.text.toString()
        val nuevoCelularSecundario = view?.findViewById<EditText>(R.id.CelularSecundario)?.text.toString()

        val equipoActualizado = equipo.copy(
            nombreEquipo = nuevoNombreEquipo,
            nombreCapitan = nuevoNombreCapitan,
            contactoUno = nuevoCelularPrincipal,
            contactoDos = nuevoCelularSecundario
        )

        equipo.id?.let { equipoId ->
            presenter.actualizarEquipo(equipoActualizado, equipoId)
            mostrarDatosEquipo(equipoActualizado)
        } ?: run {
            showError("Error: El ID del equipo es nulo.")
        }
    }

    private fun getFileFromUri(uri: Uri): File? {
        return try {
            val inputStream = context?.contentResolver?.openInputStream(uri)
            val tempFile = File.createTempFile("temp_image", ".jpg", context?.cacheDir).apply {
                deleteOnExit()
            }
            inputStream?.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            tempFile
        } catch (e: IOException) {
            Log.e("ImageUpload", "Error al crear el archivo temporal", e)
            null
        }
    }

    override fun mostrarDatosEquipo(equipo: Equipo) {
        view?.findViewById<EditText>(R.id.nombreEquipo)?.setText(equipo.nombreEquipo)
        view?.findViewById<TextView>(R.id.nombreCapitan)?.setText(equipo.nombreCapitan)
        view?.findViewById<TextView>(R.id.CelularPrincipal)?.setText(equipo.contactoUno)
        view?.findViewById<EditText>(R.id.CelularSecundario)?.setText(equipo.contactoDos)

        view?.findViewById<RecyclerView>(R.id.recyclerJugadoresSeleccionados)?.apply {
            layoutManager = LinearLayoutManager(context)
            jugadoresAdapter = JugadoresSeleccionadosAdapter(
                jugadores = equipo.participantes.toMutableList(),
                onRemove = { jugador ->
                    Toast.makeText(
                        context,
                        "Jugador eliminado: ${jugador.nombres}",
                        Toast.LENGTH_SHORT
                    ).show()
                    val updatedList = equipo.participantes.toMutableList().apply {
                        remove(jugador)
                    }
                    jugadoresAdapter.notifyDataSetChanged()
                }
            )
            adapter = jugadoresAdapter
        }

        Picasso.get()
            .load(equipo.imgLogo)
            .into(view?.findViewById<ImageView>(R.id.logoEquipo))
    }

    override fun mostrarActualizacionExitosa(equipo: Equipo) {
        Toast.makeText(context, "Equipo actualizado exitosamente", Toast.LENGTH_SHORT).show()
        mostrarDatosEquipo(equipo)
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showValidacionExitosa(idJugador: String) {
        Toast.makeText(
            context,
            "Validación exitosa para el jugador con ID: $idJugador",
            Toast.LENGTH_SHORT
        ).show()
    }
    override fun mostrarActualizacionLogoExitosa(message: String?, url: String?) {
        Toast.makeText(context, "Logo actualizado exitosamente", Toast.LENGTH_SHORT).show()
        url?.let {
            view?.findViewById<ImageView>(R.id.logoEquipo)?.let { imageView ->
                Picasso.get().load(it).into(imageView)
            }
        }
    }

    override fun showJugadores(jugadores: List<User>) {
        jugadoresAdapter = JugadoresSeleccionadosAdapter(
            jugadores = jugadores.toMutableList(),
            onRemove = { jugador ->
                Toast.makeText(
                    context,
                    "Jugador eliminado: ${jugador.nombres}",
                    Toast.LENGTH_SHORT
                ).show()
                val updatedList = jugadores.toMutableList().apply {
                    remove(jugador)
                }
                jugadoresAdapter.notifyDataSetChanged()
            }
        )
        view?.findViewById<RecyclerView>(R.id.recyclerJugadoresSeleccionados)?.adapter =
            jugadoresAdapter
    }

    override fun showLoading(isLoading: Boolean) {
        val progressBar = view?.findViewById<ProgressBar>(R.id.progressBarl)
        progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}
