package com.app.data.model.pray_times

import com.google.gson.annotations.SerializedName


data class Timings(

    @SerializedName("Fajr") var Fajr: String? = null,
    @SerializedName("Sunrise") var Sunrise: String? = null,
    @SerializedName("Dhuhr") var Dhuhr: String? = null,
    @SerializedName("Asr") var Asr: String? = null,
    @SerializedName("Sunset") var Sunset: String? = null,
    @SerializedName("Maghrib") var Maghrib: String? = null,
    @SerializedName("Isha") var Isha: String? = null,
//    @SerializedName("Imsak") var Imsak: String? = null,
//    @SerializedName("Midnight") var Midnight: String? = null,
//    @SerializedName("Firstthird") var Firstthird: String? = null,
//    @SerializedName("Lastthird") var Lastthird: String? = null

)





data class Date(

    @SerializedName("readable") var readable: String? = null,
//    @SerializedName("timestamp") var timestamp: String? = null,
//    @SerializedName("gregorian") var gregorian: Gregorian? = Gregorian(),
//    @SerializedName("hijri") var hijri: Hijri? = Hijri()

)


data class Meta(

//    @SerializedName("latitude") var latitude: Double? = null,
//    @SerializedName("longitude") var longitude: Double? = null,
    @SerializedName("timezone") var timezone: String? = null,
//    @SerializedName("method") var method: Method? = Method(),
//    @SerializedName("latitudeAdjustmentMethod") var latitudeAdjustmentMethod: String? = null,
//    @SerializedName("midnightMode") var midnightMode: String? = null,
//    @SerializedName("school") var school: String? = null,
//    @SerializedName("offset") var offset: Offset? = Offset()

)

data class PrayerTimes(

    @SerializedName("timings") var timings: Timings? = Timings(),
    @SerializedName("date") var date: Date? = Date(),
    @SerializedName("meta") var meta: Meta? = Meta(),

    var timingsArrayList: ArrayList<PrayItem> = arrayListOf()
) {
    var nextPrayingTime: PrayItem? = null
}

data class PrayItem(
    var name: String? = null,
    var time: String? = null
)