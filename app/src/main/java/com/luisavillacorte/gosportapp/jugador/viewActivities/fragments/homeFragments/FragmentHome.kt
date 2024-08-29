package com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.homeFragments

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.common.apiRetrofit.RetrofitInstance
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.homeCampeonatosService.HomeApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.Campeonatos
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.CampeonatosAdapter
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.HomeCampeonatosContract
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.HomeCampeonatosPresenter


class FragmentHome() : Fragment(), HomeCampeonatosContract.View, Parcelable {

    private lateinit var presenter: HomeCampeonatosPresenter
    private lateinit var recyclerView: RecyclerView
    private lateinit var campeonatosAdapter: CampeonatosAdapter
    constructor(parcel: Parcel) : this() {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewCampeonatos)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val campeonatosApiService = RetrofitInstance.createService(HomeApiService::class.java)
        presenter = HomeCampeonatosPresenter(campeonatosApiService)
        presenter.attachView(this)
        presenter.getCampeonatos()


    }

    override fun showLoading() {
        // Optionally show a loading indicator
    }

    override fun hideLoading() {
        // Optionally hide the loading indicator
    }

    override fun showCampeonatos(campeonatos: List<Campeonatos>) {
        if (campeonatos.isNotEmpty()) {
            campeonatosAdapter = CampeonatosAdapter(campeonatos)
            recyclerView.adapter = campeonatosAdapter
        } else {
            Toast.makeText(context, "No hay campeonatos disponibles", Toast.LENGTH_SHORT).show()
        }
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FragmentHome> {
        override fun createFromParcel(parcel: Parcel): FragmentHome {
            return FragmentHome(parcel)
        }

        override fun newArray(size: Int): Array<FragmentHome?> {
            return arrayOfNulls(size)
        }
    }

}