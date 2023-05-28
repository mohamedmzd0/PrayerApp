package com.app.usecase

import android.os.RemoteException
import android.util.AndroidException
import com.app.data.model.EndPointResponse
import com.app.data.utils.ErrorAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.transform
import retrofit2.Response
import java.io.IOException
import java.net.*

inline fun <T, R> Flow<Response<EndPointResponse<T>>>.transformResponseData(
    crossinline onSuccess: suspend FlowCollector<R>.(T) -> Unit
): Flow<R> {
    return transform {
        when {


            it.isSuccessful && it.body() != null && (it.body()!!.code in 200..300) ->
                onSuccess(it.body()!!.data!!)

            it.isSuccessful && it.body() != null && it.body()!!.code !in 200..300 ->
                onSuccess(it.body()!!.status!! as T)


            it.code() == 401 -> throw Throwable(ErrorAPI.UNAUTHRIZED)


            it.code() in 401..499 && it.errorBody() == null -> throw Throwable(ErrorAPI.BAD_REQUEST)

            it.code() in 500..599 -> throw Throwable(ErrorAPI.SERVER_ERROR)

            else -> {
                throw Throwable().handleException()
            }

        }
    }
}


fun Throwable.handleException(): Throwable {
    return if (this is AndroidException || this is RemoteException || this is BindException || this is PortUnreachableException || this is SocketTimeoutException || this is UnknownServiceException || this is UnknownHostException || this is IOException || this is ConnectException || this is NoRouteToHostException) {
        Throwable(ErrorAPI.CONNECTION_ERROR)
    } else {
        this
    }
}
