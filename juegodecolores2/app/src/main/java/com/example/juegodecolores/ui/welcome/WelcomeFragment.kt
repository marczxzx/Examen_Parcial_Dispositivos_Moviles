package com.example.juegodecolores.ui.welcome

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.juegodecolores.R
import com.google.android.material.button.MaterialButton

class WelcomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_welcome, container, false)
        val btnStart = root.findViewById<MaterialButton>(R.id.btn_start)
        btnStart.setOnClickListener {
            // mostrar reglas en AlertDialog antes de iniciar
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.welcome_title))
                .setMessage(getString(R.string.welcome_rules))
                .setPositiveButton("Comenzar") { _, _ ->
                    findNavController().navigate(R.id.action_welcome_to_game)
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
        return root
    }
}
