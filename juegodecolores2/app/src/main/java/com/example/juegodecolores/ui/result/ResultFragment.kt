package com.example.juegodecolores.ui.result

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.juegodecolores.R
import com.example.juegodecolores.adapter.ScoreAdapter
import com.example.juegodecolores.vm.GameViewModel
import com.google.android.material.button.MaterialButton

class ResultFragment : Fragment() {

    private val vm: GameViewModel by activityViewModels()
    private lateinit var prefs: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_result, container, false)

        val score = arguments?.getInt("score") ?: 0
        prefs = requireContext().getSharedPreferences("game_prefs", Context.MODE_PRIVATE)
        val best = prefs.getInt("best_score", 0)
        if (score > best) {
            prefs.edit().putInt("best_score", score).apply()
        }
        val bestNow = prefs.getInt("best_score", 0)

        val tvFinal = root.findViewById<TextView>(R.id.tv_final_score)
        val tvBest = root.findViewById<TextView>(R.id.tv_best_score)
        tvFinal.text = getString(R.string.final_score, score)
        tvBest.text = getString(R.string.best_score, bestNow)

        val rv = root.findViewById<RecyclerView>(R.id.rv_history)
        val adapter = ScoreAdapter()
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        vm.sessionHistory.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list.toList()) // enviar copia
        }

        val btnPlayAgain = root.findViewById<MaterialButton>(R.id.btn_play_again)
        btnPlayAgain.setOnClickListener {
            findNavController().navigate(R.id.action_game_to_result) // This is wrong - better navigate to game
            // Navigate back to game
            findNavController().navigate(R.id.gameFragment)
        }

        return root
    }
}
