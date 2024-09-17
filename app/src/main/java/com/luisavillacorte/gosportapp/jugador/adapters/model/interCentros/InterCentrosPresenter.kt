import android.content.Context
import com.google.gson.Gson
import com.luisavillacorte.gosportapp.jugador.adapters.model.interCentros.Equipo
import com.luisavillacorte.gosportapp.jugador.adapters.model.interCentros.InterCentrosContract
import com.luisavillacorte.gosportapp.jugador.adapters.model.interCentros.InterCentrosModel

class InterCentrosPresenter(
    private val view: InterCentrosContract.View,
    private val model: InterCentrosModel,
    private val context: Context // Necesitamos el contexto para obtener SharedPreferences
) : InterCentrosContract.Presenter {

    override fun loadPartidos(equipoId: String) {
        model.getPartidosJugados(equipoId) { partidos, error ->
            if (error != null) {
                view.showError(error)
            } else if (partidos != null) {
                view.showPartidos(partidos)
            }
        }
    }

    // Método para guardar el objeto Equipo completo en SharedPreferences
    private fun guardarEquipoEnSharedPreferences(context: Context, equipo: Equipo) {
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val equipoJson = gson.toJson(equipo) // Convertir el objeto equipo a JSON
        editor.putString("EQUIPO", equipoJson) // Guardar el JSON del equipo
        editor.apply()
    }

    // Método para obtener el objeto Equipo desde SharedPreferences
    private fun obtenerEquipoDeSharedPreferences(context: Context): Equipo? {
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val equipoJson = sharedPreferences.getString("EQUIPO", null)
        return if (equipoJson != null) {
            val gson = Gson()
            gson.fromJson(equipoJson, Equipo::class.java) // Deserializar el JSON a un objeto Equipo
        } else {
            null
        }
    }
}
