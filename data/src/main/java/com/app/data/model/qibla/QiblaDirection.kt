package com.app.data.model.qibla

import com.google.gson.annotations.SerializedName

data class QiblaDirection(
    @SerializedName("latitude") var latitude: Double? = null,
    @SerializedName("longitude") var longitude: Double? = null,
    @SerializedName("direction") var direction: Double? = null

)
