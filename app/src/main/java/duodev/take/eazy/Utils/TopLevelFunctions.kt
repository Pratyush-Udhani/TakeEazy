package duodev.take.eazy.Utils

import android.os.Handler

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
