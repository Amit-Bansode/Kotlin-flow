package com.kotlin.kotlin_flow_part_1

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
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
    val flow1 = (1..5).asFlow()
    val flow2 = (3..4).asFlow()

    init {
//        getCountdownTime()
        getElements()
        //flow transform
        getCombinedFlow()
        //flow filter
        collectFlow()
    }

    fun collectFlow() {
        val flow = flow {
            delay(1000L)
            emit("Appetizer")
            delay(1500L)
            emit("Main dish")
            delay(500L)
            emit("Dessert")
        }
        //Buffer -> buffer the emitted items, so that processing is not blocked. operators like buffer to preserve the emitted items. and to process sequentially
        //Conflate -> only the last value is processed
        //CollectLatest -> only the last value is processed

        viewModelScope.launch {
            flow.onEach {
                Log.d("TAG", "collectFlow: $it is delivered")
            }
                .collectLatest {
                    Log.d("TAG", "collectFlow: Now eating $it")
                    delay(5000L)
                    Log.d("TAG", "collectFlow: Finished eating $it")
                }
        }


    }


    fun getCombinedFlow() {
        viewModelScope.launch {
            //flatMapMerge
            //flatMapConcat
            flow1.flatMapLatest { f1 ->
                flow {
                    emit(f1)
                    Log.d("TAG", "flatMapConcat: Start $f1")
                    delay(1000.milliseconds)
                    emit(f1)
                    Log.d("TAG", "flatMapConcat:End $f1")
                }
            }.collect {
                Log.d("TAG", "getCombinedFlow: $it")
            }
        }
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
//Conflate
//2026-06-16 18:00:57.247 24187-24187 TAG                     com.kotlin.kotlin_flow_part_1        D  collectFlow: Appetizer is delivered
//2026-06-16 18:00:57.247 24187-24187 TAG                     com.kotlin.kotlin_flow_part_1        D  collectFlow: Now eating Appetizer
//2026-06-16 18:00:58.748 24187-24187 TAG                     com.kotlin.kotlin_flow_part_1        D  collectFlow: Main dish is delivered
//2026-06-16 18:00:59.249 24187-24187 TAG                     com.kotlin.kotlin_flow_part_1        D  collectFlow: Dessert is delivered
//2026-06-16 18:01:02.249 24187-24187 TAG                     com.kotlin.kotlin_flow_part_1        D  collectFlow: Finished eating Appetizer
//2026-06-16 18:01:02.249 24187-24187 TAG                     com.kotlin.kotlin_flow_part_1        D  collectFlow: Now eating Dessert
//2026-06-16 18:01:07.255 24187-24187 TAG                     com.kotlin.kotlin_flow_part_1        D  collectFlow: Finished eating Dessert

//Collect latest
//---------------------------- PROCESS ENDED (24187) for package com.kotlin.kotlin_flow_part_1 ----------------------------
//---------------------------- PROCESS STARTED (24305) for package com.kotlin.kotlin_flow_part_1 ----------------------------
//2026-06-16 18:14:20.140 24305-24305 TAG                     com.kotlin.kotlin_flow_part_1        D  collectFlow: Appetizer is delivered
//2026-06-16 18:14:20.140 24305-24305 TAG                     com.kotlin.kotlin_flow_part_1        D  collectFlow: Now eating Appetizer
//2026-06-16 18:14:21.644 24305-24305 TAG                     com.kotlin.kotlin_flow_part_1        D  collectFlow: Main dish is delivered
//2026-06-16 18:14:21.645 24305-24305 TAG                     com.kotlin.kotlin_flow_part_1        D  collectFlow: Now eating Main dish
//2026-06-16 18:14:22.147 24305-24305 TAG                     com.kotlin.kotlin_flow_part_1        D  collectFlow: Dessert is delivered
//2026-06-16 18:14:22.148 24305-24305 TAG                     com.kotlin.kotlin_flow_part_1        D  collectFlow: Now eating Dessert
//2026-06-16 18:14:27.151 24305-24305 TAG                     com.kotlin.kotlin_flow_part_1        D  collectFlow: Finished eating Dessert
