package com.view.wordwise.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.view.wordwise.data.model.WordDefinitionEntity
import com.view.wordwise.data.repository.WordRepository
import com.view.wordwise.utils.ConnectivityObserver
import com.view.wordwise.utils.ConnectivityStatus
import com.view.wordwise.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WordViewModel @Inject constructor(
    private val repository: WordRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState<WordDefinitionEntity>>(UIState.Default)
    val uiState: StateFlow<UIState<WordDefinitionEntity>> = _uiState
    private val connectivityObserver = ConnectivityObserver(context)
    private val _networkStatus = MutableStateFlow(ConnectivityStatus.UNAVAILABLE)
    val networkStatus: StateFlow<ConnectivityStatus> = _networkStatus

    init {
        observeNetworkStatus()
    }

    private fun observeNetworkStatus() {
        viewModelScope.launch {
            connectivityObserver.observeConnectivity().collect { status ->
                _networkStatus.value = status
            }
        }
    }

    fun fetchWordDefinition(word: String) {

        viewModelScope.launch {
            _uiState.value = UIState.Loading
            val result = repository.getWordDefinition(word)
            _uiState.value = result.fold(
                onSuccess = { UIState.Success(it) },
                onFailure = {
                    if (_networkStatus.value != ConnectivityStatus.AVAILABLE) {
                        UIState.Error(Throwable("No Network available"))
                    } else {
                        UIState.Error(it)
                    }
                }
            )
        }
    }
}
