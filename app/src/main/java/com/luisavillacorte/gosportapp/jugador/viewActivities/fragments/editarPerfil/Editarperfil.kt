package com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.editarPerfil

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.common.apiRetrofit.RetrofitInstance
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.homeCampeonatosService.HomeApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.Equipo
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.Campeonatos
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.HomeCampeonatosContract
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.HomeCampeonatosPresenter
import java.io.InputStream

class Editarperfil : Fragment(), HomeCampeonatosContract.View {

    private lateinit var presenter: HomeCampeonatosPresenter
    private lateinit var nombreTextView: TextView
    private lateinit var identificacio: TextView
    private lateinit var fichaperfil: TextView
    private lateinit var telefono: TextView
    private lateinit var jornada: TextView
    private lateinit var correo: TextView
    private lateinit var programa: TextView
    private lateinit var btnGuardarCambios: Button
    private lateinit var btnSubirFotoPerfil: Button
    private lateinit var imageView: ImageView

    private var userId: String? = null
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_editarperfil, container, false)

        nombreTextView = view.findViewById(R.id.edtNombre)
        identificacio = view.findViewById(R.id.edtidentificacionperfil)
        fichaperfil = view.findViewById(R.id.fichaperfil)
        telefono = view.findViewById(R.id.edtTelefono)
        jornada = view.findViewById(R.id.edtjornadaperfil)
        correo = view.findViewById(R.id.edtEmail)
        programa = view.findViewById(R.id.edtPrograma)
        btnGuardarCambios = view.findViewById(R.id.btnGuardar)
        imageView = view.findViewById(R.id.imageViewModal)
        btnSubirFotoPerfil = view.findViewById(R.id.btnSubirFotoperfil)

        // Verificar y solicitar permisos
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PICK_IMAGE_REQUEST)
        }

        // Obtener el ID del usuario desde SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        userId = sharedPreferences.getString("user_id", null)
        Log.d("Editarperfil", "User ID recuperado: $userId")

        val apiService = RetrofitInstance.createService(HomeApiService::class.java)
        presenter = HomeCampeonatosPresenter(this, requireContext(), apiService)

        if (userId == null) {
            Log.d("Editarperfil", "User ID es nulo, llamando a obtenerPerfilUsuario")
            presenter.getPerfilUsuario()
        } else {
            Log.d("Editarperfil", "User ID ya disponible: $userId")
            presenter.getPerfilUsuario()
        }

        btnGuardarCambios.setOnClickListener {
            if (userId == null) {
                showError("User ID no disponible")
                return@setOnClickListener
            }

            val perfilActualizado = PerfilUsuarioResponse(
                id = userId!!,
                nombres = nombreTextView.text.toString(),
                telefono = telefono.text.toString(),
                correo = correo.text.toString(),
                ficha = fichaperfil.text.toString(),
                jornada = jornada.text.toString(),
                identificacion = identificacio.text.toString(),
                contrasena = "",  // Actualiza estos valores según sea necesario
                public_id = "",
                url_foto = "",
                programa = programa.text.toString(),
                rol = "",
                esCapitan = ""
            )
            presenter.actualizarPerfilUsuario(perfilActualizado)
        }

        btnSubirFotoPerfil.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val filePath = data.data
            val inputStream: InputStream? = filePath?.let { requireActivity().contentResolver.openInputStream(it) }
            val bitmap = BitmapFactory.decodeStream(inputStream)
            imageView.setImageBitmap(bitmap)

            // Aquí puedes manejar la imagen seleccionada, como subirla a un servidor
            filePath?.let { presenter.subirFoto(it) }
        }
    }


    override fun traernombre(perfilUsuarioResponse: PerfilUsuarioResponse) {
        nombreTextView.text = perfilUsuarioResponse.nombres
        identificacio.text = perfilUsuarioResponse.identificacion
        fichaperfil.text = perfilUsuarioResponse.ficha
        telefono.text = perfilUsuarioResponse.telefono
        jornada.text = perfilUsuarioResponse.jornada
        correo.text = perfilUsuarioResponse.correo
        programa.text = perfilUsuarioResponse.programa

        // Guardar el ID del usuario en SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user_id", perfilUsuarioResponse.id)
        editor.apply()
        Log.d("Editarperfil", "User ID guardado: ${perfilUsuarioResponse.id}")

        // Actualizar el userId local
        userId = perfilUsuarioResponse.id
    }

    override fun showInscripcionError(message: String) {
        TODO("Not yet implemented")
    }

    override fun navigateToCrearEquipo() {
        TODO("Not yet implemented")
    }

    override fun navigateToGestionarEquipo(equipo: Equipo) {
        TODO("Not yet implemented")
    }

    override fun mostrarBotonGestionarEquipo() {
        TODO("Not yet implemented")
    }

    override fun mostrarBotonCrearEquipo() {
        TODO("Not yet implemented")
    }

    override fun mostrarMensajeSnackBar(message: String) {
        TODO("Not yet implemented")
    }

    override fun showValidacionInscripcion(estaInscrito: Boolean, equipo: Equipo?) {
        TODO("Not yet implemented")
    }

    override fun showSuccess(message: String) {
        TODO("Not yet implemented")
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        Log.d("Editarperfil", "Error: $message")
    }

    override fun showLoading() {
        // Mostrar indicador de carga
    }

    override fun hideLoading() {
        // Ocultar indicador de carga
    }

    override fun showCampeonatos(campeonatos: List<Campeonatos>) {
        // Lógica para mostrar campeonatos si es necesario
    }
}
