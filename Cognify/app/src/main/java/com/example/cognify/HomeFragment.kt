package com.example.cognify

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import clases.Pacientes
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore

// Este fragment es para seleccionar un paciente del recyclerView

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    private lateinit var botonAgregarPaciente: Button

    private lateinit var agregarNuevoPaciente:EditText

    private var flagX = false


    //Variables Recycler View
    private var listapacientes: MutableList<Pacientes> = mutableListOf()
    private lateinit var recycler: RecyclerView

    //Firebase
    private lateinit var firebaseAuth: FirebaseAuth //Variable del firebase
    val db = FirebaseFirestore.getInstance() //Instanciamos la base de datos
    private lateinit var database: FirebaseDatabase
    private lateinit var usersReference: DatabaseReference



    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Convertimos el "return inflater..." en un val view, al final retornamos view
        val view = inflater.inflate(R.layout.fragment_home3, container, false)

        database = FirebaseDatabase.getInstance()

        //Asociamos variables
        botonAgregarPaciente = view.findViewById(R.id.botonAgregarPaciente)
        recycler = view.findViewById(R.id.listaRecycle)

        agregarNuevoPaciente = view.findViewById(R.id.agregarNuevoPaciente)


        botonAgregarPaciente.setOnClickListener {
            agregarNuevoPaciente.visibility = View.VISIBLE
            agregarNuevoPaciente.requestFocus()

            botonAgregarPaciente.text = "Confirmar"

            if (agregarNuevoPaciente.text.isNotEmpty()) {

                val nombreIngresado = agregarNuevoPaciente.text.toString()

                if (listapacientes.contains(Pacientes(nombreIngresado))) {
                    Toast.makeText(getActivity(), "Ya existe ese paciente", Toast.LENGTH_LONG)
                        .show();
                } else {

                    val usuariosCollection = db.collection("Pacientes")

                    val nuevoUsuario = hashMapOf(
                        "nombre" to nombreIngresado
                    )

                    usuariosCollection
                        .add(nuevoUsuario) // Puedes usar .set() si deseas especificar un ID personalizado.
                        .addOnSuccessListener { documentReference ->
                            // Los datos se subieron con éxito.
                            //Toast.makeText(getActivity(), "$idDelNuevoDocumento", Toast.LENGTH_LONG).show();
                            println("Documento subido con ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            // Maneja cualquier error que pueda ocurrir durante la subida.
                            println("Error al subir datos a Firestore: $e")
                        }

                    listapacientes.add(Pacientes(nombreIngresado))
                    establecerAdapter()
                    Toast.makeText(getActivity(), "Se agregó paciente", Toast.LENGTH_LONG).show();

                    agregarNuevoPaciente.visibility = View.GONE
                    botonAgregarPaciente.text = "Agregar paciente"

                }
            }
            else {
                Toast.makeText(getActivity(), "Complete el campo de texto", Toast.LENGTH_LONG).show();
            }

            agregarNuevoPaciente.text.clear()

        }

        //Accedemos al documento a leer
        val docRef = db.collection("Pacientes")//.document("Paciente 1")
        docRef.get().addOnSuccessListener { documents -> //"Si todo esta bien"
            for (document in documents){ //"Si el documento existe"

                if (!flagX) {
                    val listaPacientesFiltrada = mutableListOf<String>()
                    val nombreUsuario = document.get("nombre") as String

                    if (!listapacientes.contains(Pacientes(nombreUsuario))) {
                        listapacientes.add(Pacientes(nombreUsuario))

                    }

                }

            }
            establecerAdapter()
        }
            .addOnFailureListener() { exception ->
                Log.d("TAG", "Error al obtener $exception") //Otro error
            }

        establecerAdapter()





        return view //Retornamos el view antes mencionado
    }


    private fun establecerAdapter() {
        recycler.layoutManager = LinearLayoutManager(getActivity())
        recycler.adapter = context?.let { RecyclePacientes(it, listapacientes) }
    }

}





