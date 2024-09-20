package com.luisavillacorte.gosportapp.planillero.viewActivities.Resultados

import android.app.AlertDialog
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
import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.Equipo
import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.Participantes
import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.Vs
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.CampeonatoGetNombre
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.DatosJugadorDestacado
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.EquipoR
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.Goles
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.InscripcionEquipos1
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.InscripcionEquipos2
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.JugadoresPenalesAdapter
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.Participante
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.ParticipanteAdapter
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.Resultados
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.ResultadosContract
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.ResultadosPresenter
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.UsuariosJugadorDestacado
import com.luisavillacorte.gosportapp.planillero.helper.VerTarjetas
import com.luisavillacorte.gosportapp.planillero.viewActivities.verResultados.Fragment_Ver_Resultados
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Fragment_Resultados : Fragment(), ResultadosContract.View {

    private lateinit var presenter: ResultadosPresenter
    private lateinit var adapter: ParticipanteAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var reciclePenalesEquipo1: RecyclerView
    private lateinit var reciclePenalesEquipo2: RecyclerView

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
    private  lateinit var  botonVerPenales : Button;

    private var IdCampeonato: String? = null
    private var NombreCampeonato: String? = null
    private var jugadorDestacado: UsuariosJugadorDestacado? = null
    private val jugadoresDestacadosList = mutableListOf<UsuariosJugadorDestacado>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment__resultados, container, false)
    }
    private fun obtenerPenalesParticipantesDelEquipo1(resultados: Vs): List<Participantes> {
        return resultados.equipo1.informacion.team1.Equipo.participantes // Ajusta según tu modelo de datos
    }

    private fun obtenerPenalesParticipantesDelEquipo2(resultados: Vs): List<Participantes> {
        return resultados.equipo2.informacion.team2.Equipo.participantes // Ajusta según tu modelo de datos
    }
    private fun mostrarPenales(participantesEquipoPenal1: List<Participantes>, participantesEquipoPenal2: List<Participantes>){
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.penales_subir_resultados_r_i, null)
        builder.setView(view)
        reciclePenalesEquipo1 = view.findViewById(R.id.reciclePenalesEquipo1)
        reciclePenalesEquipo1.layoutManager = LinearLayoutManager(requireContext())

        // Adaptador para Equipo 1
        val penalAdapterEquipo1 = JugadoresPenalesAdapter(participantesEquipoPenal1)
        reciclePenalesEquipo1.adapter = penalAdapterEquipo1

        // RecyclerView para Equipo 2
        reciclePenalesEquipo2 = view.findViewById(R.id.reciclePenalesEquipo2)
        reciclePenalesEquipo2.layoutManager = LinearLayoutManager(requireContext())

        // Adaptador para Equipo 2
        val penalAdapterEquipo2 = JugadoresPenalesAdapter(participantesEquipoPenal2)
        reciclePenalesEquipo2.adapter = penalAdapterEquipo2


        val tvGoalsEquipoA: TextView = view.findViewById(R.id.nombresEquipo1Penaless)
        val tvGoalsEquipoB: TextView = view.findViewById(R.id.nombresEquipo2Penales)
        val logoEquiopo1 : ImageView = view.findViewById(R.id.FotoLogoPenalesEquipo1);
        val logoEquiopo2 : ImageView = view.findViewById(R.id.FotoLogoPenalesEquipo2);


        tvGoalsEquipoA.text = ResultadosPrim.equipo1.informacion.team1.Equipo.nombreEquipo;
        tvGoalsEquipoB.text = ResultadosPrim.equipo2.informacion.team2.Equipo.nombreEquipo;
        Picasso.get().load(ResultadosPrim.equipo1.informacion.team1.Equipo.imgLogo).into(logoEquiopo1)
        Picasso.get().load(ResultadosPrim.equipo2.informacion.team2.Equipo.imgLogo).into(logoEquiopo2)


        val btnFinish: Button = view.findViewById(R.id.btn_finish)


        btnFinish.setOnClickListener {
            // Acciones al finalizar el marcador (ejemplo: guardar resultados)
            builder.create().dismiss()
        }

        // Mostrar el diálogo
        builder.create().show()

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
          botonVerPenales = view.findViewById(R.id.VerPenales);
        recyclerView = view.findViewById(R.id.recicleJugadorParticipantes)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(requireActivity()).get(VerTarjetas::class.java)

        val equipoGanador = viewModel.ganador(idVs)
        val equipoPerdedor = viewModel.perdedor(idVs)

        if (equipoGanador != null) {
            Log.d("EquipoGanador", "El equipo ganador es: ${equipoGanador.nombreEquipo}") // Ajusta el campo según tu clase de equipo
        } else {
            Log.d("EquipoGanador", "No hay equipo ganador.")
        }

        val resultadosService = RetrofitInstance.createService(ApiServiceResultados::class.java)
        presenter = ResultadosPresenter(this, resultadosService)


        botonVerPenales.setOnClickListener {
            val participantesEquipo1 = obtenerPenalesParticipantesDelEquipo1(ResultadosPrim)
            val participantesEquipo2 = obtenerPenalesParticipantesDelEquipo2(ResultadosPrim)
            mostrarPenales(participantesEquipo1, participantesEquipo2)

            equipoGanador?.let {
                mostrarGanador(it) // Puedes hacer lo que quieras con el equipo ganador
            }

            equipoPerdedor?.let {
                mostrarPerdedor(it) // Puedes hacer lo que quieras con el equipo perdedor
            }
        }

        botonGuardarDatos.setOnClickListener {
            val subirResultados = objetoEnviarResultados()
            Log.d("DatosJugadorDestacado", "Lista de jugadores destacados: $jugadoresDestacadosList")

            if (subirResultados != null && jugadoresDestacadosList.isNotEmpty()) {
                // Subimos los resultados
                presenter.subirDatosResultados(subirResultados)

                val datosJugadorDestacado = DatosJugadorDestacado(
                    jugadorDestacado = jugadoresDestacadosList,
                    Campeonato = NombreCampeonato.toString()
                )
                presenter.subirJugadorDestacado(datosJugadorDestacado)
            } else {
                Toast.makeText(requireContext(), "No se pudieron obtener los resultados o los datos del jugador destacado", Toast.LENGTH_SHORT).show()
            }
        }

        Log.d("NombreCam", "Nombre Campeonato ${NombreCampeonato}")

        viewModel.jugadoresDestacados.observe(viewLifecycleOwner, Observer { jugadoresDestacados ->
            val idsJugadoresDestacados = jugadoresDestacados.flatMap { it.value }.toSet()
            idsJugadoresDestacados.forEach { id ->
                presenter.obtenerDatosUsuario(id)
            }
        })
        presenter.obtenerResultados(idVs)

    }
    private fun mostrarGanador(equipo: Equipo) {
        Toast.makeText(requireContext(), "El equipo ganador es: ${equipo.nombreEquipo}", Toast.LENGTH_SHORT).show()
    }

    private fun mostrarPerdedor(equipo: Equipo) {
        Log.d("E","Equipo perdedor${equipo}")

    }
    override fun onJugadorDestacadoRecibido(jugador: UsuariosJugadorDestacado) {
        Log.d("jugador","juagdorrrrr ${jugador.nombres}")
        jugadoresDestacadosList.add(jugador)
    }

    override fun onCampeonatoRecibido(campeonato: CampeonatoGetNombre) {
        Log.d("cm","Campeonatooo  ${campeonato.nombreCampeonato}")
        NombreCampeonato = campeonato.nombreCampeonato;

    }

    override fun onJugadorDestacadoSubido(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }




    override fun onResultadosRecibidos(resultados: Vs) {
        this.ResultadosPrim = resultados
        Log.d("Fragment_resultados", "Datos resultados: $resultados")

        IdCampeonato = resultados.idCampeonato
        Log.d("IdCam", "Campeonato ${IdCampeonato}")
        presenter.obtenerCampeonato(IdCampeonato!!)


        nombreEquipo1Resultados.text = resultados.equipo1.informacion.team1.Equipo.nombreEquipo

        nombreEquipo2Result.text = resultados.equipo2.informacion.team2.Equipo.nombreEquipo
        BotonEquipo1.text = resultados.equipo1.informacion.team1.Equipo.nombreEquipo
        BotonEquipo2.text = resultados.equipo2.informacion.team2.Equipo.nombreEquipo
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


    private fun objetoEnviarResultados(): Resultados {
        val idVs = arguments?.getString("idVs") ?: "Error"


        val golesPorJugadorEquipo1 = viewModel.golesPorJugadorEquipo1PorVs.value?.get(idVs) ?: emptyMap()
        val golesPorJugadorEquipo2 = viewModel.golesPorJugadorEquipo2PorVs.value?.get(idVs) ?: emptyMap()
        val amarillasporJugadorEquipo1 = viewModel.amarillasEquipo1.value?.get(idVs) ?: emptyMap()
        val rojasPoJugadorEquipo1 = viewModel.rojasEquipo1.value?.get(idVs) ?: emptyMap()
        val amarillasPorJugadorEquipo2 = viewModel.amarillasEquipo2.value?.get(idVs) ?: emptyMap();
        val rojasPorJugadorEquipo2 = viewModel.rojasEquipo2.value?.get(idVs) ?: emptyMap();
       val jugadoresGoleadoresEquipo1 = golesPorJugadorEquipo1.map { (jugadorId, goles) ->
            val participante = ResultadosPrim.equipo1.informacion.team1.Equipo?.participantes?.find { it._id == jugadorId }
            participante?.let {
                Participante(
                    _id = it._id,
                    nombres = it.nombres,
                    ficha = it.ficha,
                    dorsal = it.dorsal,
                    totalGoles = goles, // Añade el total de goles aquí
                    amarillas =  it.amarillas
                )
            }
        }.filterNotNull().groupBy { it._id }.map { (_, participantes) ->
            val participante = participantes.first()
            participante.copy(totalGoles = participantes.sumOf { it.totalGoles })
        }

        val jugadoresGoleadoresEquipo2 = golesPorJugadorEquipo2.map { (jugadorId, goles) ->
            val participante = ResultadosPrim.equipo2.informacion.team2.Equipo?.participantes?.find { it._id == jugadorId }
            participante?.let {
                Participante(
                    _id = it._id,
                    nombres = it.nombres,
                    ficha = it.ficha,
                    dorsal = it.dorsal,
                    totalGoles = goles,
                    amarillas = it.amarillas
                )
            }
        }.filterNotNull().groupBy { it._id }.map { (_, participantes) ->
            val participante = participantes.first()
            participante.copy(totalGoles = participantes.sumOf { it.totalGoles })
        }

        val tarjetasAmarillasEquipo1 = amarillasporJugadorEquipo1.map { (jugadorId, amarillas) ->
            val participante = ResultadosPrim.equipo1.informacion.team1.Equipo?.participantes?.find { it._id == jugadorId }
            participante?.let {
                Participante(
                    _id = it._id,
                    nombres = it.nombres,
                    ficha = it.ficha,
                    dorsal = it.dorsal,
                    totalGoles = it.totalGoles,  // No modificamos los goles en esta parte
                    amarillas = amarillas         // Aquí asignamos el número de amarillas
                )
            }
        }.filterNotNull().groupBy { it._id }.map { (_, participantes) ->
            val participante = participantes.first()
            participante.copy(amarillas = participantes.sumOf { it.amarillas }) // Sumar las amarillas
        }
        val tarjetasAmarillasEquipo2Datos = amarillasPorJugadorEquipo2.map { (jugadorId, amarillas) ->
           val participante = ResultadosPrim.equipo2.informacion.team2.Equipo?.participantes?.find{ it._id == jugadorId }
            participante?.let {
                Participante(
                    _id = it._id,
                    nombres = it.nombres,
                    ficha = it.ficha,
                    dorsal = it.dorsal,
                    totalGoles = it.totalGoles,
                    amarillas = amarillas

                )
            }
        }.filterNotNull().groupBy { it._id }.map { (_, participantes) ->
            val participante = participantes.first()
            participante.copy(amarillas = participantes.sumOf { it.amarillas }) // Sumar las amarillas
        }
        val tarjetasRojasEquipo1 = rojasPoJugadorEquipo1.filter { it.value > 0 }.mapNotNull { (jugadorId, _) ->
            ResultadosPrim.equipo1.informacion.team1.Equipo?.participantes?.find { it._id == jugadorId }?.let { jugador ->
                Participante(
                    _id = jugador._id,
                    nombres = jugador.nombres,
                    ficha = jugador.ficha,
                    dorsal = jugador.dorsal,
                    totalGoles = jugador.totalGoles,
                    amarillas = jugador.amarillas

                )
            }
        } ?: emptyList()
        val tarjetasRojasEquipo2Datos = rojasPorJugadorEquipo2.filter { it.value > 0 }.mapNotNull { (jugadorId, _) ->
            ResultadosPrim.equipo2.informacion.team2.Equipo?.participantes?.find { it._id == jugadorId }
                ?.let { jugador ->
                    Participante(
                        _id = jugador._id,
                        nombres = jugador.nombres,
                        ficha = jugador.ficha,
                        dorsal = jugador.dorsal,
                        totalGoles = jugador.totalGoles,
                        amarillas = jugador.amarillas

                    )
                }
        } ?: emptyList();
        Log.d("TarjetasAmarillas", "Equipo 1: $tarjetasRojasEquipo1")

        Log.d("TarjetasRojas", "Equipo 1: $tarjetasRojasEquipo1")


        val equipo1 = ResultadosPrim.equipo1.informacion.team1.Equipo?.let {
            EquipoR(
                _id = ResultadosPrim._id ?: "",
                nombreEquipo = it.nombreEquipo,
                nombreCapitan = it.nombreCapitan,
                contactoUno = it.contactoUno,
                contactoDos = it.contactoDos,
                jornada = it.jornada,
                cedula = it.cedula,
                imgLogo = it.imgLogo,
                estado = true,
                participantes = it.participantes.map { participante ->
                    Participante(
                        _id = participante._id,
                        nombres = participante.nombres,
                        ficha = participante.ficha,
                        dorsal = participante.dorsal,
                        totalGoles = participante.totalGoles,
                        amarillas = participante.amarillas
                    )
                }
            )
        }?.let {
            InscripcionEquipos1(
                Equipo1 = it,
                tarjetasAmarillas = tarjetasAmarillasEquipo1,
                tarjetasRojas = tarjetasRojasEquipo1,
                goles = Goles(
                    marcador = marcadorEquipo1.text.toString().toInt(),
                    jugadorGoleador = jugadoresGoleadoresEquipo1,
                )
            )
        }


        val equipo2 = ResultadosPrim.equipo2.informacion.team2.Equipo?.let {
            EquipoR(
                _id = ResultadosPrim._id ?: "",
                nombreEquipo = it.nombreEquipo,
                nombreCapitan = it.nombreCapitan,
                contactoUno = it.contactoUno,
                contactoDos = it.contactoDos,
                jornada = it.jornada,
                cedula = it.cedula,
                imgLogo = it.imgLogo,
                estado = true,
                participantes = it.participantes.map { participante ->
                    Participante(
                        _id = participante._id,
                        nombres = participante.nombres,
                        ficha = participante.ficha,
                        dorsal = participante.dorsal,
                        totalGoles = participante.totalGoles,
                        amarillas = participante.amarillas

                    )
                }
            )
        }?.let {
            InscripcionEquipos2(
                Equipo2 = it,
                tarjetasAmarillas = tarjetasAmarillasEquipo2Datos, // Aquí puedes añadir lógica para las tarjetas amarillas
                tarjetasRojas = tarjetasRojasEquipo2Datos, // Aquí puedes añadir lógica para las tarjetas rojas
                goles = Goles(
                    marcador = marcadorEquipo2.text.toString().toInt(),
                    jugadorGoleador = jugadoresGoleadoresEquipo2,
                )
            )
        }
        val idCampeonato = ResultadosPrim.idCampeonato
        Log.d("idCam","Id campeonnato ${idCampeonato}")
        val resultados = if (equipo1 != null && equipo2 != null) {
            Resultados(
                equipo1 = equipo1,
                equipo2 = equipo2,
                IdVs = idVs,
                estadoPartido = false,
                idCampeonato =idCampeonato,
                penales = false
            )
        } else {
            null
        }

        if (resultados != null) {
            Log.d("RESULTADOS subidos", "Resultados: $resultados")
        } else {
            Log.d("RESULTADOS subidos", "No se generaron resultados")
        }

        return resultados!!

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