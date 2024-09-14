import android.content.Context
import android.net.Uri
import android.util.Log
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.homeCampeonatosService.HomeApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.editarPerfil.EditarPerfilContract
import com.luisavillacorte.gosportapp.jugador.adapters.model.editarPerfil.Programas
import com.luisavillacorte.gosportapp.jugador.adapters.storage.TokenManager
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class EditarPerfilPresenter(
    private val view: EditarPerfilContract.View,
    private val context: Context,
    private val apiService: HomeApiService
) : EditarPerfilContract.Presenter {

    private val tokenManager = TokenManager(context)
    private val token: String = tokenManager.getToken() ?: throw IllegalStateException("Token no disponible")

    override fun getPerfilUsuario() {
        view.showLoading()
        apiService.obtenerPerfilUsuario(token).enqueue(object : Callback<PerfilUsuarioResponse> {
            override fun onResponse(call: Call<PerfilUsuarioResponse>, response: Response<PerfilUsuarioResponse>) {
                view.hideLoading()
                if (response.isSuccessful) {
                    response.body()?.let {
                        view.traernombre(it)
                    }
                } else {
                    Log.e("EditarPerfilPresenter", "Error al obtener perfil: ${response.errorBody()?.string()}")
                    view.showError("Error al obtener perfil")
                }
            }

            override fun onFailure(call: Call<PerfilUsuarioResponse>, t: Throwable) {
                view.hideLoading()
                Log.e("EditarPerfilPresenter", "Error: ${t.message}")
                view.showError("Error: ${t.message}")
            }
        })
    }


    override fun actualizarPerfilUsuario(id: String, perfilUsuarioResponse: PerfilUsuarioResponse) {
        view.showLoading()
        apiService.actualizarPerfilUsuario(token, id, perfilUsuarioResponse).enqueue(object : Callback<PerfilUsuarioResponse> {
            override fun onResponse(call: Call<PerfilUsuarioResponse>, response: Response<PerfilUsuarioResponse>) {
                view.hideLoading()
                if (response.isSuccessful) {
                    view.showSuccess("Perfil actualizado con éxito")
                } else {
                    Log.e("EditarPerfilPresenter", "Error al actualizar perfil: ${response.errorBody()?.string()}")
                    view.showError("Error al actualizar perfil")
                }
            }

            override fun onFailure(call: Call<PerfilUsuarioResponse>, t: Throwable) {
                view.hideLoading()
                Log.e("EditarPerfilPresenter", "Error: ${t.message}")
                view.showError("Error: ${t.message}")
            }
        })
    }

    override fun subirFoto(uri: Uri, idUser: String) {
        val file = createFileFromUri(uri) ?: return

        val requestFile = file.asRequestBody("image/*".toMediaType())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        apiService.subirFotousuario(idUser, token, body).enqueue(object : Callback<PerfilUsuarioResponse> {
            override fun onResponse(call: Call<PerfilUsuarioResponse>, response: Response<PerfilUsuarioResponse>) {
                if (response.isSuccessful) {
                    view.showSuccess("Foto subida con éxito")
                } else {
                    Log.e("EditarPerfilPresenter", "Error al subir foto: ${response.errorBody()?.string()}")
                    view.showError("Error al subir foto")
                }
            }

            override fun onFailure(call: Call<PerfilUsuarioResponse>, t: Throwable) {
                Log.e("EditarPerfilPresenter", "Error: ${t.message}")
                view.showError("Error: ${t.message}")
            }
        })
    }

    override fun actualizarFoto(uri: Uri, idUser: String) {
        val file = createFileFromUri(uri) ?: return

        val requestFile = file.asRequestBody("image/*".toMediaType())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        apiService.actualizarfoto(idUser, token, body).enqueue(object : Callback<PerfilUsuarioResponse> {
            override fun onResponse(call: Call<PerfilUsuarioResponse>, response: Response<PerfilUsuarioResponse>) {
                if (response.isSuccessful) {
                    view.showSuccess("Foto actualizada con éxito")
                } else {
                    Log.e("EditarPerfilPresenter", "Error al actualizar foto: ${response.errorBody()?.string()}")
                    view.showError("Error al actualizar foto")
                }
            }

            override fun onFailure(call: Call<PerfilUsuarioResponse>, t: Throwable) {
                Log.e("EditarPerfilPresenter", "Error: ${t.message}")
                view.showError("Error: ${t.message}")
            }
        })
    }

    override fun eliminarFoto(idUser: String) {
        view.showLoading()
        apiService.eliminarFotoUsuario(idUser, token).enqueue(object : Callback<PerfilUsuarioResponse> {
            override fun onResponse(call: Call<PerfilUsuarioResponse>, response: Response<PerfilUsuarioResponse>) {
                view.hideLoading()
                if (response.isSuccessful) {
                    view.showSuccess("Foto eliminada con éxito")
                } else {
                    Log.e("EditarPerfilPresenter", "Error al eliminar foto: ${response.errorBody()?.string()}")
                    view.showError("Error al eliminar foto")
                }
            }

            override fun onFailure(call: Call<PerfilUsuarioResponse>, t: Throwable) {
                view.hideLoading()
                Log.e("EditarPerfilPresenter", "Error: ${t.message}")
                view.showError("Error: ${t.message}")
            }
        })
    }

    override fun obtenerProgramas() {
        view.showLoading()
        apiService.getProgramas().enqueue(object : Callback<List<Programas>> {
            override fun onResponse(call: Call<List<Programas>>, response: Response<List<Programas>>) {
                view.hideLoading()
                if (response.isSuccessful) {
                    response.body()?.let { programas ->
                        Log.d("EditarPerfilPresenter", "Programas obtenidos:")
                        for (programa in programas) {
                            Log.d("EditarPerfilPresenter", "Nombre del programa: ${programa.namePrograma}")
                        }
                        view.mostrarProgramas(programas)
                    } ?: run {
                        Log.d("EditarPerfilPresenter", "Respuesta vacía al obtener programas")
                        view.showError("No se encontraron programas")
                    }
                } else {
                    Log.e("EditarPerfilPresenter", "Error al obtener programas: ${response.errorBody()?.string()}")
                    view.showError("Error al obtener programas")
                }
            }

            override fun onFailure(call: Call<List<Programas>>, t: Throwable) {
                view.hideLoading()
                Log.e("EditarPerfilPresenter", "Error: ${t.message}")
                view.showError("Error: ${t.message}")
            }
        })
    }

    private fun createFileFromUri(uri: Uri): File? {
        // Implementación básica para convertir Uri en File
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.cacheDir, "temp_file.jpg") // Cambia la extensión según el tipo de archivo
            val outputStream = file.outputStream()
            inputStream?.copyTo(outputStream)
            file
        } catch (e: Exception) {
            Log.e("EditarPerfilPresenter", "Error al crear archivo desde URI: ${e.message}")
            null
        }
    }
}
