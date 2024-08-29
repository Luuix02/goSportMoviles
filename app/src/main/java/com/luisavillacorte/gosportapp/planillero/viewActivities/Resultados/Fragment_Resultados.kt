package com.luisavillacorte.gosportapp.planillero.viewActivities.Resultados

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
import com.luisavillacorte.gosportapp.planillero.adpaters.api.vsEquiposResultados.ApiServiceResultados
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.Participante
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.ParticipanteAdapter
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.Resultados
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.ResultadosContract
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.ResultadosPresenter
import com.luisavillacorte.gosportapp.planillero.helper.VerTarjetas
import com.squareup.picasso.Picasso


class Fragment_Resultados : Fragment(), ResultadosContract.View {

    private lateinit var presenter: ResultadosPresenter
    private lateinit var adapter: ParticipanteAdapter
    private lateinit var recyclerView: RecyclerView

    private lateinit var ResultadosPrim: Resultados

    private lateinit var nombreEquipo1Resultados: TextView
    private lateinit var nombreEquipo2Result: TextView
    private lateinit var logoEquipo1Result: ImageView
    private lateinit var logoEquipo2Result: ImageView

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
    //private  lateinit var  BotonVerResultado : Button;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment__resultados, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idVs = arguments?.getString("idVs") ?: "Error"
        Log.d("Fragment_resultados", "ID VS: $idVs")

        nombreEquipo1Resultados = view.findViewById(R.id.nombreEquipo1Result)
        nombreEquipo2Result = view.findViewById(R.id.equipo2Result)
        logoEquipo1Result = view.findViewById(R.id.FotoLogoEquipo1Result)
        logoEquipo2Result = view.findViewById(R.id.FotoLogoEquipo2Result)
        BotonEquipo1 = view.findViewById(R.id.BotonEquipo1Result)
        BotonEquipo2 = view.findViewById(R.id.BotonEquipo2Result)
        raya1 = view.findViewById(R.id.rayitaEquipo1)
        raya2 = view.findViewById(R.id.rayitaEquipo2)
        marcadorEquipo1 = view.findViewById(R.id.GolEquipo1R)
        marcadorEquipo2 = view.findViewById(R.id.GolEquipo2R)
        botonGuardarDatos = view.findViewById(R.id.BotonFinalizar)
        contadorAmarillaEquipo1 = view.findViewById(R.id.ContadorTarjetaAmarillaEquipo1)
        contadorAmarillaEquipo2 = view.findViewById(R.id.ContadorTarjetaAmarillaEquipo2)
        contadorRojaEquipo1 = view.findViewById(R.id.ContadorTarjetaRojaEquipo1)
        contadorRojaEquipo2 = view.findViewById(R.id.ContadorTarjetaRojaEquipo2)
        nombreEquipoSeleccionado = view.findViewById(R.id.nombreEquipoSeleccionadoR)

        recyclerView = view.findViewById(R.id.recicleJugadorParticipantes)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(requireActivity()).get(VerTarjetas::class.java)


        val resultadosService = RetrofitInstance.createService(ApiServiceResultados::class.java)
        presenter = ResultadosPresenter(this, resultadosService)

