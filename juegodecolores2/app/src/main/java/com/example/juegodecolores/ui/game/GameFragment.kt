package com.example.juegodecolores.ui.game

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.juegodecolores.R
import com.example.juegodecolores.vm.GameViewModel
import com.google.android.material.button.MaterialButton
import android.widget.TextView
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProvider 

class GameFragment : Fragment() {

    private lateinit var colorBox: FrameLayout
    private lateinit var scoreText: TextView
    private lateinit var timeText: TextView
    private lateinit var buttons: List<MaterialButton>
    private var currentColorName: String = ""
    private var score = 0
    private var timer: CountDownTimer? = null
    private val totalTimeMs = 30_000L

    private lateinit var soundPool: SoundPool
    private var soundCorrect = 0
    private var soundWrong = 0

    // shared ViewModel to keep session history
    private val vm: GameViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // SoundPool setup
        val attrs = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setAudioAttributes(attrs)
            .setMaxStreams(2)
            .build()
        // load raw resources (agregar archivos en res/raw: correct.mp3, wrong.mp3)
        soundCorrect = soundPool.load(requireContext(), R.raw.correct, 1)
        soundWrong = soundPool.load(requireContext(), R.raw.wrong, 1)
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_game, container, false)
        colorBox = root.findViewById(R.id.color_box)
        scoreText = root.findViewById(R.id.tv_score)
        timeText = root.findViewById(R.id.tv_time)

        val btnRed = root.findViewById<MaterialButton>(R.id.btn_red)
        val btnGreen = root.findViewById<MaterialButton>(R.id.btn_green)
        val btnBlue = root.findViewById<MaterialButton>(R.id.btn_blue)
        val btnYellow = root.findViewById<MaterialButton>(R.id.btn_yellow)
        buttons = listOf(btnRed, btnGreen, btnBlue, btnYellow)

        for (btn in buttons) {
            btn.setOnClickListener {
                handleAnswer(btn)
            }
        }

        startNewGame()
        return root
    }

    private fun handleAnswer(btn: MaterialButton) {
        val chosen = when (btn.id) {
            R.id.btn_red -> "red"
            R.id.btn_green -> "green"
            R.id.btn_blue -> "blue"
            R.id.btn_yellow -> "yellow"
            else -> ""
        }
        if (chosen == currentColorName) {
            score++
            scoreText.text = getString(R.string.score, score)
            // sonido correcto
            soundPool.play(soundCorrect, 1f, 1f, 0, 0, 1f)
            // animación pequeña
            val a = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_up)
            btn.startAnimation(a)
            nextColor()
        } else {
            // sonido incorrecto
            soundPool.play(soundWrong, 1f, 1f, 0, 0, 1f)
            // pequeña vibración visual: fade
            val a = AnimationUtils.loadAnimation(requireContext(), R.anim.shake)
            btn.startAnimation(a)
        }
    }

    private fun startNewGame() {
        score = 0
        scoreText.text = getString(R.string.score, score)
        startTimer()
        nextColor()
    }

    private fun startTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(totalTimeMs, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val sec = (millisUntilFinished / 1000).toInt()
                timeText.text = getString(R.string.time_left, sec)
            }
            override fun onFinish() {
                timeText.text = getString(R.string.time_left, 0)
                endGame()
            }
        }.start()
    }

    private fun nextColor() {
        // elegir color al azar
        val colors = listOf("red", "green", "blue", "yellow")
        val chosen = colors.random()
        currentColorName = chosen
        val colorRes = when (chosen) {
            "red" -> R.color.red
            "green" -> R.color.green
            "blue" -> R.color.blue
            "yellow" -> R.color.yellow
            else -> R.color.red
        }
        colorBox.setBackgroundColor(ContextCompat.getColor(requireContext(), colorRes))
    }

    private fun endGame() {
        timer?.cancel()
        // guardar en ViewModel (historial en sesión)
        vm.addScore(score)
        // pasar a ResultFragment con el puntaje (podemos usar bundle)
        val bundle = Bundle()
        bundle.putInt("score", score)
        findNavController().navigate(R.id.action_game_to_result, bundle)
    }
}
