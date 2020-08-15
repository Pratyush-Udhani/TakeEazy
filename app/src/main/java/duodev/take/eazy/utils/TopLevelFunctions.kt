package duodev.take.eazy.utils

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import duodev.take.eazy.pojo.Users
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
val isAuth = MutableLiveData<Boolean>(false)
val noPassword = MutableLiveData<Boolean>(false)
val noEmail = MutableLiveData<Boolean>(false)
val pm = PreferenceUtils

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

fun getAddedInt(count: String): Int {
    var newCount = Integer.parseInt(count)
    newCount++
    return newCount
}

fun getSubInt(count: String): Int {
    var newCount = Integer.parseInt(count)
    if (newCount > 0) newCount-- else newCount = 0
    return newCount
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

fun checkAuth(phone: String, password: String) {
    isAuth.value = false
    noPassword.value = false
    noEmail.value = false
    val hash = generateHash(password)

    firebaseFirestore.collection(USERS).document(phone).get()
        .addOnCompleteListener {
            if (it.isSuccessful) {
                if (it.result!!.exists() && it.result!! != null) {
                    if (it.result!!.get("userPassword") == hash) {
                        isAuth.value = true
                        pm.setUser(convertToPojo(it.result!!.data!!, Users::class.java))
                    } else {
                        noPassword.value = true
                    }
                } else {
                    noEmail.value = true
                }
            }
        }
}
