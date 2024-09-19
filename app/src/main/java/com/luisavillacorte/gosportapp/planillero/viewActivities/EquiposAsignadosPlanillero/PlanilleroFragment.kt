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
import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.AdapterIntercentros
import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.EquiposAsignadosContract
import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.EquiposAsignadosPresenter
import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.EquiposIntercentrosAsignados
import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.Vs
import com.luisavillacorte.gosportapp.planillero.viewActivities.AgregarResultadosIntercenntros.Fragment_AgregarResultadosIntercentros
import com.luisavillacorte.gosportapp.planillero.viewActivities.EditarCuenta.Fragment_Editar_Cuenta_Planillero
import com.luisavillacorte.gosportapp.planillero.viewActivities.Resultados.Fragment_Resultados
import com.luisavillacorte.gosportapp.planillero.viewActivities.verResultados.Fragment_Ver_Resultados


class PlanilleroFragment : AppCompatActivity(), EquiposAsignadosContract.View {

    private lateinit var presenterEquiposAsig: EquiposAsignadosPresenter
    private lateinit var recycleView: RecyclerView
    private lateinit var recicleViewIntercentros : RecyclerView
    private lateinit var botonIrEditarPerfil: ImageView
    private lateinit var NombrePlanillero: TextView
    private var identificaion: String? = null
    private var id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.fragment_planillero)

        recycleView = findViewById(R.id.recycleEquiposAsigandos)
        recycleView.layoutManager = LinearLayoutManager(this)
        recicleViewIntercentros = findViewById(R.id.recycleEquiposAsigandosIntercentros)
        recicleViewIntercentros.layoutManager = LinearLayoutManager(this);
        botonIrEditarPerfil = findViewById(R.id.EditarCuentaPlanillero)
        NombrePlanillero = findViewById(R.id.NombrePlanillero)

        val consumoApi = RetrofitInstance.createService(ApiServiceEquipos::class.java)
        presenterEquiposAsig = EquiposAsignadosPresenter(this, this, consumoApi)

        // Obtener perfil y equipos asignados solo si la identificación no es nula
        identificaion?.let {
            presenterEquiposAsig.obtenerEquiposAsignados(it)
        } ?: Log.e("Fragment_Planillero", "Identificación es nula")

        id?.let {
            presenterEquiposAsig.obtenerEquiposIntercentros(it)
        } ?: Log.e("Fragment_Planillero", "Identificación es nula")

        // Cargar el perfil del usuario
        presenterEquiposAsig.obtenerPerfilUsuario()

        botonIrEditarPerfil.setOnClickListener {
            // Antes de realizar la transacción de fragmentos, verificar que la actividad no está siendo destruida
            if (!isFinishing && !isDestroyed) {
                navegarEditarPerfil()
            } else {
                Log.e("Fragment_Planillero", "No se puede navegar a Editar Perfil, actividad destruida")
            }
        }
    }

    private fun navegarEditarPerfil() {
        val fragmento = Fragment_Editar_Cuenta_Planillero()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_Result, fragmento)
            .addToBackStack(null)
            .commitAllowingStateLoss()  // Usar commitAllowingStateLoss si la actividad podría estar en un estado inconsistente
    }

    override fun onEquposRecibidos(equiposAsignados: List<Vs>) {
        recycleView.adapter = AdapterEquiposPlanillero(equiposAsignados, object : AdapterEquiposPlanillero.OnItemClickListener {
            override fun onVerResultados(idVs: String) {
                if (idVs.isNotEmpty()) {
                    // Verificar si la actividad sigue disponible antes de la transacción
                    if (!isFinishing && !isDestroyed) {
                        val fragment = Fragment_Ver_Resultados().apply {
                            arguments = Bundle().apply {
                                putString("idVs", idVs)
                            }
                        }
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_Result, fragment)
                            .addToBackStack(null)
                            .commitAllowingStateLoss()  // Permitir la pérdida de estado si la actividad está cerrándose
                    } else {
                        Log.e("Fragment_Planillero", "No se puede ver resultados, actividad destruida")
                    }
                } else {
                    Log.e("Fragment_Planillero", "ID proporcionado es vacío")
                }
            }

            override fun OnAgregarResultados(idVs: String) {
                if (idVs.isNotEmpty()) {
                    // Verificar si la actividad sigue disponible antes de la transacción
                    if (!isFinishing && !isDestroyed) {
                        val fragment = Fragment_Resultados().apply {
                            arguments = Bundle().apply {
                                putString("idVs", idVs)
                            }
                        }
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_Result, fragment)
                            .addToBackStack(null)
                            .commitAllowingStateLoss()  // Permitir la pérdida de estado si la actividad está cerrándose
                    } else {
                        Log.e("Fragment_Planillero", "No se puede agregar resultados, actividad destruida")
                    }
                } else {
                    Log.e("Fragment_Planillero", "ID proporcionado es vacío")
                }
            }
        })
    }
    override fun EquiposIntercentrosRecibidos(equiposIntercentros: List<EquiposIntercentrosAsignados>) {
        recicleViewIntercentros.adapter = AdapterIntercentros(equiposIntercentros, object : AdapterIntercentros.OnItemClickListener {
            override fun onVerResultados(idVs: String) {
                if (idVs.isNotEmpty()) {
                    // Verificar si la actividad sigue disponible antes de la transacción
                    if (!isFinishing && !isDestroyed) {
                        val fragment = Fragment_Ver_Resultados().apply {
                            arguments = Bundle().apply {
                                putString("idVs", idVs)
                            }
                        }
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_Result, fragment)
                            .addToBackStack(null)
                            .commitAllowingStateLoss()  // Permitir la pérdida de estado si la actividad está cerrándose
                    } else {
                        Log.e("Fragment_Planillero", "No se puede ver resultados, actividad destruida")
                    }
                } else {
                    Log.e("Fragment_Planillero", "ID proporcionado es vacío")
                }
            }

            override fun OnAgregarResultados(idVs: String) {
                if (idVs.isNotEmpty()) {
                    // Verificar si la actividad sigue disponible antes de la transacción
                    if (!isFinishing && !isDestroyed) {
                        val fragment = Fragment_AgregarResultadosIntercentros().apply {
                            arguments = Bundle().apply {
                                putString("idVs", idVs)
                            }
                        }
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_Result, fragment)
                            .addToBackStack(null)
                            .commitAllowingStateLoss()  // Permitir la pérdida de estado si la actividad está cerrándose
                    } else {
                        Log.e("Fragment_Planillero", "No se puede agregar resultados, actividad destruida")
                    }
                } else {
                    Log.e("Fragment_Planillero", "ID proporcionado es vacío")
                }
            }
        })

    }


    override fun PerfilPlanillero(perfil: PerfilUsuarioResponse) {
        // Asignar nombre y identificación del planillero
        NombrePlanillero.text = perfil.nombres
        identificaion = perfil.identificacion
        id= perfil.id


        // Volver a llamar a obtenerEquiposAsignados si la identificación no es nula
        identificaion?.let {
            presenterEquiposAsig.obtenerEquiposAsignados(it)
        } ?: Log.e("Fragment_Planillero", "Identificación es nula después de obtener perfil")

        id?.let {
            presenterEquiposAsig.obtenerEquiposIntercentros(it)
        } ?: Log.e("Fragment_Planillero", "Identificación es nula después de obtener perfil")

    }

    override fun error(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }

    // Asegurarse de limpiar recursos o detener procesos en onPause o onDestroy
    override fun onDestroy() {
        super.onDestroy()
        // Liberar recursos si es necesario
        Log.d("Fragment_Planillero", "PlanilleroFragment destruido")
    }
}
