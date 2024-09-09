package com.luisavillacorte.gosportapp.planillero.viewActivities.verResultados

import android.os.Bundle
import android.util.Log
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
import com.luisavillacorte.gosportapp.planillero.adpaters.api.vsEquiposResultados.ApiServiceResultados
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.Participante
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.Resultados
import com.luisavillacorte.gosportapp.planillero.adpaters.model.verResultados.PresenterVerResultados
import com.luisavillacorte.gosportapp.planillero.adpaters.model.verResultados.TarjetaGeneral
import com.luisavillacorte.gosportapp.planillero.adpaters.model.verResultados.TarjetaGeneralE2
import com.luisavillacorte.gosportapp.planillero.adpaters.model.verResultados.verResultadosContract
import com.squareup.picasso.Picasso


class Fragment_Ver_Resultados : Fragment(), verResultadosContract.View {

    private  lateinit var presenter : PresenterVerResultados
    private lateinit var equipo1VerResultados: TextView
    private lateinit var equipo2VerResultados: TextView
    private lateinit var logoEquipo1VerResultados: ImageView
    private lateinit var logoEquipo2VerResultados: ImageView
    private lateinit var marcadorEquipo1VerResultados: TextView
    private lateinit var marcadorEquipo2VerResultados: TextView
    private lateinit var cardTitleE1 : TextView;
    private lateinit var cardTitleE2 : TextView;

    private lateinit var ResultadosGet : Resultados

    private lateinit var TarjetaGeneralEquipo1: TarjetaGeneral;
    private lateinit var TarjetaGeneralEquipo2: TarjetaGeneralE2;

    private lateinit var recicleViewE1 : RecyclerView;
    private lateinit var recicleViewE2 : RecyclerView;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idVs = arguments?.getString("idVs") ?: "Error"
        Log.d("VER RESULTADOS", "ID: $idVs")

        equipo1VerResultados = view.findViewById(R.id.nombreEquipo1ResultVerResultados);
        equipo2VerResultados = view.findViewById(R.id.equipo2ResultVerResultados);
        logoEquipo1VerResultados= view.findViewById(R.id.FotoLogoEquipo1VerResult);
        logoEquipo2VerResultados = view.findViewById(R.id.FotoLogoEquipo2VerResult);
        marcadorEquipo1VerResultados = view.findViewById(R.id.GolEquipo1VerResultados);
        marcadorEquipo2VerResultados = view.findViewById(R.id.GolEquipo2VerResultados);
        cardTitleE1 = view.findViewById(R.id.card_titleE1);
        cardTitleE2 = view.findViewById(R.id.card_titleE2);


        recicleViewE1 = view.findViewById(R.id.RecicleViewGolesEquipo1)
        recicleViewE2 = view.findViewById(R.id.RecicleViewGolesEquipo2)
        recicleViewE1.layoutManager = LinearLayoutManager(requireContext());
        recicleViewE2.layoutManager = LinearLayoutManager(requireContext());

        val resultadosService = RetrofitInstance.createService(ApiServiceResultados::class.java);
        presenter = PresenterVerResultados(this, resultadosService);
        presenter.obtenerVerResultados(idVs)

    }
    fun agruparParticipantesConFaltas(participantes: List<Participante>): List<Participante> {
        return participantes.groupBy { it._id }
            .map { (id, participantes) ->
                Participante(
                    _id = id,
                    nombres = participantes.first().nombres,
                    ficha = participantes.first().ficha,
                    dorsal = participantes.first().dorsal
                )
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment__ver__resultados, container, false)
    }



    override fun onVerReultados(resultados: Resultados) {
        this.ResultadosGet = resultados;

        val participantesConFaltasAmarillas = agruparParticipantesConFaltas(resultados.equipo1.tarjetasAmarillas)
        val participantesConFaltasRojas = agruparParticipantesConFaltas(resultados.equipo1.tarjetasRojas)

        val participantesConFaltasAmarillas2 = agruparParticipantesConFaltas(resultados.equipo2.tarjetasAmarillas)
        val participantesConFaltasRojas2 = agruparParticipantesConFaltas(resultados.equipo2.tarjetasRojas)

        TarjetaGeneralEquipo1 = TarjetaGeneral(resultados, participantesConFaltasAmarillas, participantesConFaltasRojas)
        recicleViewE1.adapter = TarjetaGeneralEquipo1
        TarjetaGeneralEquipo2 = TarjetaGeneralE2(resultados, participantesConFaltasAmarillas2, participantesConFaltasRojas2)
        recicleViewE2.adapter = TarjetaGeneralEquipo2


        equipo1VerResultados.text = resultados.equipo1.Equipo1.nombreEquipo;
        equipo2VerResultados.text = resultados.equipo2.Equipo2.nombreEquipo;
        marcadorEquipo1VerResultados.text = resultados.equipo1.goles.marcador.toString();
        marcadorEquipo2VerResultados.text = resultados.equipo2.goles.marcador.toString();
        Picasso.get().load(resultados.equipo1.Equipo1.imgLogo).into(logoEquipo1VerResultados)
        Picasso.get().load(resultados.equipo2.Equipo2.imgLogo).into(logoEquipo2VerResultados)
        cardTitleE1.text = resultados.equipo1.Equipo1.nombreEquipo
        cardTitleE2.text = resultados.equipo2.Equipo2.nombreEquipo

    }

    override fun error(error: String) {
        Log.e("Fragment_resultados", "Error al obtener resultados: $error")
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }


}