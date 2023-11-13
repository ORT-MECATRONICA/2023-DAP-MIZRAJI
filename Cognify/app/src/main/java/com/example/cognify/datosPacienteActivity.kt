
package com.example.cognify

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.github.mikephil.charting.data.Entry
import kotlin.properties.Delegates

/* 5LB
Como dice el nombre, aca se muestran los datos del paciente seleccionado
En la aplicacion de proyecto final muestra los entramientos, en esta version
solo al paciente que seleccionaste.
*/

private lateinit var database: FirebaseDatabase
private lateinit var usersReference: DatabaseReference

@SuppressLint("StaticFieldLeak")
lateinit var textView33:TextView

private lateinit var botonBorrar:Button


private lateinit var firebaseAuth: FirebaseAuth //Variable del firebase

@SuppressLint("StaticFieldLeak")
val db = FirebaseFirestore.getInstance() //Instanciamos la base de datos



class datosPacienteActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos_paciente)

        val keyPaciente = intent.getStringExtra("paqueteKeyPaciente")
        val keyID = intent.getStringExtra("paqueteKeyID")
        var flagBotonBorrar:Boolean = false

        botonBorrar = findViewById(R.id.botonBorrar)

        textView33 = findViewById(R.id.textView3)
        textView33.text = "Ultimos resultados de: $keyPaciente"

        database = FirebaseDatabase.getInstance()


        botonBorrar.setOnClickListener {
            if (!flagBotonBorrar) {
                Toast.makeText(
                    this, "¿Está seguro que quiere borrar " +
                            "$keyPaciente?", Toast.LENGTH_LONG
                ).show();
                flagBotonBorrar = true

            } else {
                val documentoAEliminar = db.collection("Pacientes").document(keyID.toString())
                // Borra el documento utilizando el método delete().
                documentoAEliminar
                    .delete()
                    .addOnSuccessListener {
                        // El documento se eliminó con éxito.
                        Toast.makeText(
                            this,
                            "$keyPaciente fue eliminado, reiniciando aplicación",
                            Toast.LENGTH_LONG
                        ).show();
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener { e ->
                        // Maneja cualquier error que pueda ocurrir durante la eliminación.
                        println("Error al eliminar el documento: $e")
                    }
            }
        }

    }

}
