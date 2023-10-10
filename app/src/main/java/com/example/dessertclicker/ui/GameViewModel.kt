package com.example.dessertclicker.ui

import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.DESSERT_INCREASE
import com.example.dessertclicker.data.Datasource.dessertList
import com.example.dessertclicker.data.SCORE_INCREASE
import com.example.dessertclicker.model.Dessert
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class GameViewModel: ViewModel() {
    private var _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState

    var userImput by mutableStateOf(0)
        private set

    fun OnDessertClick() {
        val updateDessertsSold = _uiState.value.dessertsSold.plus(DESSERT_INCREASE)
        val updateRevenue = _uiState.value.revenue.plus(SCORE_INCREASE)
        UpdateGameState(updateDessertsSold, updateRevenue)
    }

    fun DetermineDessertToShow() {
        val desserts = dessertList
        val dessertsSold = _uiState.value.dessertsSold
        var dessertToShow = desserts.first()
            for (dessert in desserts) {
                if (dessertsSold >= dessert.startProductionAmount) {
                    dessertToShow = dessert
                } else {
                    // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
                    // you'll start producing more expensive desserts as determined by startProductionAmount
                    // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
                    // than the amount sold.
                    break
                }
            }
        updateDessert(dessertToShow)
    }

    fun UpdateGameState(updateDessertSold: Int, updateRevenue: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                dessertsSold = updateDessertSold,
                revenue = updateRevenue
            )
        }
    }
    fun updateDessert(dessert: Dessert) {
        _uiState.update { currentState ->
            currentState.copy(
                currentDessertIndex = dessert.startProductionAmount,
                currentDessertPrice = dessert.price,
                currentDessertImageId = dessert.imageId
            )
        }
    }
}