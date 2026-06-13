package com.kotlin.kotlin_flow_part_1

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class MainViewModel : ViewModel() {
    val countDownTimer = flow<Int> {
        val startingValue = 4
        emit(startingValue)
        var currentValue = startingValue
        while (currentValue > 0) {
            delay(1000.milliseconds)
            currentValue--
            emit(currentValue)
        }
    }

    init {
        getCountdownTime()
    }

    fun getCountdownTime() {
        viewModelScope.launch {

            val count = countDownTimer.reduce { accumulator, value ->
                Log.d("TAG", "getCountdownTime: $accumulator:$value")
                accumulator + value
            }
            Log.d("TAG", "reduce accumulator: $count")
        }
    }
}
