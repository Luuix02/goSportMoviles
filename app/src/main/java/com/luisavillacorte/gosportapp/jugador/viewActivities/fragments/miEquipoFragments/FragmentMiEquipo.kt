package com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.miEquipoFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.common.apiRetrofit.RetrofitInstance
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.miEquipoService.MiEquipoApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.Equipo
import com.luisavillacorte.gosportapp.jugador.adapters.model.miEquipo.MiEquipoContract
import com.luisavillacorte.gosportapp.jugador.adapters.model.miEquipo.MiEquipoPresenter
import com.luisavillacorte.gosportapp.jugador.adapters.model.miEquipo.VerJugadoresAdapter
import com.squareup.picasso.Picasso


class FragmentMiEquipo : Fragment(), MiEquipoContract.View {

    private lateinit var presenter: MiEquipoPresenter
    private lateinit var logoImageView: ImageView
    private lateinit var nombreEquipoTextView: TextView
    private lateinit var nombreCapitanTextView: TextView
    private lateinit var contactoUnoTextView: TextView
    private lateinit var contactoDosTextView: TextView
    private lateinit var miembrosRecyclerView: RecyclerView

    private var equipo: Equipo? = null
    private var idJugador: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mi_equipo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logoImageView = view.findViewById(R.id.logoEquipo)
        nombreEquipoTextView = view.findViewById(R.id.nombreEquipo)
        nombreCapitanTextView = view.findViewById(R.id.nombreCapitan)
        contactoUnoTextView = view.findViewById(R.id.CelularPrincipal)
        contactoDosTextView = view.findViewById(R.id.CelularSecundario)
        miembrosRecyclerView = view.findViewById(R.id.recyclerJugadoresSeleccionados)

        // Usa tu cliente Retrofit centralizado
        val apiService = RetrofitInstance.createService(MiEquipoApiService::class.java)
        presenter = MiEquipoPresenter(this, apiService, requireContext())

        // Obtén el perfil del usuario para recuperar el idJugador
        presenter.getPerfilUsuario()
    }

    override fun showPerfilUsuario(perfil: PerfilUsuarioResponse) {
        // Aquí extraes el idJugador del perfil
        idJugador = perfil.id // Asumiendo que `idJugador` es una propiedad de `PerfilUsuarioResponse`

        // Ahora valida la inscripción del jugador
        idJugador?.let {
            presenter.validarInscripcionJugador(it)
        } ?: run {
            mostrarError("ID de jugador no encontrado en el perfil.")
        }
    }

    override fun mostrarEquipo(equipo: Equipo) {
        Picasso.get()
            .load(equipo.imgLogo)
            .into(logoImageView)

        nombreEquipoTextView.text = equipo.nombreEquipo
        nombreCapitanTextView.text = "Capitán: ${equipo.nombreCapitan}"
        contactoUnoTextView.text = "Contacto 1: ${equipo.contactoUno}"
        contactoDosTextView.text = "Contacto 2: ${equipo.contactoDos}"

        miembrosRecyclerView.layoutManager = LinearLayoutManager(context)
        miembrosRecyclerView.adapter = VerJugadoresAdapter(
            equipo.participantes

        )
    }

    override fun mostrarError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}