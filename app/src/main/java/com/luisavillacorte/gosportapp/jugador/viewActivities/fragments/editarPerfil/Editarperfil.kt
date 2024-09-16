package com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.editarPerfil

import EditarPerfilPresenter
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.common.apiRetrofit.RetrofitInstance
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.homeCampeonatosService.HomeApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.editarPerfil.EditarPerfilContract
import com.luisavillacorte.gosportapp.jugador.adapters.model.editarPerfil.Programas
import okhttp3.MultipartBody
import java.io.InputStream

class Editarperfil : Fragment(), EditarPerfilContract.View {

    private lateinit var presenter: EditarPerfilPresenter
    private lateinit var nombreTextView: TextView
    private lateinit var identificacio: TextView
    private lateinit var fichaperfil: TextView
    private lateinit var telefono: TextView
    private lateinit var jornada: TextView
    private lateinit var correo: TextView
    private lateinit var programa: Spinner
    private lateinit var btnGuardarCambios: Button
    private lateinit var btnSubirFotoPerfil: ImageView
    private lateinit var btnActualizarFoto: ImageView
    private lateinit var btnEliminarFoto: ImageView
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
        programa = view.findViewById(R.id.spinnerProgramas)
        btnGuardarCambios = view.findViewById(R.id.btnGuardar)
        imageView = view.findViewById(R.id.imagensubir)
        btnSubirFotoPerfil = view.findViewById(R.id.btnSubirFotoperfil)
        btnActualizarFoto = view.findViewById(R.id.btnActualizarFoto)
        btnEliminarFoto = view.findViewById(R.id.btnEliminarFoto)

        // Solicitar permisos para acceder a la galería
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PICK_IMAGE_REQUEST)
        }

        val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        userId = sharedPreferences.getString("user_id", null)
        Log.d("Editarperfil", "User ID recuperado: $userId")

        val apiService = RetrofitInstance.createService(HomeApiService::class.java)
        presenter = EditarPerfilPresenter(this, requireContext(), apiService)
        presenter.obtenerProgramas()

        userId?.let {
            presenter.getPerfilUsuario()
        }

        btnGuardarCambios.setOnClickListener {
            if (userId == null) {
                showError("User ID no disponible")
                return@setOnClickListener
            }

            val selectedProgram = programa.selectedItem?.toString() ?: run {
                showError("No se ha seleccionado ningún programa")
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
                programa = selectedProgram,
                esCapitan = false,
                public_id = "",
                rol = "jugador",
                url_foto = ""
            )

            presenter.actualizarPerfilUsuario(userId!!, perfilActualizado)
        }

        btnSubirFotoPerfil.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        btnActualizarFoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        btnEliminarFoto.setOnClickListener {
            userId?.let { id ->
                presenter.eliminarFoto(id)
            } ?: showError("User ID no disponible")
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data?.data != null) {
            val filePath = data.data
            Log.d("Editarperfil", "Imagen seleccionada: $filePath")
            filePath?.let { uri ->
                if (btnActualizarFoto.visibility == View.VISIBLE) {
                    presenter.actualizarFoto(uri, userId!!)
                } else {
                    presenter.subirFoto(uri, userId!!)
                }
                Glide.with(this)
                    .load(uri)
                    .into(imageView)
            } ?: showError("No se pudo obtener la URI de la imagen")
        }
    }

    override fun traernombre(perfilUsuarioResponse: PerfilUsuarioResponse) {
        Log.d("Editarperfil", "Datos del perfil: ${perfilUsuarioResponse.nombres}, ${perfilUsuarioResponse.url_foto}")
        nombreTextView.text = perfilUsuarioResponse.nombres
        identificacio.text = perfilUsuarioResponse.identificacion
        fichaperfil.text = perfilUsuarioResponse.ficha
        telefono.text = perfilUsuarioResponse.telefono
        jornada.text = perfilUsuarioResponse.jornada
        correo.text = perfilUsuarioResponse.correo

        val adapter = programa.adapter
        if (adapter is ArrayAdapter<*>) {
            val position = (adapter as ArrayAdapter<String>).getPosition(perfilUsuarioResponse.programa)
            if (position >= 0) {
                programa.setSelection(position)
            } else {
                Log.d("Editarperfil", "Programa no encontrado en la lista del Spinner")
            }
        } else {
            Log.d("Editarperfil", "Adaptador no es del tipo esperado")
        }

        perfilUsuarioResponse.url_foto?.let { url ->
            Glide.with(this)
                .load(url)
                .into(imageView)
        }

        if (!perfilUsuarioResponse.url_foto.isNullOrEmpty()) {
            btnActualizarFoto.visibility = View.VISIBLE
            btnEliminarFoto.visibility = View.VISIBLE
            btnSubirFotoPerfil.visibility = View.GONE
        } else {
            btnActualizarFoto.visibility = View.GONE
            btnEliminarFoto.visibility = View.GONE
            btnSubirFotoPerfil.visibility = View.VISIBLE
        }

        val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user_id", perfilUsuarioResponse.id)
        editor.apply()
        Log.d("Editarperfil", "User ID guardado: ${perfilUsuarioResponse.id}")

        userId = perfilUsuarioResponse.id
    }

    override fun showSuccess(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        Log.d("Editarperfil", "Error: $message")
    }

    override fun showLoading() {
        // Mostrar indicador de carga
    }

    override fun hideLoading() {
        // Ocultar indicador de carga
    }

    override fun mostrarProgramas(programas: List<Programas>) {
        val nombresProgramas = programas.map { it.namePrograma }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nombresProgramas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        programa.adapter = adapter
    }
}
