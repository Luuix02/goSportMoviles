package com.luisavillacorte.gosportapp.planillero.viewActivities.EquiposAsignadosPlanillero

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.common.apiRetrofit.RetrofitInstance
import com.luisavillacorte.gosportapp.planillero.adpaters.api.verEquipos.ApiServiceEquipos
import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.AdapterEquiposPlanillero
import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.EquiposAsignadosContract
import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.EquiposAsignadosPresenter
import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.Vs
import com.luisavillacorte.gosportapp.planillero.viewActivities.EditarCuenta.Fragment_Editar_Cuenta_Planillero
import com.luisavillacorte.gosportapp.planillero.viewActivities.Resultados.Fragment_Resultados
import com.luisavillacorte.gosportapp.planillero.viewActivities.verResultados.Fragment_Ver_Resultados


class PlanilleroFragment : AppCompatActivity(), EquiposAsignadosContract.View {

    private lateinit var presenterEquiposAsig: EquiposAsignadosPresenter
    private lateinit var recycleView: RecyclerView
    private  lateinit var botonIrEditarPerfil: ImageView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.fragment_planillero)

        recycleView = findViewById(R.id.recycleEquiposAsigandos)
        recycleView.layoutManager = LinearLayoutManager(this)
        botonIrEditarPerfil = findViewById(R.id.EditarCuentaPlanillero)

        val consumoApi = RetrofitInstance.createService(ApiServiceEquipos::class.java)
        presenterEquiposAsig = EquiposAsignadosPresenter(this, consumoApi)

        val IdFase = "66cf6b3196625be81736d7b9"
        presenterEquiposAsig.obtenerEquiposAsignados(IdFase)
        botonIrEditarPerfil.setOnClickListener {
            navegarEditarPerfil()
        }
    }

    private fun navegarEditarPerfil() {
        val fragmento = Fragment_Editar_Cuenta_Planillero()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_Result, fragmento)
            .addToBackStack(null)
            .commit()
    }

    override fun onEquposRecibidos(equiposAsignados: List<Vs>) {
        recycleView.adapter = AdapterEquiposPlanillero(equiposAsignados, object : AdapterEquiposPlanillero.OnItemClickListener {
//            override fun onItemClick(idVs: String) {
//                if (idVs.isNotEmpty()) {
//                    val fragment = Fragment_Resultados().apply {
//                        arguments = Bundle().apply {
//                            putString("idVs", idVs)
//                        }
//                    }
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.fragment_container_Result, fragment)
//                        .addToBackStack(null)
//                        .commit()
//                } else {
//                    Log.e("Fragment_Planillero", "ID proporcionado es vacío")
//                }
//            }

            override fun onVerResultados(idVs: String) {
                if (idVs.isNotEmpty()) {
                    val fragment = Fragment_Ver_Resultados().apply {
                        arguments = Bundle().apply {
                            putString("idVs", idVs)
                        }
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_Result, fragment)
                        .addToBackStack(null)
                        .commit()
                } else {
                    Log.e("Fragment_Planillero", "ID proporcionado es vacío")
                }
            }

            override fun OnAgregarResultados(idVs: String) {
                if (idVs.isNotEmpty()) {
                    val fragment = Fragment_Resultados().apply {
                        arguments = Bundle().apply {
                            putString("idVs", idVs)
                        }
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_Result, fragment)
                        .addToBackStack(null)
                        .commit()
                } else {
                    Log.e("Fragment_Planillero", "ID proporcionado es vacío")
                }
            }
        })
    }

    override fun error(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }
}