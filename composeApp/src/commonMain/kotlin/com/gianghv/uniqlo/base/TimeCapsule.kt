package com.gianghv.uniqlo.base

interface TimeCapsule<S : Reducer.UiState> {
    fun addState(state: S)
    fun selectState(position: Int)
    fun getStates(): List<S>
}

class TimeTravelCapsule<S : Reducer.UiState>(
    private val onStateSelected: (S) -> Unit
) : TimeCapsule<S> {

    private val states = mutableListOf<S>()

    override fun addState(state: S) {
        states.add(state)
    }

    override fun selectState(position: Int) {
        onStateSelected(states[position])
    }

    override fun getStates(): List<S> {
        return states
    }
}
