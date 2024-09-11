package com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.editarPerfil

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.Campeonatos
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.HomeCampeonatosContract
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.HomeCampeonatosPresenter
import com.squareup.picasso.Picasso
import java.io.File

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
    private lateinit var btnSubirFoto: Button
    private lateinit var imgFotoPerfil: ImageView

    private var userId: String? = null
    private var uriImagen: Uri? = null

    companion object {
        const val PICK_IMAGE_REQUEST = 1
        const val REQUEST_CODE = 1001
    }

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
        btnSubirFoto = view.findViewById(R.id.btnSubirFotoperfil)
        imgFotoPerfil = view.findViewById(R.id.imageViewModal) // Asegúrate de que este ID es correcto

        // Obtener el ID del usuario desde SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        userId = sharedPreferences.getString("user_id", null)

        val apiService = RetrofitInstance.createService(HomeApiService::class.java)
        presenter = HomeCampeonatosPresenter(this, requireContext(), apiService)

        // Verificar permisos y solicitar si es necesario
        checkAndRequestPermissions()

        // Cargar perfil del usuario si el ID está disponible
        if (userId != null) {
            presenter.getPerfilUsuario()
        } else {
            showError("User ID no disponible")
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
                contrasena = "",
                public_id = null,
                url_foto = null,
                programa = programa.text.toString()
            )
            presenter.actualizarPerfilUsuario(perfilActualizado)
        }

        // Acción para seleccionar una foto desde la galería
        btnSubirFoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, PICK_IMAGE_REQUEST)
            } else {
                showError("Permiso de lectura de almacenamiento denegado")
            }
        }

        return view
    }

    private fun checkAndRequestPermissions() {
        val permission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, puedes proceder con la operación que requiere el permiso
            } else {
                // Permiso denegado, muestra un mensaje al usuario
                Toast.makeText(context, "Permiso de almacenamiento denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            uriImagen = data?.data
            if (uriImagen != null) {
                try {
                    val imageStream = requireContext().contentResolver.openInputStream(uriImagen!!)
                    if (imageStream != null) {
                        imgFotoPerfil.setImageURI(uriImagen)
                        presenter.subirFoto(uriImagen!!)
                    } else {
                        Log.e("Editarperfil", "No se pudo abrir el stream de la imagen")
                        showError("No se pudo abrir el stream de la imagen")
                    }
                } catch (e: Exception) {
                    Log.e("Editarperfil", "Error al abrir el archivo de imagen", e)
                    showError("Error al abrir el archivo de imagen")
                }
            } else {
                Log.e("Editarperfil", "URI de imagen es nula")
                showError("URI de imagen es nula")
            }
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

        // Cargar la foto si está disponible
        perfilUsuarioResponse.url_foto?.let {
            Picasso.get().load(it).into(imgFotoPerfil)
        }

        // Guardar el ID del usuario en SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user_id", perfilUsuarioResponse.id)
        editor.apply()
    }

    override fun showSuccess(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
