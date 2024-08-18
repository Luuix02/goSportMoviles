package com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.registerFragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.jugador.adapters.model.register.RegisterContract
import com.luisavillacorte.gosportapp.jugador.util.ValidationUser
import com.luisavillacorte.gosportapp.jugador.viewActivities.activities.activitiesAuth.RegisterActivity


class RegisterOneFragment : Fragment()  {
    private lateinit var presenter: RegisterContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_one, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val saveData = (requireActivity() as RegisterActivity).getSavedData()
        view.findViewById<EditText>(R.id.nombres_completos_edit_text).setText(saveData["nombres"])
        view.findViewById<EditText>(R.id.identificacion_edit_text).setText(saveData["identificacion"])
        view.findViewById<EditText>(R.id.telefono_edit_text).setText(saveData["telefono"])
        view.findViewById<EditText>(R.id.jornada_edit_text).setText(saveData["jornada"])
        view.findViewById<EditText>(R.id.programa_formacion_edit_text).setText(saveData["programa"])


        val btnNext = view.findViewById<Button>(R.id.btn_next)
        btnNext.setOnClickListener {
            val nombres = view.findViewById<EditText>(R.id.nombres_completos_edit_text).text.toString()
            val identificacion = view.findViewById<EditText>(R.id.identificacion_edit_text).text.toString()
            val celular = view.findViewById<EditText>(R.id.telefono_edit_text).text.toString()
            val jornada = view.findViewById<EditText>(R.id.jornada_edit_text).text.toString()
            val programa = view.findViewById<EditText>(R.id.programa_formacion_edit_text).text.toString()

            if (!ValidationUser.fieldsComplete(nombres)||
                !ValidationUser.fieldsComplete(identificacion)||
                !ValidationUser.fieldsComplete(celular)||
                !ValidationUser.fieldsComplete(jornada)||
                !ValidationUser.fieldsComplete(programa)) {
                Toast.makeText(requireContext(), "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!ValidationUser.validatePhone(celular)){
                Toast.makeText(requireContext(), "Por favor ingrese un número de teléfono válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            presenter.checkIdentificationExists(identificacion) { exists ->
                if (exists) {
                    Toast.makeText(requireContext(), "Número de identificación ya existe", Toast.LENGTH_SHORT).show()
                } else {
                    (requireContext() as RegisterActivity).saveFirstSectionData(nombres, identificacion, celular, jornada, programa)
                    val fragment = RegisterTwoFragment()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit()

                    val data = mapOf(
                        "nombres" to nombres,
                        "identificacion" to identificacion,
                        "celular" to celular,
                        "jornada" to jornada,
                        "programa" to programa

                    )
                    presenter.saveFirstSectionData(data)
                }
            }




//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, RegisterTwoFragment())
//                .addToBackStack(null)
//                .commit()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        presenter = (context as RegisterActivity).presenter
    }

}