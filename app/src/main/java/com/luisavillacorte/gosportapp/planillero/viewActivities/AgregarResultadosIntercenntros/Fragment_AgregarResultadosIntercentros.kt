package com.luisavillacorte.gosportapp.planillero.viewActivities.AgregarResultadosIntercenntros

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.common.apiRetrofit.RetrofitInstance
import com.luisavillacorte.gosportapp.planillero.adpaters.api.AgregarResultadosInter.ApiServiceAgregarResultadosIntercentros
import com.luisavillacorte.gosportapp.planillero.adpaters.api.vsEquiposResultados.ApiServiceResultados
import com.luisavillacorte.gosportapp.planillero.adpaters.model.IntercentrosAgregarResultadosView.AdapterAgregarResultadosIntercentros
import com.luisavillacorte.gosportapp.planillero.adpaters.model.IntercentrosAgregarResultadosView.AgregarResultadosIntercentrosPresenter
import com.luisavillacorte.gosportapp.planillero.adpaters.model.IntercentrosAgregarResultadosView.ContractAgergarResultadosInter
import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.EquiposIntercentrosAsignados
import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.Participantes
import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.ParticipantesInter
import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.Vs
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.DatosJugadorDestacado
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.ParticipanteAdapter
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.ResultadosPresenter
import com.luisavillacorte.gosportapp.planillero.helper.VerTarjetas
import com.squareup.picasso.Picasso


