package com.example.crazybottle2

/*
Possibles evolucions:

0) Guardar els textos en fitxers interns (Jason)
1) Calcular la velocitat peque acabi on volem
2) Crear sequencia de joc:
    - Primer pas: Tiro i toca persona + Acció
    - Segon pas: Toca segona persona
    - Completat o comodí (n'hi ha 3) i es pot repetir el segon pas

3) Incorporar sensor per tal que la botella es mogui (o no) si es mou el dispositiu
4) Poder baixar llibreries de jocs (accions) - Pagar?
5) Poder-se conectar a internet (API)

 */


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.crazybottle2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Model to interchange info with Fragments
        //val model = ViewModelProvider(this)[SharedViewModel::class.java]

        // Navigation bar
        navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }



    // Should be in the Main Activity to be seen by all fragments. TODO: Check it.
    class SharedViewModel : ViewModel() {
        var listOfSegments = listOf<String>("Xavi", "Inma")
        var listOfActions = listOf<String>("dona un peto a", "abraça a")

        fun readSegmentsFromText(s: Editable?) {
            listOfSegments = (s?.lines() ?: listOf<String>("Up", "Down"))
        }

        fun writeSegmentsText(): Editable? {
            val textReturn : Editable? = Editable.Factory().newEditable("");

            for (i in listOfSegments.indices) {
                textReturn?.append(listOfSegments[i])
                if (i<listOfSegments.size-1) textReturn?.appendLine()
            }
            return (textReturn)
        }


        fun readActionsFromText(s: Editable?) {
            listOfActions = (s?.lines() ?: listOf<String>("something", "else"))
        }

        fun writeActionsText(): Editable? {
            val textReturn : Editable? = Editable.Factory().newEditable("");

            for (i in listOfActions.indices) {
                textReturn?.append(listOfActions[i])
                if (i<listOfActions.size-1) textReturn?.appendLine()
            }
            return (textReturn)
        }


    }


}