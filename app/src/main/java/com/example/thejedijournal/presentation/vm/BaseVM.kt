package com.example.thejedijournal.presentation.vm

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class BaseState

abstract class BaseVM<State: BaseState> {

    abstract fun initialState(): State

    internal val mutableState by lazy { MutableStateFlow(initialState()) }

    val state: StateFlow<State>
        get() = mutableState
}
