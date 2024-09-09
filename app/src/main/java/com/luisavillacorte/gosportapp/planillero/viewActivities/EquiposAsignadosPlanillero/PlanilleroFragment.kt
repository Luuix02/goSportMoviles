package com.luisavillacorte.gosportapp.planillero.viewActivities.EquiposAsignadosPlanillero

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.common.apiRetrofit.RetrofitInstance
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.planillero.adpaters.api.EditarCuentaPlanillero.ApiServiceEdiarCuenta
import com.luisavillacorte.gosportapp.planillero.adpaters.api.verEquipos.ApiServiceEquipos
import com.luisavillacorte.gosportapp.planillero.adpaters.model.EditarCuentaPlanillero.PresenterEditarCuentaPlanillero
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
    private lateinit var botonIrEditarPerfil: ImageView
    private lateinit var NombrePlanillero: TextView
      private  var identificaion:String?= null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.fragment_planillero)

        recycleView = findViewById(R.id.recycleEquiposAsigandos)
        recycleView.layoutManager = LinearLayoutManager(this)
        botonIrEditarPerfil = findViewById(R.id.EditarCuentaPlanillero)
        NombrePlanillero = findViewById(R.id.NombrePlanillero)

        val consumoApi = RetrofitInstance.createService(ApiServiceEquipos::class.java)
        presenterEquiposAsig = EquiposAsignadosPresenter(this, this, consumoApi)

        // Llama a obtenerPerfilUsuario para cargar el perfil y los equipos asignados
        identificaion?.let { presenterEquiposAsig.obtenerEquiposAsignados(it) }
        Log.d("i","cc:${identificaion}")
        presenterEquiposAsig.obtenerPerfilUsuario()
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

    override fun PerfilPlanillero(perfil: PerfilUsuarioResponse) {
        NombrePlanillero.text = perfil.nombres
        identificaion = perfil.identificacion
        presenterEquiposAsig.obtenerEquiposAsignados(identificaion!!)
        Log.d("i","cc:${identificaion}")

    }

    override fun error(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }
}
