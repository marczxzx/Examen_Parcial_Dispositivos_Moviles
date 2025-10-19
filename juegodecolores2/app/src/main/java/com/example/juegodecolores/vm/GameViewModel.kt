package com.example.juegodecolores.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.juegodecolores.data.models.Score

class GameViewModel : ViewModel() {

    private val _sessionHistory = MutableLiveData<MutableList<Score>>(mutableListOf())
    val sessionHistory: LiveData<MutableList<Score>> = _sessionHistory

    fun addScore(points: Int) {
        _sessionHistory.value?.add(0, Score(points))
        // notify LiveData observers
        _sessionHistory.value = _sessionHistory.value
    }

    fun clearHistory() {
        _sessionHistory.value?.clear()
        _sessionHistory.value = _sessionHistory.value
    }
}

