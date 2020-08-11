package duodev.take.eazy.utils

import android.os.Handler
import com.google.gson.Gson
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

fun <T> convertToPojo(
    data: MutableMap<String, Any>,
    pojo: Class<T>
): T {
    val gson = Gson()
    data.toMap()
    val jsonElement = gson.toJsonTree(data)
    return gson.fromJson(jsonElement, pojo)
}

fun runInHandler(time: Long, action: () -> Unit) {
    Handler().postDelayed(action,time)
}

fun getDistance(
    lat1: Double,
    lon1: Double,
    lat2: Double,
    lon2: Double
): Double {
    val theta = lon1 - lon2
    var dist = (sin(deg2rad(lat1))
            * sin(deg2rad(lat2))
            + (cos(deg2rad(lat1))
            * cos(deg2rad(lat2))
            * cos(deg2rad(theta))))
    dist = acos(dist)
    dist = rad2deg(dist)
    dist *= 60 * 1.1515
    return dist
}

fun deg2rad(deg: Double): Double {
    return deg * Math.PI / 180.0
}

fun rad2deg(rad: Double): Double {
    return rad * 180.0 / Math.PI
}
