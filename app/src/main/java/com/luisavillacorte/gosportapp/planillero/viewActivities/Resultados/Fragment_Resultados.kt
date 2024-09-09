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
import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.Participantes
import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.Vs
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.EquipoR
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.Goles
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.InscripcionEquipos1
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.InscripcionEquipos2
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

    //private lateinit var ResultadosPrim: Resultados
    private lateinit var ResultadosPrim: Vs
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

        botonGuardarDatos.setOnClickListener {
            val subirResultados = objetoEnviarResultados();
            if (subirResultados != null) {
                presenter.subirDatosResultados(subirResultados)
            };
            Toast.makeText(requireContext(), "Resultados guardados correctamente", Toast.LENGTH_SHORT).show()
        }


    }

    override fun onResultadosRecibidos(resultados: Vs) {
        this.ResultadosPrim = resultados
        Log.d("Fragment_resultados", "Datos resultados: $resultados")


        nombreEquipo1Resultados.text = resultados.equipo1.informacion.team1.Equipo.nombreEquipo
        nombreEquipo2Result.text = resultados.equipo2.informacion.team2.Equipo.nombreEquipo
        BotonEquipo1.text = resultados.equipo1.informacion.team1.Equipo.nombreEquipo
        BotonEquipo2.text = resultados.equipo2.informacion.team2.Equipo.nombreEquipo
//        marcadorEquipo1.text = resultados.equipo1.goles.marcador.toString()
//        marcadorEquipo2.text = resultados.equipo2.goles.marcador.toString()
        Picasso.get().load(resultados.equipo1.informacion.team1.Equipo.imgLogo).into(logoEquipo1Result)
        Picasso.get().load(resultados.equipo2.informacion.team2.Equipo.imgLogo).into(logoEquipo2Result)

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

    override fun messageExito(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    private fun objetoEnviarResultados(): Resultados? {
        val participantesAmarillasEquipo1 = listOf<Participante>() // Aquí deberías cargar los participantes reales
        val participantesRojasEquipo1 = listOf<Participante>() // Aquí también los participantes reales

        val participantesAmarillasEquipo2 = listOf<Participante>() // Lo mismo para el equipo 2
        val participantesRojasEquipo2 = listOf<Participante>() // Lo mismo para el equipo 2

        // Crear el objeto equipo1
        val equipo1 = ResultadosPrim._id?.let {
            EquipoR(
                _id = it, // Asigna el ID real del equipo 1
                nombreEquipo = ResultadosPrim.equipo1.informacion.team1.Equipo.nombreEquipo,
                nombreCapitan = ResultadosPrim.equipo1.informacion.team1.Equipo.nombreCapitan,
                contactoUno =  ResultadosPrim.equipo1.informacion.team1.Equipo.contactoUno,
                contactoDos =  ResultadosPrim.equipo1.informacion.team1.Equipo.contactoDos,
                jornada =  ResultadosPrim.equipo1.informacion.team1.Equipo.jornada,
                cedula =  ResultadosPrim.equipo1.informacion.team1.Equipo.cedula,
                imgLogo = ResultadosPrim.equipo1.informacion.team1.Equipo.imgLogo,
                estado = true,
                participantes = ResultadosPrim.equipo1.informacion.team1.Equipo.participantes.map { participante ->
                    Participante(
                        _id = participante._id,
                        nombres = participante.nombres,
                        ficha = participante.ficha,
                        dorsal = participante.dorsal
                    )
                })
        }?.let {
            InscripcionEquipos1(
                Equipo1 = it,
                tarjetasAmarillas = participantesAmarillasEquipo1,
                tarjetasRojas = participantesRojasEquipo1,
                goles = Goles(
                    marcador = marcadorEquipo1.text.toString().toInt(),
                    jugadorGoleador = listOf()
                )
            )
        }

        // Crear el objeto equipo2
        val equipo2 = InscripcionEquipos2(
            Equipo2 = EquipoR(
                _id = "ID_Equipo2", // Asigna el ID real del equipo 2
                nombreEquipo = "Nombre del Equipo 2",
                nombreCapitan = "Capitán Equipo 2",
                contactoUno = "Contacto 1",
                contactoDos = "Contacto 2",
                jornada = "Jornada del partido",
                cedula = "Cédula equipo",
                imgLogo = "URL del logo del equipo 2",
                estado = true,
                participantes = listOf() // Lista de participantes del equipo 2
            ),
            tarjetasAmarillas = participantesAmarillasEquipo2,
            tarjetasRojas = participantesRojasEquipo2,
            goles = Goles(
                marcador = marcadorEquipo2.text.toString().toInt(),
                jugadorGoleador = listOf() // Lista de goleadores del equipo 2
            )
        )
        val resultado = equipo1?.let {
            Resultados(
                equipo1 = it,
                equipo2 = equipo2,
                IdVs = "ID del VS",
                estadoPartido = true
            )
        }
      Log.d("TAG","Resultados ${resultado}")
        // Devolver el objeto Resultados completo
        return resultado
    }
    private fun actualizarAdapter(participantes: List<Participantes>, equipo: Int) {
        val idVs = arguments?.getString("idVs") ?: "Error"
        Log.d("Fragment_resultados", "ID VS: $idVs")

        adapter = ParticipanteAdapter(participantes, viewModel,this, equipo ,idVs)
        recyclerView.adapter = adapter
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.zoom_in)
        recyclerView?.startAnimation(animation)
        actualizarContadoresGenerales(equipo,idVs)
    }

    private fun obtenerParticipantesDelEquipo1(resultados: Vs): List<Participantes> {
        val pa = resultados.equipo1.informacion.team1.Equipo.participantes
        Log.d("pa", "Participantes : ${pa}")
        return  pa;
    }

    private fun obtenerParticipantesDelEquipo2(resultados: Vs): List<Participantes> {
        return resultados.equipo2.informacion.team2.Equipo.participantes
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

}