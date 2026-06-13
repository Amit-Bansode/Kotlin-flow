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
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class MainViewModel : ViewModel() {
    val countDownTimer = flow<Int> {
        val startingValue = 10
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

            val count = countDownTimer
                .filter {
                    it % 2 == 0
                }
                .map {
                    it * it
                }
                .onStart {
                    Log.d("count", "getCountdownTime: Started")
                    emit(-1)
                }
                .onEach {
                    Log.d("count", "getCountdownTime: onEach $it")
                }
                .onEmpty {
                    Log.d("count", "getCountdownTime: Empty")
                }
                .onCompletion {
                    Log.d("count", "getCountdownTime: Completed")
                }
                .count {
                    it % 2 == 0
                }
                // operator after count will not work

            Log.d("count", "getCountdownTime: $count")
        }


    }
}
