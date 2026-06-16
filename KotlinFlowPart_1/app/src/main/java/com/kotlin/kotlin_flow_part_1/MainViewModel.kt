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
            }.conflate()
                .collect {
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
