package com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.registerFragments

import android.app.DatePickerDialog
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class RegisterTwoFragment : Fragment() {
    private lateinit var presenter: RegisterContract.Presenter
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private lateinit var editTextFinFicha: EditText


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_two, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)

        editTextFinFicha = view.findViewById(R.id.fin_ficha)
        val btnRegiter = view.findViewById<Button>(R.id.boton_registro)

        val saveData = (requireActivity() as RegisterActivity).getSavedData()
        view.findViewById<EditText>(R.id.num_ficha).setText(saveData["ficha"])
        editTextFinFicha.setText(saveData["finFicha"])
        view.findViewById<EditText>(R.id.correo).setText(saveData["correo"])
        view.findViewById<EditText>(R.id.contrasena).setText(saveData["contrasena"])
        view.findViewById<EditText>(R.id.confirmar_contrasena)
            .setText(saveData["confirmarContrasena"])

        editTextFinFicha.setOnClickListener {
            showDatePickerDialog()
        }

        btnRegiter.setOnClickListener {
            val numFicha = view.findViewById<EditText>(R.id.num_ficha).text.toString()
            val finFicha = editTextFinFicha.text.toString()
            val correo = view.findViewById<EditText>(R.id.correo).text.toString()
            val contrasena = view.findViewById<EditText>(R.id.contrasena).text.toString()
            val confirmarContrasena =
                view.findViewById<EditText>(R.id.confirmar_contrasena).text.toString()

            if (!ValidationUser.fieldsComplete(numFicha) ||
                !ValidationUser.fieldsComplete(finFicha) ||
                !ValidationUser.fieldsComplete(correo) ||
                !ValidationUser.fieldsComplete(contrasena) ||
                !ValidationUser.fieldsComplete(confirmarContrasena)
            ) {
                Toast.makeText(
                    requireContext(),
                    "Por favor complete todos los campos",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (!ValidationUser.validateEmail(correo)) {
                Toast.makeText(requireContext(), "Correo electrónico inválido", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            if (!ValidationUser.validatePassword(contrasena)) {
                Toast.makeText(requireContext(), "Contraseña inválida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!ValidationUser.passwordConfimated(contrasena, confirmarContrasena)) {
                Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            (requireActivity() as RegisterActivity).saveSecondSectionData(
                numFicha,
                finFicha,
                correo,
                contrasena
            )

            presenter.checkEmailExists(correo) { emailExists ->
                if (emailExists) {
                    Toast.makeText(
                        requireContext(),
                        "El correo electrónico ya está existe",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    (requireActivity() as RegisterActivity)
                        .saveSecondSectionData(
                            numFicha, finFicha, correo, contrasena
                        )


                    val data = mapOf(
                        "num_ficha" to numFicha,
                        "fin_ficha" to finFicha,
                        "correo" to correo,
                        "contrasena" to contrasena
                    )
                    presenter.saveSecondSectionData(data)
                    presenter.registerUser()
                }
            }


        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                editTextFinFicha.setText(dateFormat.format(selectedDate.time))
            },
            year,
            month,
            day

        )
        datePickerDialog.show()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        presenter = (context as RegisterActivity).presenter
    }

    override fun onPause() {
        super.onPause()
        saveData()
    }

    private fun saveData() {
        val numFicha = view?.findViewById<EditText>(R.id.num_ficha)?.text.toString()
        val finFicha = editTextFinFicha.text.toString()
        val correo = view?.findViewById<EditText>(R.id.correo)?.text.toString()
        val contrasena = view?.findViewById<EditText>(R.id.contrasena)?.text.toString()

        (requireActivity() as RegisterActivity).saveSecondSectionData(
            numFicha,
            finFicha,
            correo,
            contrasena
        )

    }

}