class Fragment_AgregarResultadosIntercentros : Fragment(), ContractAgergarResultadosInter.View {
    private lateinit var presenter: AgregarResultadosIntercentrosPresenter
    private lateinit var adapter: AdapterAgregarResultadosIntercentros
    private lateinit var recyclerView: RecyclerView
    private lateinit var nombreEquipo1Resultados: TextView
    private lateinit var nombreEquipo2Result: TextView
    private lateinit var logoEquipo1Result: ImageView
    private lateinit var logoEquipo2Result: ImageView
    private lateinit var Intercentros : EquiposIntercentrosAsignados
    private lateinit var raya2: View
    private lateinit var raya1: View
    private lateinit var BotonEquipo2: Button
    private lateinit var BotonEquipo1: Button
    private lateinit var marcadorEquipo1: TextView
    private lateinit var marcadorEquipo2: TextView
    private lateinit var botonGuardarDatos: Button
    private lateinit var viewModel: VerTarjetas
    private lateinit var contadorAmarillaEquipo1: TextView
    private lateinit var contadorAmarillaEquipo2: TextView
    private lateinit var contadorRojaEquipo1: TextView
    private lateinit var contadorRojaEquipo2: TextView
    private  lateinit var nombreEquipoSeleccionado : TextView;
    private  lateinit var  botonVerPenales : Button;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment__agregar_resultados_intercentros,
            container,
            false
        )
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idVs = arguments?.getString("idVs") ?: "Error"
        Log.d("Fragment_resultados", "ID VS: $idVs")

        nombreEquipo1Resultados = view.findViewById(R.id.nombreEquipo1Intercentros)
        nombreEquipo2Result = view.findViewById(R.id.equipo2Intercentros)
        logoEquipo1Result = view.findViewById(R.id.FotoLogoEquipo1Intercentros)
        logoEquipo2Result = view.findViewById(R.id.FotoLogoEquipo2Intercentros)
        BotonEquipo1 = view.findViewById(R.id.BotonEquipo1Intercentros)
        BotonEquipo2 = view.findViewById(R.id.BotonEquipo2Intercentros)
        raya1 = view.findViewById(R.id.rayitaEquipo1)
        raya2 = view.findViewById(R.id.rayitaEquipo2)
        marcadorEquipo1 = view.findViewById(R.id.GolEquipo1Intercentros)
        marcadorEquipo2 = view.findViewById(R.id.GolEquipo2Intercentros)
        botonGuardarDatos = view.findViewById(R.id.BotonFinalizarIntercentros)
        contadorAmarillaEquipo1 = view.findViewById(R.id.ContadorTarjetaAmarillaEquipo1Intercentros)
        contadorAmarillaEquipo2 = view.findViewById(R.id.ContadorTarjetaAmarillaEquipo2Intercentros)
        contadorRojaEquipo1 = view.findViewById(R.id.ContadorTarjetaRojaEquipo1Intercentros)
        contadorRojaEquipo2 = view.findViewById(R.id.ContadorTarjetaRojaEquipo2Intercentros)
        nombreEquipoSeleccionado = view.findViewById(R.id.nombreEquipoSeleccionadoIntercentros)
        botonVerPenales = view.findViewById(R.id.VerPenales);
        recyclerView = view.findViewById(R.id.recicleJugadorParticipantesIntercentros)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(requireActivity()).get(VerTarjetas::class.java)


        val resultadosService = RetrofitInstance.createService(ApiServiceAgregarResultadosIntercentros::class.java)
        presenter = AgregarResultadosIntercentrosPresenter(this, resultadosService)




        presenter.obtenerDatosIntercentros(idVs)



    }

    override fun onIntercentrosRecibidos(intercentros: EquiposIntercentrosAsignados) {
        this.Intercentros = intercentros
        Log.d("Fragment_resultados", "Datos resultados: $intercentros")


        nombreEquipo1Resultados.text = intercentros.equipo1.nombreEquipo

        nombreEquipo2Result.text = intercentros.equipo2.nombreEquipo
        BotonEquipo1.text = intercentros.equipo1.nombreEquipo
        BotonEquipo2.text = intercentros.equipo2.nombreEquipo
        Picasso.get().load(intercentros.equipo1.imgLogo).into(logoEquipo1Result)
        Picasso.get().load(intercentros.equipo1.imgLogo).into(logoEquipo2Result)

        BotonEquipo1.setOnClickListener {
            actualizarAdapter(obtenerParticipantesDelEquipo1(intercentros), 1)
            nombreEquipoSeleccionado.text = BotonEquipo1.text
        }
        BotonEquipo2.setOnClickListener {
            actualizarAdapter(obtenerParticipantesDelEquipo2(intercentros), 2)
            nombreEquipoSeleccionado.text = BotonEquipo2.text

        }
        actualizarAdapter(obtenerParticipantesDelEquipo1(intercentros), 1)
        nombreEquipoSeleccionado.text = BotonEquipo1.text
    }

    private fun actualizarAdapter(participantes: List<ParticipantesInter>, equipo: Int) {
        val idVs = arguments?.getString("idVs") ?: "Error"
        Log.d("Fragment_resultados", "ID VS: $idVs")

        adapter = AdapterAgregarResultadosIntercentros(participantes, viewModel,this, equipo ,idVs)
        recyclerView.adapter = adapter
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.zoom_in)
        recyclerView?.startAnimation(animation)
        actualizarContadoresGenerales(equipo,idVs)
    }
    private fun obtenerParticipantesDelEquipo1(resultados: EquiposIntercentrosAsignados): List<ParticipantesInter> {
        val pa = resultados.equipo1.participantes
        Log.d("pa", "Participantes : ${pa}")
        return  pa;
    }

    private fun obtenerParticipantesDelEquipo2(resultados: EquiposIntercentrosAsignados): List<ParticipantesInter> {
        return resultados.equipo2.participantes
    }
    fun actualizarContadoresGenerales(equipo: Int, idVs: String) {
        val idVs = arguments?.getString("idVs") ?: "Error"
        Log.d("Fragment_resultados", "ID VS: $idVs")

        viewModel.golesPorJugadorEquipo1PorVs.observe(viewLifecycleOwner, Observer { goles ->
            val totalGolesEquipo1 = viewModel.obtenerTotalGolesEquipo1(idVs)
            marcadorEquipo1.text = totalGolesEquipo1.toString()
            Log.d("ViewModel", "ID VS: $idVs, Equipo: $equipo, Total Goles Equipo 1: $totalGolesEquipo1")
        })

        viewModel.golesPorJugadorEquipo2PorVs.observe(viewLifecycleOwner, Observer { goles ->
            val totalGolesEquipo2 = viewModel.obtenerTotalGolesEquipo2(idVs)
            marcadorEquipo2.text = totalGolesEquipo2.toString()
            Log.d("ViewModel", "ID VS: $idVs, Equipo: $equipo, Total Goles Equipo 2: $totalGolesEquipo2")
        })

        viewModel.amarillasEquipo1.observe(viewLifecycleOwner, Observer { amarillas ->
            val totalAmarillasEquipo1 = viewModel.obtenerTotalAmarillasEquipo1(idVs)
            contadorAmarillaEquipo1.text = totalAmarillasEquipo1.toString()
            Log.d("ViewModel", "ID VS: $idVs, Equipo: $equipo, Total Amarillas Equipo 1: $totalAmarillasEquipo1")
        })

        viewModel.amarillasEquipo2.observe(viewLifecycleOwner, Observer { amarillas ->
            val totalAmarillasEquipo2 = viewModel.obtenerTotalAmarillasEquipo2(idVs)
            contadorAmarillaEquipo2.text = totalAmarillasEquipo2.toString()
            Log.d("ViewModel", "ID VS: $idVs, Equipo: $equipo, Total Amarillas Equipo 2: $totalAmarillasEquipo2")
        })

        viewModel.rojasEquipo1.observe(viewLifecycleOwner, Observer { rojas ->
            val totalRojasEquipo1 = viewModel.obtenerTotalRojasEquipo1(idVs)
            contadorRojaEquipo1.text = totalRojasEquipo1.toString()
            Log.d("ViewModel", "ID VS: $idVs, Equipo: $equipo, Total Rojas Equipo 1: $totalRojasEquipo1")
        })

        viewModel.rojasEquipo2.observe(viewLifecycleOwner, Observer { rojas ->
            val totalRojasEquipo2 = viewModel.obtenerTotalRojasEquipo2(idVs)
            contadorRojaEquipo2.text = totalRojasEquipo2.toString()
            Log.d("ViewModel", "ID VS: $idVs, Equipo: $equipo, Total Rojas Equipo 2: $totalRojasEquipo2")
        })
    }

    override fun error(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()

    }


}