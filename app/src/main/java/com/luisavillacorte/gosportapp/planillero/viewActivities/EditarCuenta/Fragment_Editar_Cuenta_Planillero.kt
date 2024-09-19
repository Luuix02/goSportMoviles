    package com.luisavillacorte.gosportapp.planillero.viewActivities.EditarCuenta

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import com.bumptech.glide.Glide
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle

import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Transformation
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.core.content.ContextCompat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.common.apiRetrofit.RetrofitInstance
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.planillero.adpaters.api.EditarCuentaPlanillero.ApiServiceEdiarCuenta
import com.luisavillacorte.gosportapp.planillero.adpaters.model.EditarCuentaPlanillero.ContractEditarCuentaPlanillero
import com.luisavillacorte.gosportapp.planillero.adpaters.model.EditarCuentaPlanillero.DatosPlanilleroActualizar
import com.luisavillacorte.gosportapp.planillero.adpaters.model.EditarCuentaPlanillero.PresenterEditarCuentaPlanillero
import com.squareup.picasso.Picasso

class Fragment_Editar_Cuenta_Planillero : Fragment(), ContractEditarCuentaPlanillero.View {
    private lateinit var PresenterPlanillero : ContractEditarCuentaPlanillero.Presenter



    private lateinit var NombrePlanillero : EditText
    private lateinit var TelefonoPlanillero : EditText
    private lateinit var CorreoPlanillero : EditText
    private lateinit var ContraseñaPlanillero : EditText;
    private lateinit var BotonActualizar : Button;

