package com.example.epact.ui.screens.media

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epact.data.RetrofitClient
import com.example.epact.model.MediaPactData
import kotlinx.coroutines.launch

class MediaViewModel : ViewModel() {
    var items = mutableStateOf<List<MediaPactData>>(emptyList())
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    init {
        loadMedia()
    }

    fun loadMedia() {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            try {
                val response = RetrofitClient.instance.getMediaPact()
                // Ordena por campo "ordem" — itens sem ordem ficam no fim
                items.value = (response.data ?: emptyList())
                    .sortedWith(compareBy(nullsLast()) { it.ordem })
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage.value = "Erro ao carregar media"
            } finally {
                isLoading.value = false
            }
        }
    }
}