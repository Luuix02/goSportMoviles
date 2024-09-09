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
        equipo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("equipo", Equipo::class.java)
        } else {
            arguments?.getParcelable("equipo")
        } ?: throw IllegalArgumentException("Equipo no encontrado")

        val idJugador = arguments?.getString("idJugador") ?: throw IllegalArgumentException("ID del jugador no encontrado")
        mostrarDatosEquipo(equipo)

        view.findViewById<Button>(R.id.btnActualizarEquipo).setOnClickListener {
            val nuevoNombreEquipo = view.findViewById<EditText>(R.id.nombreEquipo).text.toString()
            val nuevoNombreCapitan = view.findViewById<TextView>(R.id.nombreCapitan).text.toString()
            val nuevoCelularPrincipal = view.findViewById<TextView>(R.id.CelularPrincipal).text.toString()
            val nuevoCelularSecundario = view.findViewById<EditText>(R.id.CelularSecundario).text.toString()

            val equipoActualizado = equipo.copy(
                nombreEquipo = nuevoNombreEquipo,
                nombreCapitan = nuevoNombreCapitan,
                contactoUno = nuevoCelularPrincipal,
                contactoDos = nuevoCelularSecundario
            )

            presenter.actualizarEquipo(equipoActualizado, equipo.id)
            subirLogoEquipo()
        }

        view.findViewById<ImageView>(R.id.iconoCamara).setOnClickListener {
            getImageLauncher.launch("image/*")
        }
    }

    private val getImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = uri
            view?.findViewById<ImageView>(R.id.logoEquipo)?.setImageURI(selectedImageUri)

            val file = getFileFromUri(uri)
            if (file != null && file.exists()) {
                uploadImage(file)
            } else {
                Log.e("ImageUpload", "El archivo no se encuentra en la ruta proporcionada")
            }
        }
    }

    private fun getFileFromUri(uri: Uri): File? {
        val inputStream = context?.contentResolver?.openInputStream(uri)
        return try {
            val file = File.createTempFile(
                "temp",
                ".jpg",
                context?.cacheDir ?: throw IOException("Cache directory not available")
            )
            inputStream?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            file
        } catch (e: IOException) {
            Log.e("ImageUpload", "Error al crear el archivo temporal", e)
            null
        }

//    private fun getRealPathFromUri(uri: Uri): String? {
//        val projection = arrayOf(MediaStore.Images.Media.DATA)
//        val cursor = requireContext().contentResolver.query(uri, projection, null, null, null)
//        cursor?.use {
//            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//            if (it.moveToFirst()) {
//                return it.getString(columnIndex)
//            }
//        }
//        return null
//    }

    }
    private fun uploadImage(file: File) {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        val publicId = equipo.idLogo
        Log.d("ImageUpload", "Subiendo imagen: ${file.absolutePath}, publicId: $publicId")
        presenter.actualizarLogoEquipo(equipo.id, publicId, body).apply {

        }
    }

    private fun subirLogoEquipo() {
        selectedImageUri?.let { uri ->
            val file = getFileFromUri(uri) ?: run {
                Toast.makeText(context, "No se pudo obtener el archivo.", Toast.LENGTH_SHORT).show()
                return
            }
            uploadImage(file)
        } ?: run {
            Toast.makeText(context, "No se ha seleccionado ninguna imagen.", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(context, "Jugador eliminado: ${jugador.nombres}", Toast.LENGTH_SHORT).show()
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

    override fun mostrarActualizacionLogoExitosa() {
        Toast.makeText(context, "Logo actualizado exitosamente", Toast.LENGTH_SHORT).show()
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showValidacionExitosa(idJugador: String) {
        Toast.makeText(context, "Validación exitosa para el jugador con ID: $idJugador", Toast.LENGTH_SHORT).show()
    }

    override fun showJugadores(jugadores: List<User>) {
        jugadoresAdapter = JugadoresSeleccionadosAdapter(
            jugadores = jugadores.toMutableList(),
            onRemove = { jugador ->
                Toast.makeText(context, "Jugador eliminado: ${jugador.nombres}", Toast.LENGTH_SHORT).show()
                val updatedList = jugadores.toMutableList().apply {
                    remove(jugador)
                }
                jugadoresAdapter.notifyDataSetChanged()
            }
        )
        view?.findViewById<RecyclerView>(R.id.recyclerJugadoresSeleccionados)?.adapter = jugadoresAdapter
    }

    override fun showLoading(isLoading: Boolean) {
        val progressBar = view?.findViewById<ProgressBar>(R.id.progressBar)
        progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