        presenter.obtenerResultados(idVs)


    }

    override fun onResultadosRecibidos(resultados: Resultados) {
        this.ResultadosPrim = resultados
        Log.d("Fragment_resultados", "Datos resultados: $resultados")


        nombreEquipo1Resultados.text = resultados.equipo1.Equipo1.nombreEquipo
        nombreEquipo2Result.text = resultados.equipo2.Equipo2.nombreEquipo
        BotonEquipo1.text = resultados.equipo1.Equipo1.nombreEquipo
        BotonEquipo2.text = resultados.equipo2.Equipo2.nombreEquipo
        marcadorEquipo1.text = resultados.equipo1.goles.marcador.toString()
        marcadorEquipo2.text = resultados.equipo2.goles.marcador.toString()
        Picasso.get().load(resultados.equipo1.Equipo1.imgLogo).into(logoEquipo1Result)
        Picasso.get().load(resultados.equipo2.Equipo2.imgLogo).into(logoEquipo2Result)

        BotonEquipo1.setOnClickListener {
            actualizarAdapter(obtenerParticipantesDelEquipo1(resultados), 1)
            nombreEquipoSeleccionado.text = BotonEquipo1.text
        }
        BotonEquipo2.setOnClickListener {
            actualizarAdapter(obtenerParticipantesDelEquipo2(resultados), 2)
            nombreEquipoSeleccionado.text = BotonEquipo2.text

        }
        actualizarAdapter(obtenerParticipantesDelEquipo1(resultados), 1)
        nombreEquipoSeleccionado.text = BotonEquipo1.text
    }

    override fun error(error: String) {
        Log.e("Fragment_resultados", "Error al obtener resultados: $error")
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    private fun actualizarAdapter(participantes: List<Participante>, equipo: Int) {
        val idVs = arguments?.getString("idVs") ?: "Error"
        Log.d("Fragment_resultados", "ID VS: $idVs")

        adapter = ParticipanteAdapter(participantes, viewModel,this, equipo ,idVs)
        recyclerView.adapter = adapter
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.zoom_in)
        recyclerView?.startAnimation(animation)
        actualizarContadoresGenerales(equipo,idVs)
    }

    private fun obtenerParticipantesDelEquipo1(resultados: Resultados): List<Participante> {
        return resultados.equipo1.Equipo1.participantes
    }

    private fun obtenerParticipantesDelEquipo2(resultados: Resultados): List<Participante> {
        return resultados.equipo2.Equipo2.participantes
    }

    fun actualizarContadoresGenerales(equipo: Int, idVs:String) {

        val idVs = arguments?.getString("idVs") ?: "Error"
        Log.d("Fragment_resultados", "ID VS: $idVs")

        viewModel.golesPorJugadorEquipo1PorVs.observe(viewLifecycleOwner, Observer { goles ->
            marcadorEquipo1.text = viewModel.obtenerTotalGolesEquipo1(idVs).toString()
            Log.d("ViewModel", "ID VS: $idVs, Equipo: $equipo, Total Goles: ${viewModel.obtenerTotalGolesEquipo1(idVs)}")


        })

        viewModel.golesPorJugadorEquipo2PorVs.observe(viewLifecycleOwner, Observer { goles ->
            marcadorEquipo2.text = viewModel.obtenerTotalGolesEquipo2(idVs).toString()
            Log.d("ViewModel", "ID VS: $idVs, Equipo: $equipo, Total Goles: ${viewModel.obtenerTotalGolesEquipo2(idVs)}")
        })
        viewModel.amarillasEquipo1.observe(viewLifecycleOwner, Observer { goles ->
            contadorAmarillaEquipo1.text = viewModel.obtenerTotalAmarillasEquipo1(idVs).toString()
            Log.d("ViewModel", "ID VS: $idVs, Equipo: $equipo, Total Goles: ${viewModel.obtenerTotalAmarillasEquipo1(idVs)}")
        })
        viewModel.amarillasEquipo2.observe(viewLifecycleOwner, Observer { goles ->
            contadorAmarillaEquipo2.text = viewModel.obtenerTotalAmarillasEquipo2(idVs).toString()
            Log.d("ViewModel", "ID VS: $idVs, Equipo: $equipo, Total Goles: ${viewModel.obtenerTotalAmarillasEquipo2(idVs)}")

        })
        viewModel.rojasEquipo1.observe(viewLifecycleOwner, Observer { rojas ->
            contadorRojaEquipo1.text = viewModel.obtenerTotalRojasEquipo1(idVs).toString()
            Log.d("ViewModel", "ID VS: $idVs, Equipo: $equipo, Total Goles: ${viewModel.obtenerTotalRojasEquipo1(idVs)}")
        })
        viewModel.rojasEquipo2.observe(viewLifecycleOwner, Observer { rojas->
            contadorRojaEquipo2.text = viewModel.obtenerTotalRojasEquipo2(idVs).toString()
            Log.d("ViewModel", "ID VS: $idVs, Equipo: $equipo, Total Goles: ${viewModel.obtenerTotalRojasEquipo2(idVs)}")
        })

    }
}
