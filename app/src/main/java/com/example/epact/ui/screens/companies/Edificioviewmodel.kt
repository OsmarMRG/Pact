package com.example.epact.ui.screens.companies

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epact.data.RetrofitClient
import com.example.epact.model.EdificioData
import kotlinx.coroutines.launch

class EdificioViewModel : ViewModel() {
    var edificios = mutableStateOf<List<EdificioData>>(emptyList())
    var isLoading  = mutableStateOf(false)

    init { load() }

    fun load() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = RetrofitClient.instance.getEdificios()
                edificios.value = response.data ?: emptyList()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading.value = false
            }
        }
    }
}