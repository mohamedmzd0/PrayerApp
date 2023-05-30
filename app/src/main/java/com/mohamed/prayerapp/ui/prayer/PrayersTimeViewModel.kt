package com.mohamed.prayerapp.ui.prayer

import com.app.base.BaseViewModel
import com.app.data.remote.NetWorkState
import com.app.usecase.AppUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class PrayersTimeViewModel @Inject constructor(private val useCase: AppUseCase) : BaseViewModel() {

    // prayers flow
    private val _prayersFlow = MutableStateFlow<NetWorkState>(NetWorkState.Loading)
    val prayersFlow = _prayersFlow.asSharedFlow()


    // qibla flow
    private val _qiblaFlow = MutableStateFlow<NetWorkState>(NetWorkState.Loading)
    val qiblaFlow = _qiblaFlow.asSharedFlow()

    // get prayers time
    fun getPrayersTime( latitude: Double, longitude: Double) {
        executeSharedApi(_prayersFlow) {
            useCase.getPrayerTimes( latitude, longitude)
                .onStart { _prayersFlow.emit(NetWorkState.Loading) }
                .catch { _prayersFlow.emit(NetWorkState.Error(it)) }
                .onCompletion { _prayersFlow.emit(NetWorkState.DismissLoading) }
                .collectLatest { _prayersFlow.emit(NetWorkState.Success(it)) }
        }
    }

    // get qibla direction
    fun getQiblaDirection(latitude: Double, longitude: Double) {
        executeSharedApi(_qiblaFlow) {
            useCase.getQiblaDirection(latitude, longitude)
                .onStart { _qiblaFlow.emit(NetWorkState.Loading) }
                .catch { _qiblaFlow.emit(NetWorkState.Error(it)) }
                .onCompletion { _qiblaFlow.emit(NetWorkState.DismissLoading) }
                .collectLatest { _qiblaFlow.emit(NetWorkState.Success(it)) }
        }
    }


}