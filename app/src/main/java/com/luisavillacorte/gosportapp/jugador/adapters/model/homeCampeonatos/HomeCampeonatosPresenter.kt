package com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos

import android.util.Log
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.homeCampeonatosService.HomeApiService
import retrofit2.Call
import retrofit2.Response

class HomeCampeonatosPresenter(private val campeonatosService: HomeApiService) : HomeCampeonatosContract.Presenter {

    private var view: HomeCampeonatosContract.View? = null

    override fun attachView(view: HomeCampeonatosContract.View) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }

    override fun getCampeonatos() {
        view?.showLoading()
        Log.d("HomePresenter", "Fetching campeonatos from API")
        val call = campeonatosService.getCampeonato()
        call.enqueue(object : retrofit2.Callback<List<Campeonatos>> {
            override fun onResponse(call: Call<List<Campeonatos>>, response: Response<List<Campeonatos>>) {
                if (response.isSuccessful) {
                    response.body()?.let { campeonatos ->
                        // Filtrar los campeonatos por los estados 'Inscripción' o 'Ejecución'
                        val campeonatosFiltrados = campeonatos.filter {
                            it.estadoCampeonato == "Ejecucion" || it.estadoCampeonato == "Inscripcion"
                        }

                        // Mostrar solo los campeonatos filtrados
                        view?.showCampeonatos(campeonatosFiltrados)
                        Log.d("HomePresenter", "Campeonatos filtered and shown: ${campeonatosFiltrados.size}")
                    }
                } else {
                    view?.showError("error: ${response.code()}")
                    Log.e("HomePresenter", "Error response code: ${response.code()}")
                }
                view?.hideLoading()
            }

            override fun onFailure(call: Call<List<Campeonatos>>, t: Throwable) {
                view?.hideLoading()
                view?.showError(t.message ?: "unknown error")
                Log.e("HomePresenter", "API call failed: ${t.message}")
            }
        })
    }

}