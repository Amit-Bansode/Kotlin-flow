package com.kotlin.kotlin_flow_part_1

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class MainViewModel : ViewModel() {
    val countDownTimer = flow<Int> {
        val startingValue = 5
        emit(startingValue)
        var currentValue = startingValue
        while (currentValue > 0) {
            delay(1000.milliseconds)
            currentValue--
            emit(currentValue)
        }
    }

    val elements = flowOf(10, 20, 30)


    init {
//        getCountdownTime()
        getElements()
    }

    fun getCountdownTime() {
        viewModelScope.launch {
            //fold takes initial value and then accumulate with value
            val count = countDownTimer.fold(5) { accumulator, value ->
                Log.d("TAG", "getCountdownTime: $accumulator:$value")
                accumulator + value
            }
            Log.d("TAG", "reduce accumulator: $count")
        }
    }

    fun getElements() {
        viewModelScope.launch {
            val count = elements.scan(10) { accumulator, value ->
                delay(1000.milliseconds)
                Log.d("TAG", "getCountdownTime: $accumulator:$value")
                accumulator + value
            }.collect {
                Log.d("TAG", "getElements: $it")
            }
            Log.d("TAG", "reduce accumulator: $count")
        }
    }
}
