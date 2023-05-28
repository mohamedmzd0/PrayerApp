package com.app.data.utils

import android.os.RemoteException
import android.util.AndroidException
import android.util.Log
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import java.net.*


fun ResponseBody.handleError():String {
    val json = JSONObject(this.string())
    if (json.has("data")){
        var json=json.getJSONObject("data")
        return if (json.has("errors")) {
            val errors = json.getJSONArray("errors").getJSONObject(0)
            val errorMessage = errors.getString("value")
            Log.e("errorMessage",errorMessage)
            val key = errors.getString("key")
            errorMessage
        }

        else if (json.has("error")){
            val jsonError = json.getString("error")
            //val errorMessage = jsonError.getString("error")
            jsonError
        }
        else if (json.has("message")){
            val jsonError = json.getString("message")
            //val errorMessage = jsonError.getString("error")
            jsonError
        }
        else {
            val jsonError = json.getJSONArray("error").getJSONObject(0)
            val errorMessage = jsonError.getString("value")
            errorMessage

        }
    }
    else {
        return if (json.has("errors")) {
            val errors = json.getJSONArray("errors").getJSONObject(0)
            val errorMessage = errors.getString("value")
            Log.e("errorMessage", errorMessage)
            val key = errors.getString("key")
            errorMessage
        } else if (json.has("error")) {
            val jsonError = json.getString("error")
            //val errorMessage = jsonError.getString("error")
            jsonError
        } else if (json.has("message")) {
            val jsonError = json.getString("message")
            //val errorMessage = jsonError.getString("error")
            jsonError
        } else {
            val jsonError = json.getJSONArray("error").getJSONObject(0)
            val errorMessage = jsonError.getString("value")
            errorMessage

        }
    }


}


data class CustomErrorThrow(val key:String,val value:String):Throwable()


