package com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.homeFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.common.apiRetrofit.RetrofitInstance
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.homeCampeonatosService.HomeApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.Equipo
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.Campeonatos
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.CampeonatosAdapter
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.HomeCampeonatosContract
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.HomeCampeonatosPresenter
import com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.crearEquipoFragments.FragmentCrearEquipo
import com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.gestionarMiEquipoFragments.FragmentGestionarMiEquipo
import kotlin.math.E

class FragmentHome : Fragment(), HomeCampeonatosContract.View {

    private lateinit var presenter: HomeCampeonatosPresenter
    private lateinit var recyclerViewCampeonatos: RecyclerView
    private lateinit var campeonatosAdapter: CampeonatosAdapter
    private lateinit var nombrejuga: TextView
    private lateinit var btnFlotante: FloatingActionButton
    private var idJugador: String? = null
    private var perfilUsuarioResponse: PerfilUsuarioResponse? = null
    private var esCapitan: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = HomeCampeonatosPresenter(
            this, requireContext(),
            RetrofitInstance.createService(HomeApiService::class.java)
        )

        btnFlotante= view.findViewById(R.id.btnFlotanteAddTeam)
        recyclerViewCampeonatos = view.findViewById(R.id.recyclerViewCampeonatos)
        nombrejuga = view.findViewById(R.id.nombreusuario)

        recyclerViewCampeonatos.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)




        // Llama a los métodos del Presenter
        presenter.getCampeonatos()
        presenter.getPerfilUsuario()

        //        //nueva linea
        btnFlotante.visibility = View.GONE



        btnFlotante.setOnClickListener{
            handleBotonFlotanteClick()
//            val idJugador = this.idJugador
//            if (idJugador != null){
//                val esCapitan = perfilUsuarioResponse?.esCapitan ?: false
//                if (esCapitan){
//                    presenter.validarInscripcionJugador(idJugador)
//                } else {
//                    showError("Solo el capitán del equipo puede gestionar el equipo.")
//                }
//            } else {
//                navigateToCrearEquipo()
////                showError("Error al obtener la información del jugador")
//            }

        }

    }
    private fun handleBotonFlotanteClick() {
        if (esCapitan){
            mostrarMensajeSnackBar("Actualiza tu Equipo")
            presenter.validarInscripcionJugador(idJugador!!)
        } else {
            mostrarMensajeSnackBar("Crea tu equipo")
            navigateToCrearEquipo()
        }
    }

    override fun showLoading() {
        // Muestra un indicador de carga si es necesario
    }

    override fun hideLoading() {
        // Oculta el indicador de carga si es necesario
    }

    override fun showCampeonatos(campeonatos: List<Campeonatos>) {
        if (campeonatos.isNotEmpty()) {
            campeonatosAdapter = CampeonatosAdapter(campeonatos)
            recyclerViewCampeonatos.adapter = campeonatosAdapter
        } else {
            context?.let {
                Toast.makeText(it, "No hay campeonatos disponibles", Toast.LENGTH_SHORT).show()
            } ?: Log.e("HomeFragment", "Context is null, cannot show toast")
        }
    }

    override fun showError(message: String) {
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        } ?: Log.e("HomeFragment", "Context is null, cannot show toast")
    }

    override fun traernombre(perfilUsuarioResponse: PerfilUsuarioResponse) {
        val nombreJugador = perfilUsuarioResponse.nombres
        val idJugador = perfilUsuarioResponse.id
        esCapitan = perfilUsuarioResponse.esCapitan
        this.idJugador = idJugador
        nombrejuga.text = "Hola $nombreJugador, Bienvenido a GoSport"
        Log.d("HomeFragment", "Nombre del usuario: $nombreJugador")
        presenter.validarInscripcionJugador(idJugador)
//        if (esCapitan){
//            mostrarBotonGestionarEquipo()
//        } else {
//            mostrarBotonCrearEquipo()
//        }
    }
    override fun showValidacionInscripcion(estaInscrito: Boolean, equipo: Equipo?) {
        if (estaInscrito) {
            if (esCapitan) {
                // Si está inscrito y es capitán, mostrar el botón para gestionar el equipo
                btnFlotante.visibility = View.VISIBLE
                btnFlotante.setImageResource(R.drawable.ic_editar_equipo)  // Cambiar icono
                btnFlotante.setOnClickListener {
                    mostrarMensajeSnackBar("Actualiza tu Equipo")
                    if (equipo != null){
                        navigateToGestionarEquipo(equipo)
                    } else {
                        mostrarMensajeSnackBar("Error al obtener el equipo")
                    }
                     // Navegar a la gestión del equipo
                }
            } else {
                // Si está inscrito pero no es capitán, ocultar el botón
                btnFlotante.visibility = View.GONE
            }
        } else {
            // Si no está inscrito, mostrar el botón para crear un equipo
            btnFlotante.visibility = View.VISIBLE
            btnFlotante.setImageResource(R.drawable.crearequipo)  // Cambiar icono
            btnFlotante.setOnClickListener {
                mostrarMensajeSnackBar("Crea tu equipo")
                navigateToCrearEquipo()  // Navegar a la creación de equipo
            }
        }
    }
    override fun mostrarBotonGestionarEquipo() {
        btnFlotante.setImageResource(R.drawable.ic_editar_equipo)  // Cambia el ícono
    }

    override fun mostrarBotonCrearEquipo() {
        btnFlotante.setImageResource(R.drawable.crearequipo)  // Cambia el ícono
    }

    override fun mostrarMensajeSnackBar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

    override fun showInscripcionError(message: String) {
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        } ?: Log.e("HomeFragment", "Context is null, cannot show toast")
    }

    override fun navigateToCrearEquipo() {
        if (isAdded) {
            val fragmentCrearEquipo = FragmentCrearEquipo()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragmentCrearEquipo)
                .addToBackStack(null)
                .commit()
        } else {
            Log.e("FragmentHome", "El fragmento no está agregado alFragmentManager")
        }
    }

    override fun navigateToGestionarEquipo(equipo: Equipo) {
        val fragmentGestionarMiEquipo = FragmentGestionarMiEquipo().apply {
            arguments = Bundle().apply {
                putParcelable("equipo", equipo)
                putString("idJugador", idJugador)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragmentGestionarMiEquipo)
            .addToBackStack(null)
            .commit()

    }
}
