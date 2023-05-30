package com.app.usecase

import android.os.RemoteException
import android.util.AndroidException
import com.app.data.model.BaseEndPointResponse
import com.app.data.utils.ErrorAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.transform
import retrofit2.Response
import java.io.IOException
import java.net.*

inline fun <T, R> Flow<Response<BaseEndPointResponse<T>>>.transformResponseData(
    crossinline onSuccess: suspend FlowCollector<R>.(T) -> Unit
): Flow<R> {
    return transform { response ->
        when {
            response.isSuccessful && response.body() != null && response.code() in 200..299 ->
                onSuccess(response.body()!!.data!!)
            response.isSuccessful && response.body() != null && response.body()!!.code !in 200..299 ->
                onSuccess(response.body()!!.status!! as T)
            response.code() == 401 -> throw Throwable(ErrorAPI.UNAUTHRIZED)
            response.code() in 401..499 && response.errorBody() == null ->
                throw Throwable(ErrorAPI.BAD_REQUEST)
            response.code() in 500..599 -> throw Throwable(ErrorAPI.SERVER_ERROR)
            else -> throw Exception()
        }
    }
}


