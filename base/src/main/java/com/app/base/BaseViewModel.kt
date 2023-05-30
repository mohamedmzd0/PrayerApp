package com.app.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.data.remote.NetWorkState
import com.app.data.utils.ConstantsApi
import com.app.data.utils.ErrorAPI
import com.app.usecase.handleException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {


    private val parentJob = Job()
    private fun handlerShared(state: MutableSharedFlow<NetWorkState>): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { coroutineContext, throwable ->
            when (throwable.message) {
                ErrorAPI.UNAUTHRIZED -> {
                }
                else -> {
                    state.tryEmit(NetWorkState.Error(throwable.handleException()))
                }
            }
        }
    }


    fun executeSharedApi(
        state: MutableSharedFlow<NetWorkState>, job: Job = parentJob, action: suspend () -> Unit
    ) {
        viewModelScope.launch(handlerShared(state)) {
            action()
        }
    }


    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

}


