package com.view.wordwise.utils

sealed class UIState<out T> {
    object Loading : UIState<Nothing>()
    object Default : UIState<Nothing>()
    data class Success<out T>(val data: T) : UIState<T>()
    data class Error(val exception: Throwable) : UIState<Nothing>()
}
