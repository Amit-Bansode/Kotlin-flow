package com.kotlin.kotlin_flow_part_1

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class MainViewModel : ViewModel() {
    val countDownTimer = flow<Int> {
        val startingValue = 10
        emit(startingValue)
        var currentValue = startingValue
        while (currentValue > 0) {
            delay(1000L.milliseconds)
            currentValue--
            emit(currentValue)
        }
    }

    init {
        getCountdownTime()
    }

    fun getCountdownTime() {
        viewModelScope.launch {
            countDownTimer.collectLatest { time ->
                delay(1500L.milliseconds)
                Log.d("count", "getCountdownTime: $time")
            }
        }
    }
}