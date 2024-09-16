import com.luisavillacorte.gosportapp.jugador.adapters.model.interCentros.InterCentrosContract
import com.luisavillacorte.gosportapp.jugador.adapters.model.interCentros.InterCentrosModel

class InterCentrosPresenter(
    private val view: InterCentrosContract.View,
    private val model: InterCentrosModel
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

}
