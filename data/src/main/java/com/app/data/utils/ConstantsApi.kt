package com.app.data.utils


object ConstantsApi {

    var VARIANT_URL = ""


    object PrefKeys {
        const val TOKEN = "TOKEN"
    }


}


object ErrorAPI {
    //500
    const val SERVER_ERROR = "serverError"

    //400
    const val BAD_REQUEST = "bad_request"

    const val UNAUTHRIZED = "unauthrized"

    //no internet connection
    const val CONNECTION_ERROR = "connection_error"

}