    private lateinit var BotonSeleccionaarFoto: ImageView
    private lateinit var FotoPlanillero : ImageView
    private val PICK_IMAGE_REQUEST = 1
    private var imagePath: String? = null
    private var publicId: String? = null
    private lateinit var BotonoEliminarFotoPlanillero: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment__editar__cuenta__planillero, container, false)
        NombrePlanillero = view.findViewById(R.id.editTextNombrePlanillero)
        TelefonoPlanillero = view.findViewById(R.id.editTextTelefonoPlanillero);
        CorreoPlanillero = view.findViewById(R.id.editTextCorreoPlanillero);
        ContraseñaPlanillero = view.findViewById(R.id.editTextContraseñaPlanillero);
        BotonActualizar = view.findViewById(R.id.BotonActualizarDatosPlanillero);
        FotoPlanillero = view.findViewById(R.id.FotoPlanillero);
        BotonoEliminarFotoPlanillero = view.findViewById(R.id.BotonEliminarFotoPlanillero);
        // Botones de foto
        val botonAñadirFoto = view.findViewById<ImageView>(R.id.BotonAñadirFotoPlanillero)
        val botonActualizarFoto = view.findViewById<ImageView>(R.id.BotonActualizarFotoPlanillero)


        PresenterPlanillero= PresenterEditarCuentaPlanillero(this, requireContext(),RetrofitInstance.createService(ApiServiceEdiarCuenta::class.java))

        BotonActualizar.setOnClickListener {
            ActualizarDatosPlanillero();
        }

        BotonoEliminarFotoPlanillero.setOnClickListener {
            EliminarFotoPlaninillero();
        }
        botonAñadirFoto.setOnClickListener {
            SeleccionarFotoPlanillero()
        }

        botonActualizarFoto.setOnClickListener {
            SeleccionarFotoPlanillero()
        }
        BotonSubirOActualizar();
        PresenterPlanillero.obtenerPerfilUsuario()
        return view
    }
    private fun ExisteFoto(userIdPlanillero: String): Boolean {
        return !publicId.isNullOrBlank() || !imagePath.isNullOrBlank()
    }

    private fun BotonSubirOActualizar(){
        val userIdPlanillero = UserIdPlanillero() ?: return Toast.makeText(requireContext(), "Error al recuperar el id", Toast.LENGTH_SHORT).show()
        val existeFoto = ExisteFoto(userIdPlanillero)

        val botonSubir = view?.findViewById<ImageView>(R.id.BotonAñadirFotoPlanillero)
        val botonActualizar = view?.findViewById<ImageView>(R.id.BotonActualizarFotoPlanillero)

        if (existeFoto) {
            botonSubir?.visibility = View.GONE
            botonActualizar?.visibility = View.VISIBLE
        } else {
            botonSubir?.visibility = View.VISIBLE
            botonActualizar?.visibility = View.GONE
        }
    }

    private fun SeleccionarFotoPlanillero() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen"), PICK_IMAGE_REQUEST)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val uri = data.data
            uri?.let {
                imagePath = getFilePathFromUri(it)
                mostrarVistaPrevia(it)
            }
        }
    }


    private fun mostrarVistaPrevia(uri: Uri) {
        val userIdPlanillero = UserIdPlanillero() ?: return Toast.makeText(requireContext(), "Error al recuperar el id", Toast.LENGTH_SHORT).show()
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Vista Previa")
            .setPositiveButton("Guardar") { _, _ ->
                imagePath?.let {
                    if (ExisteFoto(userIdPlanillero)) {
                        publicId?.let { id ->
                            PresenterPlanillero.actualizarFoto(userIdPlanillero, it, id)
                        }
                    } else {
                        PresenterPlanillero.subirFoto(userIdPlanillero, it)
                    }
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        val view = layoutInflater.inflate(R.layout.previzualizar_iamgen_planillero, null)
        val imageViewPreview = view.findViewById<ImageView>(R.id.FotoModalPlanillerooo)

        Glide.with(this)
            .load(uri)
            .into(imageViewPreview)

        dialog.setView(view)
        dialog.show()
        val botonAceptar = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        val botonCancel = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(16, 16, 16, 16)


        botonAceptar?.let {
            it.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            it.background = ContextCompat.getDrawable(requireContext(), R.drawable.boton)

            it.backgroundTintList = null
        }

        botonCancel?.let {
            it.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            it.background = ContextCompat.getDrawable(requireContext(), R.drawable.border)

            it.backgroundTintList = null

        }
    }

    private fun getFilePathFromUri(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireContext().contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            if (it.moveToFirst()) {
                return it.getString(columnIndex)
            }
        }
        return null
    }


    override fun PerfilPlanillero(perfil: PerfilUsuarioResponse) {
        if (isAdded) {
            Log.d(TAG, "PerfilPlanillero: $perfil")
            NombrePlanillero.hint = perfil.nombres
            TelefonoPlanillero.hint = perfil.telefono
            CorreoPlanillero.hint = perfil.correo
            publicId = perfil.public_id;
            imagePath = perfil.url_foto;

            if (perfil.url_foto.isNullOrEmpty()) {
                FotoPlanillero.setImageResource(R.drawable.usuario2)
            } else {
                FotoPlanillero.background = ContextCompat.getDrawable(requireContext(), R.drawable.radius_perfil_foto)
                Picasso.get()
                    .load(perfil.url_foto)
                    .placeholder(R.drawable.loading)
                   // .error(R.drawable.error)
                    .into(FotoPlanillero)
            }
            BotonSubirOActualizar()
        } else {
            Log.e(TAG, "El fragmento no está adjunto al contexto.")
        }
    }



    private fun EliminarFotoPlaninillero() {
        val userIdPlanillero = UserIdPlanillero() ?: return Toast.makeText(requireContext(), "Error al recuperar el id", Toast.LENGTH_SHORT).show()
        if (publicId.isNullOrBlank()) {
            Toast.makeText(context, "No hay foto para eliminar", Toast.LENGTH_SHORT).show()
            return
        }
        PresenterPlanillero.eliminarFoto(userIdPlanillero, publicId!!)
    }


    private fun UserIdPlanillero(): String? {
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("USER_ID", null)
    }

    private fun ActualizarDatosPlanillero() {
        val userIdPlanillero = UserIdPlanillero() ?: return Toast.makeText(requireContext(), "Error al recuperar el id", Toast.LENGTH_SHORT).show()
        val nombre = NombrePlanillero.text.toString()
        val telefono = TelefonoPlanillero.text.toString()
        val correo = CorreoPlanillero.text.toString()
        val contraseña = ContraseñaPlanillero.text.toString()

        if (nombre.isEmpty() && telefono.isEmpty() && correo.isEmpty() && contraseña.isEmpty()) {
            return Toast.makeText(requireContext(), "Los campos están vacíos", Toast.LENGTH_SHORT).show()
        }

        val actualizar = DatosPlanilleroActualizar(
            nombres = if (nombre.isNotEmpty()) nombre else null,
            telefono = if (telefono.isNotEmpty()) telefono else null,
            correo = if (correo.isNotEmpty()) correo else null,
            contrasena = if (contraseña.isNotEmpty()) contraseña else null
        )

        PresenterPlanillero.updatePerfilPlanillero(userIdPlanillero, actualizar)
        PresenterPlanillero.obtenerPerfilUsuario()
    }



    override fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

    }

    override fun error(error: String) {
        Toast.makeText(requireContext(), "ERRRORRR", Toast.LENGTH_SHORT).show()

    }

    override fun showSuccess(message: String) {
        Toast.makeText(requireContext(), "Se actualizo con exito", Toast.LENGTH_SHORT).show()

    }

//    override fun Foto(photoUrl: String?) {
//        if (photoUrl.isNullOrEmpty()) {
//            FotoPlanillero.setImageResource(R.drawable.usuario) // Usa una imagen por defecto o una acción alternativa
//        } else {
//            Glide.with(this)
//                .load(photoUrl)
//                .into(FotoPlanillero)
//        }
//    }
}


